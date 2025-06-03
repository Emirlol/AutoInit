import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin

plugins {
	alias(libs.plugins.kotlin) apply false
}

subprojects {
	group = "me.ancientri.symbols"
	version = property("version") as String

	repositories {
		mavenCentral()
	}

	plugins.withType<JavaPlugin> {
		extensions.configure<JavaPluginExtension>() {
			withSourcesJar()
		}
	}

	plugins.withType<KotlinBasePlugin> {
		extensions.configure<KotlinJvmProjectExtension> {
			jvmToolchain(21)
		}
	}
}