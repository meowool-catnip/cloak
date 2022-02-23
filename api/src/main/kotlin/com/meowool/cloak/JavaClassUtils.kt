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
@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "NOTHING_TO_INLINE")

package com.meowool.cloak

import com.meowool.cloak.internal.MatchingDistance.isMatched
import com.meowool.cloak.internal.calculateDistanceBetween
import com.meowool.sweekt.LazyInit
import com.meowool.sweekt.cast
import java.lang.Boolean as BooleanObject
import java.lang.Byte as ByteObject
import java.lang.Character as CharObject
import java.lang.Double as DoubleObject
import java.lang.Float as FloatObject
import java.lang.Integer as IntObject
import java.lang.Long as LongObject
import java.lang.Short as ShortObject

@PublishedApi @LazyInit internal val BOOLEAN_OBJECT = BooleanObject::class.java
@PublishedApi @LazyInit internal val CHAR_OBJECT = CharObject::class.java
@PublishedApi @LazyInit internal val BYTE_OBJECT = ByteObject::class.java
@PublishedApi @LazyInit internal val SHORT_OBJECT = ShortObject::class.java
@PublishedApi @LazyInit internal val INT_OBJECT = IntObject::class.java
@PublishedApi @LazyInit internal val LONG_OBJECT = LongObject::class.java
@PublishedApi @LazyInit internal val FLOAT_OBJECT = FloatObject::class.java
@PublishedApi @LazyInit internal val DOUBLE_OBJECT = DoubleObject::class.java
@PublishedApi @LazyInit internal val VOID_OBJECT = Void::class.java

@PublishedApi @LazyInit internal val NUMBER_OBJECT = Number::class.java

/**
 * Returns `true` if this class is an object type.
 *
 * @author RinOrz
 */
val Class<*>.isObject: Boolean
  get() = when {
    isArray -> componentType.isObject
    else -> !isPrimitiveOrPrimitiveArray && this != Void.TYPE
  }

/**
 * Returns `true` if this class is a [Byte] type or [Byte] wrapper type.
 *
 * @author RinOrz
 */
inline val Class<*>.isBytePrimitiveOrWrapper: Boolean get() = this == BYTE_OBJECT || this == ByteObject.TYPE

/**
 * Returns `true` if this class is a [Short] type or [Short] wrapper type.
 *
 * @author RinOrz
 */
inline val Class<*>.isShortPrimitiveOrWrapper: Boolean get() = this == SHORT_OBJECT || this == ShortObject.TYPE

/**
 * Returns `true` if this class is a [Int] type or [Int] wrapper type.
 *
 * @author RinOrz
 */
inline val Class<*>.isIntPrimitiveOrWrapper: Boolean get() = this == INT_OBJECT || this == IntObject.TYPE

/**
 * Returns `true` if this class is a [Long] type or [Long] wrapper type.
 *
 * @author RinOrz
 */
inline val Class<*>.isLongPrimitiveOrWrapper: Boolean get() = this == LONG_OBJECT || this == LongObject.TYPE

/**
 * Returns `true` if this class is a [Float] type or [Float] wrapper type.
 *
 * @author RinOrz
 */
inline val Class<*>.isFloatPrimitiveOrWrapper: Boolean get() = this == FLOAT_OBJECT || this == FloatObject.TYPE

/**
 * Returns `true` if this class is a [Double] type or [Double] wrapper type.
 *
 * @author RinOrz
 */
inline val Class<*>.isDoublePrimitiveOrWrapper: Boolean get() = this == DOUBLE_OBJECT || this == DoubleObject.TYPE

/**
 * Returns `true` if this class is a [Boolean] type or [Boolean] wrapper type.
 *
 * @author RinOrz
 */
inline val Class<*>.isBooleanPrimitiveOrWrapper: Boolean get() = this == BOOLEAN_OBJECT || this == BooleanObject.TYPE

