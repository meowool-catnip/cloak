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

package com.meowool.cloak.internal

import com.meowool.cloak.BOOLEAN_OBJECT
import com.meowool.cloak.BYTE_OBJECT
import com.meowool.cloak.CHAR_OBJECT
import com.meowool.cloak.DOUBLE_OBJECT
import com.meowool.cloak.FLOAT_OBJECT
import com.meowool.cloak.INT_OBJECT
import com.meowool.cloak.LONG_OBJECT
import com.meowool.cloak.SHORT_OBJECT
import com.meowool.cloak.canCastTo
import com.meowool.cloak.internal.MatchingDistance.isMatched
import com.meowool.cloak.internal.MatchingDistance.isMismatch
import com.meowool.cloak.isObject
import com.meowool.cloak.isPrimitiveNumber
import kotlin.Boolean
import java.lang.Boolean as BooleanObject
import java.lang.Byte as ByteObject
import java.lang.Character as CharObject
import java.lang.Double as DoubleObject
import java.lang.Float as FloatObject
import java.lang.Integer as IntObject
import java.lang.Long as LongObject
import java.lang.Short as ShortObject

/**
 * @author RinOrz
 */
internal object MatchingDistance {
  const val Min = 0
  const val AnyMatched = 30
  const val KindMatched = 10
  const val NumberMatched = 18
  const val PrimitiveKindMatched = 7
  const val Mismatch = -1

  inline val Int.isMismatch get() = this < Min
  inline val Int.isMatched get() = this >= Min
}

/**
 * Calculates the distance between the [passed] class and the [declared] class.
 *
 * @return The better the match between classes, the smaller the result distance.
 * @author RinOrz
 */
internal fun calculateDistanceBetween(
  passed: Class<*>?,
  declared: Class<*>,
  depth: Boolean
): Int {
  if (passed == null) {
    return if (declared.isObject) MatchingDistance.AnyMatched else MatchingDistance.Mismatch
  }

  if (passed == declared) return MatchingDistance.Min

  if (passed.isArray && declared.isArray) return calculateDistanceBetween(
    passed.componentType, declared.componentType, depth
  )

  if ( // (int) Integer.valueOf(1)
    (passed == INT_OBJECT && declared == IntObject.TYPE) ||
    // (Integer) 1
    (passed == IntObject.TYPE && declared == INT_OBJECT) ||

    // (double) Double.valueOf(1.0)
    (passed == DOUBLE_OBJECT && declared == DoubleObject.TYPE) ||
    // (Double) 1.0
    (passed == DoubleObject.TYPE && declared == DOUBLE_OBJECT) ||

    // (float) Float.valueOf(1.0f)
    (passed == FLOAT_OBJECT && declared == FloatObject.TYPE) ||
    // (Float) 1.0f
    (passed == FloatObject.TYPE && declared == FLOAT_OBJECT) ||

    // (long) Long.valueOf(1L)
    (passed == LONG_OBJECT && declared == LongObject.TYPE) ||
    // (Long) 1L
    (passed == LongObject.TYPE && declared == LONG_OBJECT) ||

    // (short) Short.valueOf((short) 1)
    (passed == SHORT_OBJECT && declared == ShortObject.TYPE) ||
    // (Short) (short) 1
    (passed == ShortObject.TYPE && declared == SHORT_OBJECT) ||

    // (byte) Byte.valueOf((byte) 1)
    (passed == BYTE_OBJECT && declared == ByteObject.TYPE) ||
    // (Byte) ((byte) 1)
    (passed == ByteObject.TYPE && declared == BYTE_OBJECT) ||

    // (boolean) Boolean.valueOf(true)
    (passed == BOOLEAN_OBJECT && declared == BooleanObject.TYPE) ||
    // (Boolean) true
    (passed == BooleanObject.TYPE && declared == BOOLEAN_OBJECT) ||

    // (char) Character.valueOf('a')
    (passed == CHAR_OBJECT && declared == CharObject.TYPE) ||
    // (Character) 'a'
    (passed == CharObject.TYPE && declared == CHAR_OBJECT)
  ) return MatchingDistance.PrimitiveKindMatched

  if (passed.isPrimitiveNumber && declared.isPrimitiveNumber) return MatchingDistance.NumberMatched

  if (declared.isAssignableFrom(passed)) {
    if (!depth) return MatchingDistance.KindMatched

    var distance = MatchingDistance.Min
    var calculating = passed
    // Depth calculating: the deeper the depth of the superclass, the farther the distance.
    //   algorithm reference from commons-lang3:
    //   https://github.com/apache/commons-lang/blob/master/src/main/java/org/apache/commons/lang3/reflect/MemberUtils.java#L218-L237
    while (calculating != null && calculating != declared) {
      if (declared.isInterface && calculating canCastTo declared) {
        // slight penalty for interface match.
        // we still want an exact match to override an interface match,
        // but
        // an interface match should override anything where we have to
        // get a superclass.
        distance += 12
        break
      }
      calculating = calculating.superclass
      distance += 10
    }

    // If the calculating class is `null`, we've traveled all the way up to
    // an Object match. We'll penalize this by adding 15 to the distance.
    if (calculating == null) distance += 15

    return distance
  }

  return MatchingDistance.Mismatch
}

/**
 * Calculates the distance between the [passed] parameter types and the [declared] parameter types.
 *
 * @return The better the match between classes, the smaller the result distance.
 * @author RinOrz
 */
internal fun calculateDistanceBetween(
  passed: Array<out Class<*>?>,
  declared: Array<Class<*>>,
  hasVararg: Boolean,
  depth: Boolean = true
): Int {
  // If the method has 'vararg' parameters, we should consider the following cases:
  //    declared:            String, Boolean, vararg Int
  //  - explicit array case: | ""    | true   | arrayOf(1, 2)
  //  - flattened case:      | ""    | true   | 1, 2
  //  - no vararg case:      | ""    | true   |
  if (when (hasVararg) {
    false -> declared.size != passed.size
    true -> passed.size < declared.lastIndex
  }
  ) return MatchingDistance.Mismatch

  var totalDistance = 0
  // Directly check whether all parameter lists match
  if (declared.size == passed.size && declared.allIndexed { index, actualType ->
    calculateDistanceBetween(passed[index], actualType, depth).also { totalDistance += it }.isMatched
  }
  ) return totalDistance else totalDistance = 0

  if (hasVararg) {
    // In Java, the vararg parameter is always at the end of the method parameter list
    val varargIndex = declared.lastIndex

    // First, check whether all parameters except the last one match the actual parameter list
    for (index in 0 until varargIndex) {
      totalDistance += calculateDistanceBetween(passed[index], declared[index], depth).also {
        if (it.isMismatch) return MatchingDistance.Mismatch
      }
    }

    // Then, when no varargs passed, we need to add some distance
    if (passed.size == declared.lastIndex) return totalDistance + 3

    // Finally, we take out the type of the last vararg (array) parameter,
    val varargType = declared.last().componentType
    // and check whether all parameter types starting from vararg index can become vararg parameters
    for (index in varargIndex until passed.size) {
      totalDistance += calculateDistanceBetween(passed[index], varargType, depth).also {
        if (it.isMismatch) return MatchingDistance.Mismatch
      } + 1 // slight penalty for vararg match
    }

    return totalDistance
  }

  return MatchingDistance.Mismatch
}
