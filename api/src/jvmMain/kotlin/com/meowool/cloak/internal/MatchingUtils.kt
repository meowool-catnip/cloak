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
import com.meowool.cloak.isObject
import com.meowool.cloak.isPrimitiveNumber
import kotlin.Boolean
import kotlin.Float
import java.lang.Boolean as BooleanObject
import java.lang.Byte as ByteObject
import java.lang.Character as CharObject
import java.lang.Double as DoubleObject
import java.lang.Float as FloatObject
import java.lang.Integer as IntObject
import java.lang.Long as LongObject
import java.lang.Short as ShortObject


internal const val MIN_WEIGHT = 0f
internal const val MATCH_NULL_WEIGHT = 1f
internal const val MATCH_KIND_WEIGHT = 0.3f
internal const val PRIMITIVE_WEIGHT = 0.5f
internal const val SAME_PRIMITIVE_KIND_WEIGHT = 0.2f
internal const val MISMATCH_WEIGHT = -1f

internal inline val Float.isMismatchWeight get() = this < MIN_WEIGHT
internal inline val Float.isMatchedWeight get() = this >= MIN_WEIGHT

/**
 * Calculates the weight of the [passed] class relative to the [declared] class.
 *
 * @return The lighter the weight, the better they match.
 * @author 凛 (RinOrz)
 */
internal fun calculateClassWeight(
  passed: Class<*>?,
  declared: Class<*>,
  depth: Boolean
): Float {
  if (passed == null) {
    return if (declared.isObject) MATCH_NULL_WEIGHT else MISMATCH_WEIGHT
  }

  if (passed == declared) return MIN_WEIGHT

  if (passed.isArray && declared.isArray) return calculateClassWeight(
    passed.componentType, declared.componentType, depth
  )

  if (// (int) Integer.valueOf(1)
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
  ) return SAME_PRIMITIVE_KIND_WEIGHT

  if (passed.isPrimitiveNumber && declared.isPrimitiveNumber) return PRIMITIVE_WEIGHT

  if (declared.isAssignableFrom(passed)) {
    if (!depth) return MATCH_KIND_WEIGHT

    var weight = MIN_WEIGHT
    var weighing = passed
    // Depth weighing: the deeper the depth of the superclass, the higher the weighing.
    //   algorithm reference from commons-lang3:
    //   https://github.com/apache/commons-lang/blob/master/src/main/java/org/apache/commons/lang3/reflect/MemberUtils.java#L218-L237
    while (weighing != null && weighing != declared) {
      if (declared.isInterface && weighing canCastTo declared) {
        // slight penalty for interface match.
        // we still want an exact match to override an interface match,
        // but
        // an interface match should override anything where we have to
        // get a superclass.
        weight += 1.25f
        break
      }
      weighing = weighing.superclass
      weight++
    }

    // If the weighting class is `null`, we've traveled all the way up to
    // an Object match. We'll penalize this by adding 1.5 to the weight.
    if (weighing == null) weight += 1.5f

    return weight
  }

  return MISMATCH_WEIGHT
}

/**
 * Calculates the weight of the [passed] parameter types relative to the [declared] parameter types.
 *
 * @return The lighter the weight, the better they match.
 * @author 凛 (RinOrz)
 */
internal fun calculateParametersWeight(
  passed: Array<out Class<*>?>,
  declared: Array<Class<*>>,
  hasVararg: Boolean,
  depth: Boolean = true
): Float {
  // If the method has 'vararg' parameters, we should consider the following cases:
  //    declared:            String, Boolean, vararg Int
  //  - explicit array case: | ""    | true   | arrayOf(1, 2)
  //  - flattened case:      | ""    | true   | 1, 2
  //  - no vararg case:      | ""    | true   |
  if (when (hasVararg) {
      false -> declared.size != passed.size
      true -> passed.size < declared.lastIndex
    }
  ) return MISMATCH_WEIGHT

  var totalWeight = 0f
  // Directly check whether all parameter lists match
  if (declared.size == passed.size && declared.allIndexed { index, actualType ->
      calculateClassWeight(passed[index], actualType, depth).also { totalWeight += it }.isMatchedWeight
    }
  ) return totalWeight else totalWeight = 0f

  if (hasVararg) {
    // In Java, the vararg parameter is always at the end of the method parameter list
    val varargIndex = declared.lastIndex

    // First, check whether all parameters except the last one match the actual parameter list
    for (index in 0 until varargIndex) {
      totalWeight += calculateClassWeight(passed[index], declared[index], depth).also {
        if (it.isMismatchWeight) return MISMATCH_WEIGHT
      }
    }

    // Then, when no varargs passed, we need to add some weight
    if (passed.size == declared.lastIndex) return totalWeight + 0.01f

    // Finally, we take out the type of the last vararg (array) parameter,
    val varargType = declared.last().componentType
    // and check whether all parameter types starting from vararg index can become vararg parameters
    for (index in varargIndex until passed.size) {
      totalWeight += calculateClassWeight(passed[index], varargType, depth).also {
        if (it.isMismatchWeight) return MISMATCH_WEIGHT
      } + 0.001f // slight penalty for vararg match
    }

    return totalWeight
  }

  return MISMATCH_WEIGHT
}