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
package com.meowool.cloak

import io.kotest.assertions.withClue
import io.kotest.inspectors.forAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.Test
import java.util.AbstractList
import java.util.AbstractMap

/**
 * Test for 'com/meowool/cloak/JavaClassUtilsTests.kt'.
 *
 * @author 凛 (RinOrz)
 */
class JavaClassUtilsTests {
  private val primitiveTypes = arrayOfPrimitives()

  @Test fun `class is object`() {
    fun Class<*>.shouldBeObject() = withClue("Expected ${this.name} is an object class") {
      this.isObject.shouldBeTrue()
    }

    fun Class<*>.shouldNotBeObject() = withClue("Expected ${this.name} is not an object class") {
      this.isObject.shouldBeFalse()
    }

    Int::class.javaObjectType.shouldBeObject()
    Int::class.javaPrimitiveType!!.shouldNotBeObject()
    Integer::class.java.shouldBeObject()
    Integer.TYPE.shouldNotBeObject()
    Array<Int>::class.java.shouldBeObject()
    IntArray::class.java.shouldNotBeObject()

    Byte::class.javaObjectType.shouldBeObject()
    Byte::class.javaPrimitiveType!!.shouldNotBeObject()
    java.lang.Byte::class.java.shouldBeObject()
    java.lang.Byte.TYPE.shouldNotBeObject()
    Array<Byte>::class.java.shouldBeObject()
    ByteArray::class.java.shouldNotBeObject()

    Short::class.javaObjectType.shouldBeObject()
    Short::class.javaPrimitiveType!!.shouldNotBeObject()
    java.lang.Short::class.java.shouldBeObject()
    java.lang.Short.TYPE.shouldNotBeObject()
    Array<Short>::class.java.shouldBeObject()
    ShortArray::class.java.shouldNotBeObject()

    Long::class.javaObjectType.shouldBeObject()
    Long::class.javaPrimitiveType!!.shouldNotBeObject()
    java.lang.Long::class.java.shouldBeObject()
    java.lang.Long.TYPE.shouldNotBeObject()
    Array<Long>::class.java.shouldBeObject()
    LongArray::class.java.shouldNotBeObject()

    Float::class.javaObjectType.shouldBeObject()
    Float::class.javaPrimitiveType!!.shouldNotBeObject()
    java.lang.Float::class.java.shouldBeObject()
    java.lang.Float.TYPE.shouldNotBeObject()
    Array<Float>::class.java.shouldBeObject()
    FloatArray::class.java.shouldNotBeObject()

    Double::class.javaObjectType.shouldBeObject()
    Double::class.javaPrimitiveType!!.shouldNotBeObject()
    java.lang.Double::class.java.shouldBeObject()
    java.lang.Double.TYPE.shouldNotBeObject()
    Array<Double>::class.java.shouldBeObject()
    DoubleArray::class.java.shouldNotBeObject()

    Boolean::class.javaObjectType.shouldBeObject()
    Boolean::class.javaPrimitiveType!!.shouldNotBeObject()
    java.lang.Boolean::class.java.shouldBeObject()
    java.lang.Boolean.TYPE.shouldNotBeObject()
    Array<Boolean>::class.java.shouldBeObject()
    BooleanArray::class.java.shouldNotBeObject()

    Char::class.javaObjectType.shouldBeObject()
    Char::class.javaPrimitiveType!!.shouldNotBeObject()
    Character::class.java.shouldBeObject()
    Character.TYPE.shouldNotBeObject()
    Array<Char>::class.java.shouldBeObject()
    CharArray::class.java.shouldNotBeObject()

    Void::class.javaObjectType.shouldBeObject()
    Void.TYPE.shouldNotBeObject()
    Array<Void>::class.java.shouldBeObject()
  }