/**
 * Returns `true` if this class is a [Char] type or [Char] wrapper type.
 *
 * @author RinOrz
 */
inline val Class<*>.isCharPrimitiveOrWrapper: Boolean get() = this == CHAR_OBJECT || this == CharObject.TYPE

/**
 * Returns `true` if this class is a primitive wrapper class.
 *
 * @author RinOrz
 */
val Class<*>.isPrimitiveWrapper: Boolean
  get() = when {
    isArray -> componentType.isPrimitiveWrapper
    isObject ->
      this == BOOLEAN_OBJECT ||
        this == CHAR_OBJECT ||
        this == BYTE_OBJECT ||
        this == DOUBLE_OBJECT ||
        this == FLOAT_OBJECT ||
        this == INT_OBJECT ||
        this == LONG_OBJECT ||
        this == SHORT_OBJECT ||
        this == VOID_OBJECT
    else -> false
  }

/**
 * Returns `true` if this class is a primitive wrapper class.
 *
 * @author RinOrz
 */
val Class<*>.isPrimitiveOrPrimitiveArray: Boolean
  get() = when {
    isArray -> componentType.run { this != Void.TYPE && isPrimitive }
    else -> isPrimitive || this == Void.TYPE
  }

/**
 * Returns `true` if this class is a primitive or wrapper class.
 *
 * @author RinOrz
 */
inline val Class<*>.isPrimitiveOrWrapper: Boolean
  get() = isPrimitiveOrPrimitiveArray || isPrimitiveWrapper || this == Void.TYPE

/**
 * Returns `true` if this class is a primitive number class.
 *
 * @author RinOrz
 */
val Class<*>.isPrimitiveNumber: Boolean
  get() = this == ByteObject.TYPE ||
    this == ShortObject.TYPE ||
    this == IntObject.TYPE ||
    this == LongObject.TYPE ||
    this == FloatObject.TYPE ||
    this == DoubleObject.TYPE

/**
 * Returns `true` if this class is a primitive wrapper class.
 *
 * @author RinOrz
 */
inline val Class<*>.isBoxedNumber: Boolean get() = NUMBER_OBJECT.isAssignableFrom(this)

/**
 * Returns `true` if this class is kind of number.
 *
 * @author RinOrz
 */
inline val Class<*>.isNumber: Boolean get() = isPrimitiveNumber || isBoxedNumber

/**
 * Returns an array that contains all the classes of primitives.
 *
 * @param containVoid Whether to include [Void.TYPE] in the array.
 * @author RinOrz
 */
fun arrayOfPrimitives(containVoid: Boolean = false): Array<Class<out Any>> = when {
  containVoid -> arrayOf(
    BooleanObject.TYPE,
    CharObject.TYPE,
    ByteObject.TYPE,
    ShortObject.TYPE,
    IntObject.TYPE,
    LongObject.TYPE,
    FloatObject.TYPE,
    DoubleObject.TYPE,
    Void.TYPE
  )
  else -> arrayOf(
    BooleanObject.TYPE,
    CharObject.TYPE,
    ByteObject.TYPE,
    ShortObject.TYPE,
    IntObject.TYPE,
    LongObject.TYPE,
    FloatObject.TYPE,
    DoubleObject.TYPE
  )
}

/**
 * Returns an array that contains all the classes of primitive wrappers.
 *
 * @param containVoid Whether to include [Void] in the array.
 * @author RinOrz
 */
fun arrayOfPrimitiveWrappers(containVoid: Boolean = false): Array<Class<out Any>> = when {
  containVoid -> arrayOf(
    BOOLEAN_OBJECT,
    CHAR_OBJECT,
    BYTE_OBJECT,
    SHORT_OBJECT,
    INT_OBJECT,
    LONG_OBJECT,
    FLOAT_OBJECT,
    DOUBLE_OBJECT,
    VOID_OBJECT
  )
  else -> arrayOf(
    BOOLEAN_OBJECT,
    CHAR_OBJECT,
    BYTE_OBJECT,
    SHORT_OBJECT,
    INT_OBJECT,
    LONG_OBJECT,
    FLOAT_OBJECT,
    DOUBLE_OBJECT
  )
}

