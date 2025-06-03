# AutoInit
Find all kotlin objects annotated with the `@AutoInit` annotation and generates an `Initializer` object that contains references to those found kotlin objects, to initialize them. This is based on kotlin's objects being lazily initialized upon first access.

The annotation has a single `priority` property that can be used to specify the order in which the objects should be initialized. The lower the value, the earlier the object will be initialized. Defaults to 0.

## Usage

### build.gradle.kts:
```kotlin
plugins { 
	id("com.google.devtools.ksp") version "2.1.21-2.0.1"
}

repositories {
	maven("https://ancientri.me/maven/releases") {
		name = "AncientRime"
	}
}

dependencies {
	ksp("me.ancientri.symbols:init-processor:1.0.0") // The actual processor that does the work
	compileOnly("me.ancientri.symbols:init-annotation:1.0.0") // The annotation
}
```

------

For those using `libs.versions.toml` versioning, you can use these instead:
### libs.versions.toml:
```toml
[versions]
autoInit = "1.0.0"
ksp = "2.1.21-2.0.1"

[libraries]
initProcessor = { module = "me.ancientri.symbols:init-processor", version.ref = "autoInit" }
initAnnotation = { module = "me.ancientri.symbols:init-annotation", version.ref = "autoInit" }

[plugins]
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
```
### build.gradle.kts:
```kotlin
plugins {
	alias(libs.plugins.ksp)
}

repositories {
	maven("https://ancientri.me/maven/releases") {
		name = "AncientRime"
	}
}

dependencies {
	ksp(libs.initProcessor)
	compileOnly(libs.initAnnotation)
}
```