  @Test fun `class is primitive wrapper`() {
    fun Class<*>.shouldBePrimitiveWrapper() = withClue("Expected ${this.name} is a primitive wrapper class") {
      this.isPrimitiveWrapper.shouldBeTrue()
    }

    fun Class<*>.shouldNotBePrimitiveWrapper() = withClue("Expected ${this.name} is not a primitive wrapper class") {
      this.isPrimitiveWrapper.shouldBeFalse()
    }

    Int::class.javaObjectType.shouldBePrimitiveWrapper()
    Int::class.javaPrimitiveType!!.shouldNotBePrimitiveWrapper()
    Integer::class.java.shouldBePrimitiveWrapper()
    Integer.TYPE.shouldNotBePrimitiveWrapper()
    Array<Int>::class.java.shouldBePrimitiveWrapper()
    IntArray::class.java.shouldNotBePrimitiveWrapper()

    Byte::class.javaObjectType.shouldBePrimitiveWrapper()
    Byte::class.javaPrimitiveType!!.shouldNotBePrimitiveWrapper()
    java.lang.Byte::class.java.shouldBePrimitiveWrapper()
    java.lang.Byte.TYPE.shouldNotBePrimitiveWrapper()
    Array<Byte>::class.java.shouldBePrimitiveWrapper()
    ByteArray::class.java.shouldNotBePrimitiveWrapper()

    Short::class.javaObjectType.shouldBePrimitiveWrapper()
    Short::class.javaPrimitiveType!!.shouldNotBePrimitiveWrapper()
    java.lang.Short::class.java.shouldBePrimitiveWrapper()
    java.lang.Short.TYPE.shouldNotBePrimitiveWrapper()
    Array<Short>::class.java.shouldBePrimitiveWrapper()
    ShortArray::class.java.shouldNotBePrimitiveWrapper()

    Long::class.javaObjectType.shouldBePrimitiveWrapper()
    Long::class.javaPrimitiveType!!.shouldNotBePrimitiveWrapper()
    java.lang.Long::class.java.shouldBePrimitiveWrapper()
    java.lang.Long.TYPE.shouldNotBePrimitiveWrapper()
    Array<Long>::class.java.shouldBePrimitiveWrapper()
    LongArray::class.java.shouldNotBePrimitiveWrapper()

    Float::class.javaObjectType.shouldBePrimitiveWrapper()
    Float::class.javaPrimitiveType!!.shouldNotBePrimitiveWrapper()
    java.lang.Float::class.java.shouldBePrimitiveWrapper()
    java.lang.Float.TYPE.shouldNotBePrimitiveWrapper()
    Array<Float>::class.java.shouldBePrimitiveWrapper()
    FloatArray::class.java.shouldNotBePrimitiveWrapper()

    Double::class.javaObjectType.shouldBePrimitiveWrapper()
    Double::class.javaPrimitiveType!!.shouldNotBePrimitiveWrapper()
    java.lang.Double::class.java.shouldBePrimitiveWrapper()
    java.lang.Double.TYPE.shouldNotBePrimitiveWrapper()
    Array<Double>::class.java.shouldBePrimitiveWrapper()
    DoubleArray::class.java.shouldNotBePrimitiveWrapper()

    Boolean::class.javaObjectType.shouldBePrimitiveWrapper()
    Boolean::class.javaPrimitiveType!!.shouldNotBePrimitiveWrapper()
    java.lang.Boolean::class.java.shouldBePrimitiveWrapper()
    java.lang.Boolean.TYPE.shouldNotBePrimitiveWrapper()
    Array<Boolean>::class.java.shouldBePrimitiveWrapper()
    BooleanArray::class.java.shouldNotBePrimitiveWrapper()

    Char::class.javaObjectType.shouldBePrimitiveWrapper()
    Char::class.javaPrimitiveType!!.shouldNotBePrimitiveWrapper()
    Character::class.java.shouldBePrimitiveWrapper()
    Character.TYPE.shouldNotBePrimitiveWrapper()
    Array<Char>::class.java.shouldBePrimitiveWrapper()
    CharArray::class.java.shouldNotBePrimitiveWrapper()

    Void::class.javaObjectType.shouldBePrimitiveWrapper()
    Void.TYPE.shouldNotBePrimitiveWrapper()
    Array<Void>::class.java.shouldBePrimitiveWrapper()

    Any::class.java.shouldNotBePrimitiveWrapper()
    List::class.java.shouldNotBePrimitiveWrapper()
    String::class.java.shouldNotBePrimitiveWrapper()
  }

