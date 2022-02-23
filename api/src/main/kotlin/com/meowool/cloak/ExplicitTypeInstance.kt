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
@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.meowool.cloak

import com.meowool.cloak.builtin.types._Boolean
import com.meowool.cloak.builtin.types._Byte
import com.meowool.cloak.builtin.types._Character
import com.meowool.cloak.builtin.types._Double
import com.meowool.cloak.builtin.types._Float
import com.meowool.cloak.builtin.types._Integer
import com.meowool.cloak.builtin.types._Long
import com.meowool.cloak.builtin.types._Short
import com.meowool.cloak.builtin.types._boolean
import com.meowool.cloak.builtin.types._byte
import com.meowool.cloak.builtin.types._char
import com.meowool.cloak.builtin.types._double
import com.meowool.cloak.builtin.types._float
import com.meowool.cloak.builtin.types._int
import com.meowool.cloak.builtin.types._long
import com.meowool.cloak.builtin.types._short

/**
 * Used to explicitly specify the [type] of [instance].
 *
 * @author 凛 (RinOrz)
 */
class ExplicitTypeInstance<T : Any> @PublishedApi internal constructor(val instance: T?, val type: Type<T>)

/**
 * Explicitly specify that the type of this [T] instance is [actualType].
 *
 * The [ExplicitTypeInstance] wrapper class is usually parsed by the cloak-compiler, it is very useful when need to
 * explicitly specify the type of instance.
 *
 * -----------------------------------------------------------------------------------------------------------
 *
 * For example, the following argument instance will be resolved to [CharSequence]:
 * ```
 * class A(val message: CharSequence?)
 *
 * A::class.type.new(
 *   arguments = arrayOf(null.typed<CharSequence>())
 * )
 * ```
 *
 * In fact, the above code is consistent with the following code behavior:
 * ```
 * A::class.type.new(
 *   arguments = arrayOf(null),
 *   parameters = arrayOf(CharSequence::class.type)
 * )
 * ```
 *
 * @author 凛 (RinOrz)
 */
inline fun <reified T : Any> T?.typed(actualType: Type<T> = T::class.type): ExplicitTypeInstance<T> =
  ExplicitTypeInstance(this, actualType)

/**
 * Explicitly specify that the type of this byte instance is primitive type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Byte.primitiveTyped: ExplicitTypeInstance<Byte> get() = this.typed(_byte)

/**
 * Explicitly specify that the type of this short instance is primitive type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Short.primitiveTyped: ExplicitTypeInstance<Short> get() = this.typed(_short)

/**
 * Explicitly specify that the type of this int instance is primitive type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Int.primitiveTyped: ExplicitTypeInstance<Int> get() = this.typed(_int)

/**
 * Explicitly specify that the type of this long instance is primitive type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Long.primitiveTyped: ExplicitTypeInstance<Long> get() = this.typed(_long)

/**
 * Explicitly specify that the type of this float instance is primitive type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Float.primitiveTyped: ExplicitTypeInstance<Float> get() = this.typed(_float)

/**
 * Explicitly specify that the type of this double instance is primitive type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Double.primitiveTyped: ExplicitTypeInstance<Double> get() = this.typed(_double)

/**
 * Explicitly specify that the type of this char instance is primitive type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Char.primitiveTyped: ExplicitTypeInstance<Char> get() = this.typed(_char)

/**
 * Explicitly specify that the type of this boolean instance is primitive type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Boolean.primitiveTyped: ExplicitTypeInstance<Boolean> get() = this.typed(_boolean)

/**
 * Explicitly specify that the type of this byte instance is object (boxed) type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Byte.objectTyped: ExplicitTypeInstance<Byte> get() = this.typed(_Byte)

/**
 * Explicitly specify that the type of this short instance is object (boxed) type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Short.objectTyped: ExplicitTypeInstance<Short> get() = this.typed(_Short)

/**
 * Explicitly specify that the type of this int instance is object (boxed) type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Int.objectTyped: ExplicitTypeInstance<Int> get() = this.typed(_Integer)

/**
 * Explicitly specify that the type of this long instance is object (boxed) type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Long.objectTyped: ExplicitTypeInstance<Long> get() = this.typed(_Long)

/**
 * Explicitly specify that the type of this float instance is object (boxed) type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Float.objectTyped: ExplicitTypeInstance<Float> get() = this.typed(_Float)

/**
 * Explicitly specify that the type of this double instance is object (boxed) type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Double.objectTyped: ExplicitTypeInstance<Double> get() = this.typed(_Double)

/**
 * Explicitly specify that the type of this char instance is object (boxed) type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Char.objectTyped: ExplicitTypeInstance<Char> get() = this.typed(_Character)

/**
 * Explicitly specify that the type of this boolean instance is object (boxed) type.
 *
 * @see Any.typed
 * @author 凛 (RinOrz)
 */
inline val Boolean.objectTyped: ExplicitTypeInstance<Boolean> get() = this.typed(_Boolean)
