package me.ancientri.symbols.init

import com.google.devtools.ksp.getVisibility
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.*
import org.intellij.lang.annotations.Language

class InitProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
	private var ksFile: KSFile? = null
	private var classes = mutableListOf<KSClassDeclaration>()

	override fun process(resolver: Resolver): List<KSAnnotated> = with(environment) {
		logger.info("Trying to generate $INIT_FILE_NAME.kt")
		val symbols = resolver.getSymbolsWithAnnotation(AutoInit::class.qualifiedName!!)

		for (symbol in symbols) {
			if (symbol is KSClassDeclaration) {
				if (symbol.classKind == ClassKind.OBJECT) {
					if (symbol.getVisibility() == Visibility.PUBLIC) classes += symbol
					else logger.error("Only public objects can be annotated with @AutoInit", symbol)
				} else logger.error("Only objects can be annotated with @AutoInit", symbol)
			} else logger.error("Only singleton classes (objects) can be annotated with @AutoInit", symbol)
		}

		logger.info("Found ${classes.size} classes to initialize.")

		ksFile = resolver.getNewFiles().firstOrNull() // This file is different for each project using this processor, so we can't just get it by name.

		return emptyList()
	}

	@Suppress("KDocUnresolvedReference") // False positive. It's a reference in the generated file's template, not in the code. Not being resolved here doesn't matter. It will be resolved in the generated file.
	override fun finish() = with(environment) {
		when {
			ksFile == null -> {
				logger.error("No file to write to.")
				return
			}

			classes.isEmpty() -> {
				logger.warn("No classes to initialize.") // Could be a normal case, so it's a warning.
				return
			}
		}

		classes.sortBy { clazz -> clazz.annotations.first { it.shortName.asString() == AutoInit::class.simpleName || it.shortName.asString() == AutoInit::class.qualifiedName }.arguments.first().value as Int }

		codeGenerator
			.createNewFile(Dependencies(false, ksFile!!), ksFile!!.packageName.asString(), INIT_FILE_NAME)
			.bufferedWriter()
			.use { writer ->
				writer.append(
					syntaxHighlighter(
						// region Generated Code
						// This has no indentations, because with indents we have to add way more \t to balance it out when we call trimIndent()
						"""
package ${ksFile!!.packageName.asString()}

/**
 * This file is generated by the InitProcessor to initialize features marked with [AutoInit][me.ancientri.symbols.init.AutoInit]. Do not edit it, as it will be overwritten.
 *
 * Include a reference to this object in your project to trigger the initialization of the features. This relies on kotlin's objects being initialized lazily, upon first access.
 * 
 * **Note that having multiple references to this object will not reinitialize the features, as the initialization of kotlin objects is done only once.**
 */
internal object $INIT_FILE_NAME {
	init {
		${classes.mapNotNull { it.qualifiedName?.asString() }.joinToString("\n\t\t") { it }}
	}
}
"""
						// endregion Generated Code
					)
				)
			}

		logger.info("Generated $INIT_FILE_NAME.kt")
	}

	companion object {
		const val INIT_FILE_NAME = "Initializer"

		/**
		 * A simple syntax highlighter for Kotlin code, using the IntelliJ IDEA's language injection feature.
		 *
		 * I don't know why this can't be applied to string literals directly, so this is a workaround.
		 */
		@Suppress("NOTHING_TO_INLINE")
		inline fun syntaxHighlighter(@Language("kotlin") code: String) = code.trimIndent()
	}
}