  @Test fun `class is primitive or primitive wrapper`() {
    fun Class<*>.shouldBePrimitiveOrWrapper() =
      withClue("Expected ${this.name} is a primitive or primitive wrapper class") {
        this.isPrimitiveOrWrapper.shouldBeTrue()
      }

    fun Class<*>.shouldNotBePrimitiveOrWrapper() =
      withClue("Expected ${this.name} is not a primitive or primitive wrapper class") {
        this.isPrimitiveOrWrapper.shouldBeFalse()
      }

    Int::class.javaObjectType.shouldBePrimitiveOrWrapper()
    Int::class.javaPrimitiveType!!.shouldBePrimitiveOrWrapper()
    Integer::class.java.shouldBePrimitiveOrWrapper()
    Integer.TYPE.shouldBePrimitiveOrWrapper()
    Array<Int>::class.java.shouldBePrimitiveOrWrapper()
    IntArray::class.java.shouldBePrimitiveOrWrapper()

    Byte::class.javaObjectType.shouldBePrimitiveOrWrapper()
    Byte::class.javaPrimitiveType!!.shouldBePrimitiveOrWrapper()
    java.lang.Byte::class.java.shouldBePrimitiveOrWrapper()
    java.lang.Byte.TYPE.shouldBePrimitiveOrWrapper()
    Array<Byte>::class.java.shouldBePrimitiveOrWrapper()
    ByteArray::class.java.shouldBePrimitiveOrWrapper()

    Short::class.javaObjectType.shouldBePrimitiveOrWrapper()
    Short::class.javaPrimitiveType!!.shouldBePrimitiveOrWrapper()
    java.lang.Short::class.java.shouldBePrimitiveOrWrapper()
    java.lang.Short.TYPE.shouldBePrimitiveOrWrapper()
    Array<Short>::class.java.shouldBePrimitiveOrWrapper()
    ShortArray::class.java.shouldBePrimitiveOrWrapper()

    Long::class.javaObjectType.shouldBePrimitiveOrWrapper()
    Long::class.javaPrimitiveType!!.shouldBePrimitiveOrWrapper()
    java.lang.Long::class.java.shouldBePrimitiveOrWrapper()
    java.lang.Long.TYPE.shouldBePrimitiveOrWrapper()
    Array<Long>::class.java.shouldBePrimitiveOrWrapper()
    LongArray::class.java.shouldBePrimitiveOrWrapper()

    Float::class.javaObjectType.shouldBePrimitiveOrWrapper()
    Float::class.javaPrimitiveType!!.shouldBePrimitiveOrWrapper()
    java.lang.Float::class.java.shouldBePrimitiveOrWrapper()
    java.lang.Float.TYPE.shouldBePrimitiveOrWrapper()
    Array<Float>::class.java.shouldBePrimitiveOrWrapper()
    FloatArray::class.java.shouldBePrimitiveOrWrapper()

    Double::class.javaObjectType.shouldBePrimitiveOrWrapper()
    Double::class.javaPrimitiveType!!.shouldBePrimitiveOrWrapper()
    java.lang.Double::class.java.shouldBePrimitiveOrWrapper()
    java.lang.Double.TYPE.shouldBePrimitiveOrWrapper()
    Array<Double>::class.java.shouldBePrimitiveOrWrapper()
    DoubleArray::class.java.shouldBePrimitiveOrWrapper()

    Boolean::class.javaObjectType.shouldBePrimitiveOrWrapper()
    Boolean::class.javaPrimitiveType!!.shouldBePrimitiveOrWrapper()
    java.lang.Boolean::class.java.shouldBePrimitiveOrWrapper()
    java.lang.Boolean.TYPE.shouldBePrimitiveOrWrapper()
    Array<Boolean>::class.java.shouldBePrimitiveOrWrapper()
    BooleanArray::class.java.shouldBePrimitiveOrWrapper()

    Char::class.javaObjectType.shouldBePrimitiveOrWrapper()
    Char::class.javaPrimitiveType!!.shouldBePrimitiveOrWrapper()
    Character::class.java.shouldBePrimitiveOrWrapper()
    Character.TYPE.shouldBePrimitiveOrWrapper()
    Array<Char>::class.java.shouldBePrimitiveOrWrapper()
    CharArray::class.java.shouldBePrimitiveOrWrapper()

    Void::class.javaObjectType.shouldBePrimitiveOrWrapper()
    Void.TYPE.shouldBePrimitiveOrWrapper()
    Array<Void>::class.java.shouldBePrimitiveOrWrapper()

    Any::class.java.shouldNotBePrimitiveOrWrapper()
    List::class.java.shouldNotBePrimitiveOrWrapper()
    String::class.java.shouldNotBePrimitiveOrWrapper()
  }

