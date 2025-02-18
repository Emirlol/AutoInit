package me.rime.symbols.init

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class InitProcessorProvider : SymbolProcessorProvider {
	override fun create(environment: SymbolProcessorEnvironment) = InitProcessor(environment)
}