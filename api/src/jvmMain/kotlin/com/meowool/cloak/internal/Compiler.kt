package com.meowool.cloak.internal

/**
 * Represents that the member marked with annotation will be deleted at compile time.
 *
 * @author 凛 (RinOrz)
 */
@Retention(AnnotationRetention.SOURCE)
internal annotation class CompileOnly

@PublishedApi internal fun compilerImplementation(): Nothing = throw UnsupportedOperationException(
  "Implemented by cloak compiler plugin, " +
    "please make sure you have applied the plugin of https://github.com/meowool-teasn/cloak"
)