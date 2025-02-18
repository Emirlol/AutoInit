import magik.createGithubPublication
import magik.github

plugins {
	alias(libs.plugins.kotlin)
	alias(libs.plugins.magik)
	`maven-publish`
}

repositories {
	mavenCentral()
}

publishing {
	publications {
		createGithubPublication {
			groupId = "me.rime.symbols"
			artifactId = "init-annotation"
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