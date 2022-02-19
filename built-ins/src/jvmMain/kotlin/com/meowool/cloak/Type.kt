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
@file:Suppress("FunctionName", "NOTHING_TO_INLINE", "MemberVisibilityCanBePrivate", "unused")

package com.meowool.cloak

import com.meowool.sweekt.cast
import com.meowool.sweekt.castOrNull
import com.meowool.sweekt.toJvmQualifiedTypeName
import com.meowool.sweekt.toJvmTypeDescriptor
import kotlin.reflect.KClass
import java.lang.Class.forName as classOf

/**
 * Safe type representation of a run time class.
 *
 * @author 凛 (RinOrz)
 */
@JvmInline value class Type<T : Any> @PublishedApi internal constructor(private val actual: Any) {

  /**
   * Tries to return the actual type value as an instance of the java run time class.
   */
  val javaClass: Class<T> get() = actual.castOrNull<Class<T>>() ?: classOf(qualifiedName).cast()

  /**
   * Tries to return the actual type value as an instance of the kotlin run time class.
   */
  val kotlinClass: KClass<T> get() = javaClass.kotlin

  /**
   * Returns the class qualified name of this type.
   */
  val qualifiedName: String get() = (actual as? String ?: javaClass.name).toJvmQualifiedTypeName()

  /**
   * Returns the class descriptor of this type.
   */
  val descriptor: String get() = (actual as? String ?: javaClass.name).toJvmTypeDescriptor()

  override fun toString(): String = actual.toString()
}

/**
 * Type representation of a java run time class.
 *
 * @param value Java class corresponding to type.
 * @author 凛 (RinOrz)
 */
inline fun <reified T : Any> Type(value: Class<out T>): Type<T> = Type(actual = value)

/**
 * Type representation of a kotlin run time class.
 *
 * @param value Kotlin class corresponding to type.
 * @author 凛 (RinOrz)
 */
inline fun <reified T : Any> Type(value: KClass<out T> = T::class): Type<T> = Type(actual = value.java)

/**
 * Type char sequence representation of a run time class.
 *
 * @param value Type value, which can be the descriptor of the class or the qualified name of the class.
 * @author 凛 (RinOrz)
 */
inline fun <reified T : Any> Type(value: String): Type<T> = Type(actual = value)

/**
 * Type char sequence representation of a run time class.
 *
 * @param value Type value, which can be the descriptor of the class or the qualified name of the class.
 * @author 凛 (RinOrz)
 */
@JvmName("AnyType")
inline fun Type(value: String): Type<Any> = Type(actual = value)

/**
 * Returns a [Type] from this [KClass].
 *
 * @author 凛 (RinOrz)
 */
inline val <T : Any> KClass<out T>.type: Type<T> get() = Type(actual = this.java)

/**
 * Returns a [Type] from this [Class].
 *
 * @author 凛 (RinOrz)
 */
inline val <T : Any> Class<out T>.type: Type<T> get() = Type(actual = this)

/**
 * Returns a [Type] from this [String].
 *
 * @author 凛 (RinOrz)
 */
inline val String.type: Type<Any> get() = Type(this)
