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
@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package com.meowool.cloak

import com.meowool.cloak.case.Animal
import com.meowool.cloak.case.Cat
import com.meowool.cloak.case.Dog
import com.meowool.cloak.case.FieldsContainer
import com.meowool.cloak.case.FieldsParent
import com.meowool.cloak.case.Grass
import com.meowool.cloak.case.Organism
import com.meowool.cloak.case.Rabbit
import com.meowool.cloak.case.Zoo
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

/**
 * @author 凛 (RinOrz)
 */
class JavaMemberUtilsTests {
  @Test fun `match the best constructor`() {
    Zoo::class.java.matchBestConstructor().shouldBeNull()

    // case of primitive or wrapper of the same type: `true` or `java.lang.Boolean.valueOf(true)`
    //   result: `constructor(open: Boolean)`
    arrayOf(
      Zoo::class.java.matchBestConstructor(Boolean::class.javaPrimitiveType),
      Zoo::class.java.matchBestConstructor(Boolean::class.javaObjectType),
    ).forEach {
      it shouldBe Zoo.BOOLEAN
      it shouldNotBe Zoo.INT
    }

    // case of passed some primitive numbers: `10.0`, `10f`, etc...
    //   result: `constructor(id: Int)`
    arrayOf(
      Byte::class.java,
      Short::class.java,
      Int::class.java,
      Long::class.java,
      Float::class.java,
      Double::class.java,
    ).map { Zoo::class.java.matchBestConstructor(it) }.forAll {
      it shouldBe Zoo.INT
      it shouldNotBeIn arrayOf(
        Zoo.BOOLEAN,
        Zoo.INT_OBJ_VARARG_ANIMAL,
      )
    }

    // case of when there are primitive and wrapper of the same kind and one of them has varargs:
    //   no vararg passed: `Integer.valueOf(10)`
    //   result:           `constructor(id: Int? /* Integer */, vararg animals: Animal)`
    Zoo::class.java.matchBestConstructor(Int::class.javaObjectType) shouldBeIn arrayOf(
      Zoo.INT_OBJ_VARARG_ANIMAL,
      Zoo.INT_OBJ_VARARG_Int,
    )

    // case of when there are primitive and wrapper of the same kind and one of them has varargs:
    //   no vararg passed: `Integer.valueOf(10)`
    //   result:           `constructor(id: Int? /* Integer */, vararg animals: Animal)`
    Zoo::class.java.matchBestConstructor(CharSequence::class.javaObjectType) shouldNotBeIn arrayOf(
      Zoo.STRING,
      Zoo.STRING_VARARG_ANIMAL,
      Zoo.STRING_VARARG_CAT
    )

    // constructor(name: String, vararg cats: Cat)
    Zoo::class.java.matchBestConstructor(null, Cat::class.java) shouldBe Zoo.STRING_VARARG_CAT
    // constructor(cat: Cat)
    Zoo::class.java.matchBestConstructor(Cat::class.java) shouldBe Zoo.CAT
    // constructor(dog: Dog)
    Zoo::class.java.matchBestConstructor(Dog::class.java) shouldBe Zoo.DOG
    // constructor(organism: Organism)
    Zoo::class.java.matchBestConstructor(Grass::class.java) shouldBe Zoo.ORGANISM

    // closer interface has higher priority
    //   result: `constructor(animal: Animal)`
    Zoo::class.java.matchBestConstructor(Rabbit::class.java).also {
      it shouldBe Zoo.ANIMAL
      it shouldNotBe Zoo.ORGANISM
    }

    // case of multiple matches, determined by bytecode order
    Zoo::class.java.matchBestConstructor(null) shouldBe Zoo::class.java.declaredConstructors.first {
      it.parameterTypes.size == 1 && it.parameterTypes.first().isObject
    }
    // constructor(name: String, vararg animals: Animal)
    Zoo::class.java.matchBestConstructor(null, Animal::class.java) shouldBe Zoo::class.java.declaredConstructors.first {
      it.parameterTypes.size == 2 && it.parameterTypes.first().isObject && it.parameterTypes.last() == Array<Animal>::class.java
    }
    Zoo::class.java.matchBestConstructor(null, null) shouldBe Zoo::class.java.declaredConstructors.first {
      it.parameterTypes.size == 2 && it.parameterTypes.first().isObject && it.parameterTypes.last().isObject
    }
  }

  @Test fun `match the best field`() {
    // int
    FieldsContainer::class.java.matchBestField("intField", null).shouldNotBeNull().shouldBeIn(
      FieldsContainer::class.java.matchBestField("intField", Int::class.javaObjectType),
      FieldsContainer::class.java.matchBestField(null, Int::class.javaPrimitiveType),
      FieldsContainer::class.java.getDeclaredField("intField"),
    )

    // boolean
    FieldsContainer::class.java.matchBestField(null, Boolean::class.javaPrimitiveType).shouldNotBeNull().shouldBeIn(
      FieldsParent::class.java.matchBestField("booleanField", Boolean::class.javaPrimitiveType),
      FieldsContainer::class.java.getDeclaredField("booleanField"),
    )
    FieldsContainer::class.java.matchBestField("baseBooleanField", null).shouldNotBeNull().shouldBeIn(
      FieldsParent::class.java.matchBestField(null, Boolean::class.javaPrimitiveType),
      FieldsParent::class.java.getDeclaredField("baseBooleanField")
    )
    FieldsContainer::class.java.matchBestField(null, Boolean::class.javaObjectType).shouldNotBeNull().apply {
      // For performance reasons, if the class is found, the super class will not be traversed,
      shouldNotBe(FieldsParent::class.java.getDeclaredField("baseBooleanObjectField"))
      // therefore, the result is the earliest matched field that type is boolean
      shouldBe(FieldsContainer::class.java.getDeclaredField("booleanField"))
    }

    // string
    FieldsContainer::class.java.matchBestField(null, String::class.java).shouldNotBeNull().shouldBe(
      FieldsParent::class.java.getDeclaredField("baseStringField"),
    )

    // interface
    FieldsContainer::class.java.matchBestField(null, Organism::class.java).shouldNotBeNull().shouldBe(
      FieldsContainer::class.java.getDeclaredField("interfaceLowField")
    )
    FieldsContainer::class.java.matchBestField(null, Cat::class.java).shouldNotBeNull().shouldBeIn(
      FieldsContainer::class.java.matchBestField(null, Animal::class.java),
      FieldsContainer::class.java.getDeclaredField("interfaceField"),
    )

    // non
    FieldsContainer::class.java.matchBestField(null, Object::class.java).shouldBeNull()
  }
}
