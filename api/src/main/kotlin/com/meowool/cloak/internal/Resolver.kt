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

import com.meowool.cloak.BOOLEAN_OBJECT
import com.meowool.cloak.BYTE_OBJECT
import com.meowool.cloak.CHAR_OBJECT
import com.meowool.cloak.DOUBLE_OBJECT
import com.meowool.cloak.ExplicitTypeInstance
import com.meowool.cloak.FLOAT_OBJECT
import com.meowool.cloak.INT_OBJECT
import com.meowool.cloak.InstanceMock
import com.meowool.cloak.LONG_OBJECT
import com.meowool.cloak.SHORT_OBJECT
import com.meowool.cloak.canCastTo
import com.meowool.cloak.isBooleanPrimitiveOrWrapper
import com.meowool.cloak.isBytePrimitiveOrWrapper
import com.meowool.cloak.isCharPrimitiveOrWrapper
import com.meowool.cloak.isDoublePrimitiveOrWrapper
import com.meowool.cloak.isFloatPrimitiveOrWrapper
import com.meowool.cloak.isIntPrimitiveOrWrapper
import com.meowool.cloak.isLongPrimitiveOrWrapper
import com.meowool.cloak.isObject
import com.meowool.cloak.isPrimitiveOrPrimitiveArray
import com.meowool.cloak.isShortPrimitiveOrWrapper
import com.meowool.cloak.unboxing
import com.meowool.sweekt.cast
import com.meowool.sweekt.castOrNull
import com.meowool.sweekt.ifNull
import java.lang.System.arraycopy
import java.lang.reflect.Executable
import kotlin.Any
import kotlin.Array
import kotlin.Boolean
import kotlin.BooleanArray
import kotlin.Byte
import kotlin.ByteArray
import kotlin.Char
import kotlin.CharArray
import kotlin.Double
import kotlin.DoubleArray
import kotlin.Float
import kotlin.FloatArray
import kotlin.Int
import kotlin.IntArray
import kotlin.Long
import kotlin.LongArray
import kotlin.NullPointerException
import kotlin.PublishedApi
import kotlin.Short
import kotlin.ShortArray
import kotlin.arrayOf
import kotlin.arrayOfNulls
import kotlin.emptyArray
import kotlin.error
import java.lang.reflect.Array as ReflectArray

/**
 * Returns the resolved type of this instance.
 *
 * @author RinOrz
 */
@PublishedApi
internal fun Any?.resolveClass(): Class<*>? = when (this) {
  null -> null
  is InstanceMock<*> -> this.actualType.javaClass
  is ExplicitTypeInstance<*> -> this.type.javaClass
  else -> javaClass.unboxing()
}

/**
 * Returns the resolved instance of this instance.
 *
 * @author RinOrz
 */
@PublishedApi
internal fun Any?.resolveInstance(): Any? = when (this) {
  null -> null
  is InstanceMock<*> -> this.actual
  is ExplicitTypeInstance<*> -> this.instance
  else -> this
}

/**
 * Returns a canonical array to pass to a method with vararg parameters.
 *
 * [Reference](https://github.com/apache/commons-lang/blob/master/src/main/java/org/apache/commons/lang3/reflect/MethodUtils.java#L465)
 *
 * @author RinOrz
 */
internal fun Executable.resolveVarArgs(arguments: Array<*>): Array<*> {
  if (!isVarArgs) return arguments

  val parameters = parameterTypes
  val varargIndex = parameters.lastIndex

  // Canonical case: passed `(true, intArrayOf(10)` to `(Boolean, vararg Int)`
  if (parameters.size == arguments.size && parameters.last() == arguments.lastOrNull()?.javaClass) return arguments
  // Canonical case: passed `()` to `(vararg Any)`
  if (varargIndex == 0 && arguments.isEmpty()) return arrayOf(emptyArray<Any>())

  // Construct a new array matching the method's declared parameter types
  val newArgs = arrayOfNulls<Any>(parameters.size)

  // Copy the normal (non-varargs) arguments
  arguments.copyInto(newArgs, endIndex = varargIndex)

  val varArgType = parameters[varargIndex].componentType
  val varArgsSize = arguments.size - varargIndex

  // When passed array as a variadic arguments, may need converted to a primitive array
  if (varArgsSize == 1) {
    val passedVarArgsArray = arguments.last() as? Array<*>
    val passedVarArgType = passedVarArgsArray?.javaClass?.componentType
    if (passedVarArgsArray != null && passedVarArgType!! canCastTo varArgType) {
      // case IntArray -> Array<Int>:
      //   passed `(intArrayOf(0, 1))` to `(vararg Int?)`
      if (passedVarArgType.isPrimitiveOrPrimitiveArray && varArgType.isObject) {
        newArgs[varargIndex] = passedVarArgsArray.toObjectArray()

        // Return a new argument array that converted the primitive array of vararg to the primitive wrapper array
        return newArgs
      }

      // case Array<Int> -> IntArray:
      //   passed `(arrayOf(0, 1))` to `(vararg Int)`
      if (passedVarArgType.isObject && varArgType.isPrimitive) {
        newArgs[varargIndex] = passedVarArgsArray.toPrimitiveArray()

        // Return a new argument array that converted the primitive wrapper array of vararg to the primitive array
        return newArgs
      }

      error("Unsupported array of varargs conversion, expected: ${parameters.last()}, passed: $passedVarArgsArray")
    }
  }

  // Construct a new array for the variadic parameter
  var varArgsArray: Any = ReflectArray.newInstance(varArgType, varArgsSize)

  // Copy the variadic arguments into the varargs array.
  arraycopy(
    /* src */ arguments,
    /* srcPos */ varargIndex,
    /* dest */ varArgsArray,
    /* destPos */ 0,
    /* length */ varArgsSize
  )

  if (varArgType.isPrimitiveOrPrimitiveArray) {
    // unbox from wrapper type to primitive type
    //   case:
    //     passed `(arrayOf(0, 1))` to `(vararg Int)`
    //     passed `(arrayOf(arrayOf(0), arrayOf(1)))` to `(vararg IntArray)`
    varArgsArray = varArgsArray.cast<Array<*>>().toPrimitiveArray()
  }

  // Store the varargs array in the last position of the array to return
  newArgs[varargIndex] = varArgsArray

  // Return the canonical varargs array.
  return newArgs
}

