plugins {
	alias(libs.plugins.kotlin)
	alias(libs.plugins.ksp)
	`maven-publish`
}

dependencies {
	implementation(libs.symbolProcessingApi)
	libs.bundles.kotlinpoet.get().map(::implementation)
	implementation(project(":annotations"))
}

publishing {
	repositories {
		maven("https://ancientri.me/maven/releases") {
			name = "AncientRime"
			credentials(PasswordCredentials::class)
			authentication {
				create<BasicAuthentication>("basic")
			}
		}
	}
	publications {
		create<MavenPublication>("maven") {
			groupId = "me.ancientri.symbols"
			artifactId = "init-processor"
			version = project.version as String
			from(components["java"])
		}
	}
}