/**
 * Returns an unboxing class. If this class is not a primitive wrapper type, nothing will be done.
 *
 * @author RinOrz
 */
fun <T> Class<T>.unboxing(): Class<T> {
  if (isPrimitive) return this

  return when (name) {
    "java.lang.Boolean" -> BooleanObject.TYPE
    "java.lang.Character" -> CharObject.TYPE
    "java.lang.Byte" -> ByteObject.TYPE
    "java.lang.Short" -> ShortObject.TYPE
    "java.lang.Integer" -> IntObject.TYPE
    "java.lang.Long" -> LongObject.TYPE
    "java.lang.Float" -> FloatObject.TYPE
    "java.lang.Double" -> DoubleObject.TYPE
    "java.lang.Void" -> Void.TYPE
    else -> this
  }.cast()
}

/**
 * Returns a boxed object class. If this class is not a primitive type, nothing will be done.
 *
 * @author RinOrz
 */
fun <T> Class<T>.boxing(): Class<T> {
  if (!isPrimitive) return this

  return when (name) {
    "boolean" -> BOOLEAN_OBJECT
    "char" -> CHAR_OBJECT
    "byte" -> BYTE_OBJECT
    "short" -> SHORT_OBJECT
    "int" -> INT_OBJECT
    "long" -> LONG_OBJECT
    "float" -> FLOAT_OBJECT
    "double" -> DOUBLE_OBJECT
    "void" -> VOID_OBJECT
    else -> this
  }.cast()
}

/**
 * Returns `true` if this instance can be cast to the specified [base] class.
 *
 * This function is a bit like Java's keyword `instanceof` and Kotlin's keyword `is`, but the conditions for this
 * function are more relaxed:
 *
 * - If this instance is `null` and the [base] class is non-primitive, the condition is `true`.
 *
 * - If this instance is a primitive number and the [base] class also is a primitive number type, the condition
 *   is `true`, because any kind of primitive number can be converted to each other in Java, such as `(int) 10f`.
 *
 * - If this instance is a primitive or primitive wrapper, regardless whether the [base] class is a primitive type or
 *   a primitive wrapper type, once they are of the same kind (e.g. [Boolean] equals [java.lang.Boolean]),
 *   the conditions are `true`, because expressions like `(double) Double.valueOf(1.0)` or `(Double) 1.0` are
 *   allowed in Java.
 *
 * @author RinOrz
 */
inline infix fun Any?.canCastTo(base: Class<*>): Boolean = when (this) {
  is Class<*> -> this canCastTo base
  else -> this?.javaClass canCastTo base
}

/**
 * Returns `true` if this class can be cast to the specified [base] class.
 *
 * This function is a bit like Java's keyword `instanceof` and Kotlin's keyword `is` and [Class.isAssignableFrom], but
 * the conditions for this function are more relaxed:
 *
 * - If this class is `null` and the [base] class is non-primitive, the condition is `true`.
 *
 * - If this class is a primitive number and the [base] class also is a primitive number type, the condition
 *   is `true`, because any kind of primitive number can be converted to each other in Java, such as `(int) 10f`.
 *
 * - If this class is a primitive or primitive wrapper, regardless whether the [base] class is a primitive type or
 *   a primitive wrapper type, once they are of the same kind (e.g. [Boolean] equals [java.lang.Boolean]),
 *   the conditions are `true`, because expressions like `(double) Double.valueOf(1.0)` or `(Double) 1.0` are
 *   allowed in Java.
 *
 * @author RinOrz
 */
infix fun Class<*>?.canCastTo(base: Class<*>): Boolean = calculateDistanceBetween(
  declared = base,
  passed = this,
  depth = false
).isMatched