/**
 * Create an array of primitive type from an array of wrapper type.
 *
 * [Original](https://github.com/apache/commons-lang/blob/1e0014fc1776f41bda6ecbc19c30317dc99c3300/src/main/java/org/apache/commons/lang3/ArrayUtils.java#L9609)
 *
 * @receiver an array of wrapper object
 * @return an array of the corresponding primitive type, or the original array
 * @author Apache Software Foundation
 */
private fun Array<*>.toPrimitiveArray(arrayType: Class<*> = javaClass.componentType): Any {
  fun unboxing(index: Int, expectedType: Class<*>): Any = get(index).ifNull {
    throw NullPointerException("Array element at $index is `null`, cannot be unboxed to ${expectedType.unboxing().name}.")
  }.castToPrimitiveWrapper(expectedType)

  return when {
    // Array<Byte> -> ByteArray
    arrayType.isBytePrimitiveOrWrapper -> ByteArray(size) { unboxing(it, BYTE_OBJECT).cast() }
    // Array<Short> -> ShortArray
    arrayType.isShortPrimitiveOrWrapper -> ShortArray(size) { unboxing(it, SHORT_OBJECT).cast() }
    // Array<Int> -> IntArray
    arrayType.isIntPrimitiveOrWrapper -> IntArray(size) { unboxing(it, INT_OBJECT).cast() }
    // Array<Long> -> LongArray
    arrayType.isLongPrimitiveOrWrapper -> LongArray(size) { unboxing(it, LONG_OBJECT).cast() }
    // Array<Float> -> FloatArray
    arrayType.isFloatPrimitiveOrWrapper -> FloatArray(size) { unboxing(it, FLOAT_OBJECT).cast() }
    // Array<Double> -> DoubleArray
    arrayType.isDoublePrimitiveOrWrapper -> DoubleArray(size) { unboxing(it, DOUBLE_OBJECT).cast() }
    // Array<Boolean> -> BooleanArray
    arrayType.isBooleanPrimitiveOrWrapper -> CharArray(size) { unboxing(it, CHAR_OBJECT).cast() }
    // Array<Character> -> CharArray
    arrayType.isCharPrimitiveOrWrapper -> BooleanArray(size) { unboxing(it, BOOLEAN_OBJECT).cast() }
    // Array<Array<Int>?> -> Array<IntArray?>
    arrayType.isArray -> Array(size) { this.getOrNull(it).castOrNull<Array<*>>()?.toPrimitiveArray() }
    else -> this
  }
}

/**
 * Create an array of primitive wrapper type from an array of primitive type.
 *
 * @receiver an array of primitive object
 * @return an array of the corresponding primitive wrapper type, or the original array
 * @author RinOrz
 */
private fun Array<*>.toObjectArray(arrayType: Class<*> = javaClass.componentType): Any {
  fun boxing(index: Int, expectedType: Class<*>): Any? = get(index)?.castToPrimitiveWrapper(expectedType)

  return when {
    // ByteArray -> Array<Byte>
    arrayType.isBytePrimitiveOrWrapper -> Array<Byte?>(size) { boxing(it, BYTE_OBJECT).castOrNull() }
    // ShortArray -> Array<Short>
    arrayType.isShortPrimitiveOrWrapper -> Array<Short?>(size) { boxing(it, SHORT_OBJECT).castOrNull() }
    // IntArray -> Array<Int>
    arrayType.isIntPrimitiveOrWrapper -> Array<Int?>(size) { boxing(it, INT_OBJECT).castOrNull() }
    // LongArray -> Array<Long>
    arrayType.isLongPrimitiveOrWrapper -> Array<Long?>(size) { boxing(it, LONG_OBJECT).castOrNull() }
    // FloatArray -> Array<Float>
    arrayType.isFloatPrimitiveOrWrapper -> Array<Float?>(size) { boxing(it, FLOAT_OBJECT).castOrNull() }
    // DoubleArray -> Array<Double>
    arrayType.isDoublePrimitiveOrWrapper -> Array<Double?>(size) { boxing(it, DOUBLE_OBJECT).castOrNull() }
    // BooleanArray -> Array<Boolean>
    arrayType.isBooleanPrimitiveOrWrapper -> Array<Char?>(size) { boxing(it, CHAR_OBJECT).castOrNull() }
    // CharArray -> Array<Character>
    arrayType.isCharPrimitiveOrWrapper -> Array<Boolean?>(size) { boxing(it, BOOLEAN_OBJECT).castOrNull() }
    // Array<IntArray?> -> Array<Array<Int>?>
    arrayType.isArray -> Array(size) { this.getOrNull(it).castOrNull<Array<*>>()?.toObjectArray() }
    else -> this
  }
}