  @Test fun `class is primitive number`() {
    fun Class<*>.shouldBePrimitiveNumber() = withClue("Expected ${this.name} is a primitive number class") {
      this.isPrimitiveNumber.shouldBeTrue()
      this.isNumber.shouldBeTrue()
    }

    fun Class<*>.shouldNotBePrimitiveNumber() = withClue("Expected ${this.name} is not a primitive number class") {
      this.isPrimitiveNumber.shouldBeFalse()
    }

    Int::class.java.shouldBePrimitiveNumber()
    Int::class.javaPrimitiveType!!.shouldBePrimitiveNumber()
    Int::class.javaObjectType.shouldNotBePrimitiveNumber()
    Integer::class.java.shouldNotBePrimitiveNumber()
    Integer.TYPE.shouldBePrimitiveNumber()
    Array<Int>::class.java.shouldNotBePrimitiveNumber()
    IntArray::class.java.shouldNotBePrimitiveNumber()

    Byte::class.java.shouldBePrimitiveNumber()
    Byte::class.javaPrimitiveType!!.shouldBePrimitiveNumber()
    Byte::class.javaObjectType.shouldNotBePrimitiveNumber()
    java.lang.Byte::class.java.shouldNotBePrimitiveNumber()
    java.lang.Byte.TYPE.shouldBePrimitiveNumber()
    Array<Byte>::class.java.shouldNotBePrimitiveNumber()
    ByteArray::class.java.shouldNotBePrimitiveNumber()

    Short::class.java.shouldBePrimitiveNumber()
    Short::class.javaPrimitiveType!!.shouldBePrimitiveNumber()
    Short::class.javaObjectType.shouldNotBePrimitiveNumber()
    java.lang.Short::class.java.shouldNotBePrimitiveNumber()
    java.lang.Short.TYPE.shouldBePrimitiveNumber()
    Array<Short>::class.java.shouldNotBePrimitiveNumber()
    ShortArray::class.java.shouldNotBePrimitiveNumber()

    Long::class.java.shouldBePrimitiveNumber()
    Long::class.javaPrimitiveType!!.shouldBePrimitiveNumber()
    Long::class.javaObjectType.shouldNotBePrimitiveNumber()
    java.lang.Long::class.java.shouldNotBePrimitiveNumber()
    java.lang.Long.TYPE.shouldBePrimitiveNumber()
    Array<Long>::class.java.shouldNotBePrimitiveNumber()
    LongArray::class.java.shouldNotBePrimitiveNumber()

    Float::class.java.shouldBePrimitiveNumber()
    Float::class.javaPrimitiveType!!.shouldBePrimitiveNumber()
    Float::class.javaObjectType.shouldNotBePrimitiveNumber()
    java.lang.Float::class.java.shouldNotBePrimitiveNumber()
    java.lang.Float.TYPE.shouldBePrimitiveNumber()
    Array<Float>::class.java.shouldNotBePrimitiveNumber()
    FloatArray::class.java.shouldNotBePrimitiveNumber()

    Double::class.java.shouldBePrimitiveNumber()
    Double::class.javaPrimitiveType!!.shouldBePrimitiveNumber()
    Double::class.javaObjectType.shouldNotBePrimitiveNumber()
    java.lang.Double::class.java.shouldNotBePrimitiveNumber()
    java.lang.Double.TYPE.shouldBePrimitiveNumber()
    Array<Double>::class.java.shouldNotBePrimitiveNumber()
    DoubleArray::class.java.shouldNotBePrimitiveNumber()

    Boolean::class.java.shouldNotBePrimitiveNumber()
    Boolean::class.javaPrimitiveType!!.shouldNotBePrimitiveNumber()
    Boolean::class.javaObjectType.shouldNotBePrimitiveNumber()
    java.lang.Boolean::class.java.shouldNotBePrimitiveNumber()
    java.lang.Boolean.TYPE.shouldNotBePrimitiveNumber()
    Array<Boolean>::class.java.shouldNotBePrimitiveNumber()
    BooleanArray::class.java.shouldNotBePrimitiveNumber()

    Char::class.java.shouldNotBePrimitiveNumber()
    Char::class.javaPrimitiveType!!.shouldNotBePrimitiveNumber()
    Char::class.javaObjectType.shouldNotBePrimitiveNumber()
    Character::class.java.shouldNotBePrimitiveNumber()
    Character.TYPE.shouldNotBePrimitiveNumber()
    Array<Char>::class.java.shouldNotBePrimitiveNumber()
    CharArray::class.java.shouldNotBePrimitiveNumber()

    Void::class.javaObjectType.shouldNotBePrimitiveNumber()
    Void.TYPE.shouldNotBePrimitiveNumber()
    Array<Void>::class.java.shouldNotBePrimitiveNumber()

    Any::class.java.shouldNotBePrimitiveNumber()
    List::class.java.shouldNotBePrimitiveNumber()
    String::class.java.shouldNotBePrimitiveNumber()
  }

