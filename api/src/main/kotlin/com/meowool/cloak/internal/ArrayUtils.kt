/*
 * Copyright (c) 2022. The Meowool Organization Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * In addition, if you modified the project, you must include the Meowool
 * organization URL in your code file: https://github.com/meowool
 *
 * 如果您修改了此项目，则必须确保源文件中包含 Meowool 组织 URL: https://github.com/meowool
 */
package com.meowool.cloak.internal

/**
 * Returns a new array containing the results of applying the given [transform] function to each element in the
 * original array.
 *
 * @author 凛 (RinOrz)
 */
// TODO: Move to sweekt
inline fun <T, reified R> Array<out T>.fastMap(transform: (T) -> R): Array<R> =
  Array(size) { transform(this[it]) }

/**
 * Returns `true` if all elements match the given [predicate].
 *
 * @author 凛 (RinOrz)
 */
// TODO: Move to sweekt
inline fun <T> Array<out T>.allIndexed(predicate: (index: Int, T) -> Boolean): Boolean {
  var index = 0
  for (element in this) if (!predicate(index++, element)) return false
  return true
}