/**
 * Tries converts this instance to the specified primitive (wrapper) type.
 *
 * ```
 * Integer(10) -> 10
 * true        -> true as java.lang.Boolean
 * Byte(0)     -> 0.toDouble()
 * ```
 * @author RinOrz
 */
private infix fun Any.castToPrimitiveWrapper(expectedType: Class<*>): Any {
  val actualType = javaClass
  return when {
    expectedType.isBytePrimitiveOrWrapper -> when {
      actualType.isBytePrimitiveOrWrapper -> this
      actualType.isShortPrimitiveOrWrapper -> this.cast<Short>().toByte()
      actualType.isIntPrimitiveOrWrapper -> this.cast<Int>().toByte()
      actualType.isLongPrimitiveOrWrapper -> this.cast<Long>().toByte()
      actualType.isFloatPrimitiveOrWrapper -> this.cast<Float>().toInt().toByte()
      actualType.isDoublePrimitiveOrWrapper -> this.cast<Double>().toInt().toByte()
      else -> this
    }
    expectedType.isShortPrimitiveOrWrapper -> when {
      actualType.isBytePrimitiveOrWrapper -> this.cast<Byte>().toShort()
      actualType.isShortPrimitiveOrWrapper -> this
      actualType.isIntPrimitiveOrWrapper -> this.cast<Int>().toShort()
      actualType.isLongPrimitiveOrWrapper -> this.cast<Long>().toShort()
      actualType.isFloatPrimitiveOrWrapper -> this.cast<Float>().toInt().toShort()
      actualType.isDoublePrimitiveOrWrapper -> this.cast<Double>().toInt().toShort()
      else -> this
    }
    expectedType.isIntPrimitiveOrWrapper -> when {
      actualType.isBytePrimitiveOrWrapper -> this.cast<Byte>().toInt()
      actualType.isShortPrimitiveOrWrapper -> this.cast<Short>().toInt()
      actualType.isIntPrimitiveOrWrapper -> this
      actualType.isLongPrimitiveOrWrapper -> this.cast<Long>().toInt()
      actualType.isFloatPrimitiveOrWrapper -> this.cast<Float>().toInt()
      actualType.isDoublePrimitiveOrWrapper -> this.cast<Double>().toInt()
      else -> this
    }
    expectedType.isLongPrimitiveOrWrapper -> when {
      actualType.isBytePrimitiveOrWrapper -> this.cast<Byte>().toLong()
      actualType.isShortPrimitiveOrWrapper -> this.cast<Short>().toLong()
      actualType.isIntPrimitiveOrWrapper -> this.cast<Int>().toLong()
      actualType.isLongPrimitiveOrWrapper -> this
      actualType.isFloatPrimitiveOrWrapper -> this.cast<Float>().toLong()
      actualType.isDoublePrimitiveOrWrapper -> this.cast<Double>().toLong()
      else -> this
    }
    expectedType.isFloatPrimitiveOrWrapper -> when {
      actualType.isBytePrimitiveOrWrapper -> this.cast<Byte>().toFloat()
      actualType.isShortPrimitiveOrWrapper -> this.cast<Short>().toFloat()
      actualType.isIntPrimitiveOrWrapper -> this.cast<Int>().toFloat()
      actualType.isLongPrimitiveOrWrapper -> this.cast<Long>().toFloat()
      actualType.isFloatPrimitiveOrWrapper -> this
      actualType.isDoublePrimitiveOrWrapper -> this.cast<Double>().toFloat()
      else -> this
    }
    expectedType.isDoublePrimitiveOrWrapper -> when {
      actualType.isBytePrimitiveOrWrapper -> this.cast<Byte>().toDouble()
      actualType.isShortPrimitiveOrWrapper -> this.cast<Short>().toDouble()
      actualType.isIntPrimitiveOrWrapper -> this.cast<Int>().toDouble()
      actualType.isLongPrimitiveOrWrapper -> this.cast<Long>().toDouble()
      actualType.isFloatPrimitiveOrWrapper -> this.cast<Float>().toDouble()
      actualType.isDoublePrimitiveOrWrapper -> this
      else -> this
    }
    else -> this
  }
}