  @Test fun `class is boxed number`() {
    fun Class<*>.shouldBeBoxedNumber() = withClue("Expected ${this.name} is a boxed number class") {
      this.isBoxedNumber.shouldBeTrue()
      this.isNumber.shouldBeTrue()
    }

    fun Class<*>.shouldNotBeBoxedNumber() = withClue("Expected ${this.name} is not a boxed number class") {
      this.isBoxedNumber.shouldBeFalse()
    }

    Int::class.java.shouldNotBeBoxedNumber()
    Int::class.javaPrimitiveType!!.shouldNotBeBoxedNumber()
    Int::class.javaObjectType.shouldBeBoxedNumber()
    Integer::class.java.shouldBeBoxedNumber()
    Integer.TYPE.shouldNotBeBoxedNumber()
    Array<Int>::class.java.shouldNotBeBoxedNumber()
    IntArray::class.java.shouldNotBeBoxedNumber()

    Byte::class.java.shouldNotBeBoxedNumber()
    Byte::class.javaPrimitiveType!!.shouldNotBeBoxedNumber()
    Byte::class.javaObjectType.shouldBeBoxedNumber()
    java.lang.Byte::class.java.shouldBeBoxedNumber()
    java.lang.Byte.TYPE.shouldNotBeBoxedNumber()
    Array<Byte>::class.java.shouldNotBeBoxedNumber()
    ByteArray::class.java.shouldNotBeBoxedNumber()

    Short::class.java.shouldNotBeBoxedNumber()
    Short::class.javaPrimitiveType!!.shouldNotBeBoxedNumber()
    Short::class.javaObjectType.shouldBeBoxedNumber()
    java.lang.Short::class.java.shouldBeBoxedNumber()
    java.lang.Short.TYPE.shouldNotBeBoxedNumber()
    Array<Short>::class.java.shouldNotBeBoxedNumber()
    ShortArray::class.java.shouldNotBeBoxedNumber()

    Long::class.java.shouldNotBeBoxedNumber()
    Long::class.javaPrimitiveType!!.shouldNotBeBoxedNumber()
    Long::class.javaObjectType.shouldBeBoxedNumber()
    java.lang.Long::class.java.shouldBeBoxedNumber()
    java.lang.Long.TYPE.shouldNotBeBoxedNumber()
    Array<Long>::class.java.shouldNotBeBoxedNumber()
    LongArray::class.java.shouldNotBeBoxedNumber()

    Float::class.java.shouldNotBeBoxedNumber()
    Float::class.javaPrimitiveType!!.shouldNotBeBoxedNumber()
    Float::class.javaObjectType.shouldBeBoxedNumber()
    java.lang.Float::class.java.shouldBeBoxedNumber()
    java.lang.Float.TYPE.shouldNotBeBoxedNumber()
    Array<Float>::class.java.shouldNotBeBoxedNumber()
    FloatArray::class.java.shouldNotBeBoxedNumber()

    Double::class.java.shouldNotBeBoxedNumber()
    Double::class.javaPrimitiveType!!.shouldNotBeBoxedNumber()
    Double::class.javaObjectType.shouldBeBoxedNumber()
    java.lang.Double::class.java.shouldBeBoxedNumber()
    java.lang.Double.TYPE.shouldNotBeBoxedNumber()
    Array<Double>::class.java.shouldNotBeBoxedNumber()
    DoubleArray::class.java.shouldNotBeBoxedNumber()

    Boolean::class.java.shouldNotBeBoxedNumber()
    Boolean::class.javaPrimitiveType!!.shouldNotBeBoxedNumber()
    Boolean::class.javaObjectType.shouldNotBeBoxedNumber()
    java.lang.Boolean::class.java.shouldNotBeBoxedNumber()
    java.lang.Boolean.TYPE.shouldNotBeBoxedNumber()
    Array<Boolean>::class.java.shouldNotBeBoxedNumber()
    BooleanArray::class.java.shouldNotBeBoxedNumber()

    Char::class.java.shouldNotBeBoxedNumber()
    Char::class.javaPrimitiveType!!.shouldNotBeBoxedNumber()
    Char::class.javaObjectType.shouldNotBeBoxedNumber()
    Character::class.java.shouldNotBeBoxedNumber()
    Character.TYPE.shouldNotBeBoxedNumber()
    Array<Char>::class.java.shouldNotBeBoxedNumber()
    CharArray::class.java.shouldNotBeBoxedNumber()

    Void::class.javaObjectType.shouldNotBeBoxedNumber()
    Void.TYPE.shouldNotBeBoxedNumber()
    Array<Void>::class.java.shouldNotBeBoxedNumber()

    Any::class.java.shouldNotBeBoxedNumber()
    List::class.java.shouldNotBeBoxedNumber()
    String::class.java.shouldNotBeBoxedNumber()
  }

