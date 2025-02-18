import magik.createGithubPublication
import magik.github

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