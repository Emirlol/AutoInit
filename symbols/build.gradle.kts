
import magik.createGithubPublication
import magik.github
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	alias(libs.plugins.kotlin)
	alias(libs.plugins.ksp)
	alias(libs.plugins.magik)
	`maven-publish`
}

repositories {
	mavenCentral()
}

dependencies {
	implementation(libs.symbolProcessingApi)
	libs.bundles.kotlinpoet.get().map(::implementation)
	compileOnly(project(":annotations"))
}

publishing {
	publications {
		createGithubPublication {
			groupId = "me.rime.symbols"
			artifactId = "init-processor"
			version = libs.versions.version.get()

			from(components["java"])
		}
	}
	repositories {
		github {
			name = "RimeMaven"
			domain = "Emirlol/Maven"
		}
	}
}

val targetJavaVersion = 21
java {
	toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
	withSourcesJar()
}

tasks {
	withType<JavaCompile>().configureEach {
		// ensure that the encoding is set to UTF-8, no matter what the system default is
		// this fixes some edge cases with special characters not displaying correctly
		// see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
		// If Javadoc is generated, this must be specified in that task too.
		options.encoding = "UTF-8"
		options.release.set(targetJavaVersion)
	}

	withType<KotlinCompile>().configureEach {
		compilerOptions {
			jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
		}
	}
}