  @Test fun `whether instance can cast to specified class`() {
    infix fun Any?.shouldCanCastTo(base: Class<*>) = withClue("Expected `$this` can cast to ${base.name} class.") {
      (this canCastTo base).shouldBeTrue()
    }

    infix fun Any?.shouldCannotCastTo(base: Class<*>) =
      withClue("Expected `$this` can not cast to ${base.name} class.") {
        (this canCastTo base).shouldBeFalse()
      }

    fun Number.test() {
      shouldCanCastTo(javaClass)
      shouldCanCastTo(javaClass.boxing())
      shouldCanCastTo(javaClass.unboxing())
      shouldCannotCastTo(Void.TYPE)
      shouldCannotCastTo(Void::class.java)

      if (javaClass.isPrimitive) primitiveTypes.forAll {
        if (it != javaClass) {
          shouldCanCastTo(it.unboxing())
          shouldCannotCastTo(it.boxing())
        }
      }
    }

    10.toByte().test()
    10.toShort().test()
    10.test()
    10L.test()
    10F.test()
    10.0.test()

    primitiveTypes.forAll {
      // `null` value can be any object
      null.shouldCanCastTo(it.boxing())
      // but `null` value never be primitive type
      null.shouldCannotCastTo(it.unboxing())
    }

    arrayListOf(1, 2, 3).apply {
      shouldCanCastTo(ArrayList::class.java)
      shouldCanCastTo(AbstractList::class.java)
      shouldCanCastTo(List::class.java)
      shouldCanCastTo(Any::class.java)
    }

    hashMapOf("a" to 1, "b" to 2).apply {
      shouldCanCastTo(AbstractMap::class.java)
      shouldCanCastTo(Map::class.java)
      shouldCanCastTo(Any::class.java)
    }

    StringBuilder::class.java.shouldCanCastTo(CharSequence::class.java)
    CharSequence::class.java.shouldCannotCastTo(StringBuilder::class.java)

    Appendable::class.java.shouldCanCastTo(Any::class.java)
    Any::class.java.shouldCannotCastTo(Appendable::class.java)

    Any::class.java.shouldCanCastTo(Any::class.java)

    Array<Int>::class.java.shouldCanCastTo(IntArray::class.java)
    FloatArray::class.java.shouldCanCastTo(Array<Float>::class.java)
  }

  @Test fun `class boxing and unboxing`() {
    arrayOfPrimitiveWrappers(true).map { it.boxing() } shouldContainExactlyInAnyOrder arrayOfPrimitiveWrappers(true).toList()
    arrayOfPrimitiveWrappers(true).map { it.unboxing() } shouldContainExactlyInAnyOrder arrayOfPrimitives(true).toList()

    arrayOfPrimitives(true).map { it.boxing() } shouldContainExactlyInAnyOrder arrayOfPrimitiveWrappers(true).toList()
    arrayOfPrimitives(true).map { it.unboxing() } shouldContainExactlyInAnyOrder arrayOfPrimitives(true).toList()
  }
}
