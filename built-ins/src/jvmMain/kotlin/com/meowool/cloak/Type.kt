@file:Suppress("FunctionName", "NOTHING_TO_INLINE", "MemberVisibilityCanBePrivate", "unused")

package com.meowool.cloak

import com.meowool.sweekt.cast
import com.meowool.sweekt.castOrNull
import com.meowool.sweekt.toJvmQualifiedTypeName
import com.meowool.sweekt.toJvmTypeDescriptor
import java.lang.Class.forName as classOf
import kotlin.reflect.KClass

/**
 * Safe type representation of a runtime class.
 *
 * @author 凛 (RinOrz)
 */
@JvmInline value class Type<T : Any> @PublishedApi internal constructor(private val actual: Any) {

  /**
   * Tries to return the actual type value as an instance of the java runtime class.
   */
  val javaClass: Class<T> get() = actual.castOrNull<Class<T>>() ?: classOf(qualifiedName).cast()

  /**
   * Tries to return the actual type value as an instance of the kotlin runtime class.
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
 * Type representation of a java runtime class.
 *
 * @param value Java class corresponding to type.
 * @author 凛 (RinOrz)
 */
inline fun <reified T : Any> Type(value: Class<out T>): Type<T> = Type(actual = value)

/**
 * Type representation of a kotlin runtime class.
 *
 * @param value Kotlin class corresponding to type.
 * @author 凛 (RinOrz)
 */
inline fun <reified T : Any> Type(value: KClass<out T> = T::class): Type<T> = Type(actual = value.java)

/**
 * Type char sequence representation of a runtime class.
 *
 * @param value Type value, which can be the descriptor of the class or the qualified name of the class.
 * @author 凛 (RinOrz)
 */
inline fun <reified T : Any> Type(value: String): Type<T> = Type(actual = value)

/**
 * Type char sequence representation of a runtime class.
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