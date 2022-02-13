package com.meowool.cloak.internal

import com.meowool.cloak.unboxing

/**
 * Returns a new array containing the results of applying the given [transform] function to each element in the
 * original array.
 *
 * @author 凛 (RinOrz)
 */
inline fun <T, reified R> Array<out T>.fastMap(transform: (T) -> R): Array<R> =
  Array(size) { transform(this[it]) }

/**
 * Returns `true` if all elements match the given [predicate].
 *
 * @author 凛 (RinOrz)
 */
inline fun <T> Array<out T>.allIndexed(predicate: (index: Int, T) -> Boolean): Boolean {
  var index = 0
  for (element in this) if (!predicate(index++, element)) return false
  return true
}