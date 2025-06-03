package me.ancientri.symbols.init

/**
 * Used by the InitProcessor in the symbols module to generate an Initializer file that initializes all classes annotated with this annotation.
 *
 * @param priority The priority of the class. Classes with a lower priority will be initialized first. Defaults to 0.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AutoInit(val priority: Int = 0)
