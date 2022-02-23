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

import com.meowool.cloak.case.Animal
import com.meowool.cloak.case.Cat
import com.meowool.cloak.case.Dog
import com.meowool.cloak.case.Grass
import com.meowool.cloak.case.Rabbit
import com.meowool.cloak.case.Zoo
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import org.junit.jupiter.api.Test

/**
 * Test for 'com/meowool/cloak/ImperativeConstructor.kt'.
 *
 * @author RinOrz
 */
class ImperativeConstructorTests {
  @Test fun `cannot create an instance of a class without any constructor matching`() {
    shouldThrowMessage("${Zoo::class.java.name}.constructor()") { Zoo::class.type.new() }
  }

  @Test fun `argument types mismatch`() {
    shouldThrow<IllegalArgumentException> {
      Zoo::class.type.new(0, parameters = arrayOf(String::class.type, Array<Animal>::class.type))
    }.message shouldMatch Regex(
      "The passed arguments \\[0, \\[Lcom.meowool.cloak.case.Animal;@.+] " +
        "does not match the parameter types \\[java.lang.String, \\[Lcom.meowool.cloak.case.Animal;]"
    )
  }

  @Test fun `correct constructor was created via implicit parameters`() {
    Zoo::class.type.new(true) shouldBe Zoo(true)
    Zoo::class.type.new(false) shouldBe Zoo(false)
    Zoo::class.type.new(0) shouldBe Zoo(0)
    Zoo::class.type.new(0, 10) shouldBe Zoo(id = 0, 10)
    Zoo::class.type.new(0, arrayOf(10, Integer.valueOf(20))) shouldBe Zoo(0, 10, 20)
    Zoo::class.type.new(0, Dog()) shouldBe Zoo(0, Dog())
    Zoo::class.type.new(Integer.valueOf(0), Dog()) shouldBe Zoo(0, Dog())
    Zoo::class.type.new("single") shouldBe Zoo("single")
    Zoo::class.type.new("name", Cat(), Dog()) shouldBe Zoo("name", Cat(), Dog())
    Zoo::class.type.new("name", Cat()) shouldBe Zoo("name", Cat())
    Zoo::class.type.new(Rabbit()) shouldBe Zoo(animal = Rabbit())
    Zoo::class.type.new(Grass()) shouldBe Zoo(Grass())
    Zoo::class.type.new(Cat()) shouldBe Zoo(Cat())
    Zoo::class.type.new(Dog()) shouldBe Zoo(Dog())

    // We cannot implicitly create this case,
    //   because the arguments are automatically unboxed during the transfer process,
    //   and there are `constructor(Int?, Int)` and `constructor(Int, Int)` in the case,
    //   so it will be ambiguous.
    Zoo::class.type.new(0, 10) shouldNotBe Zoo(nullableId = 0, 10)
    // Therefore, we can only explicitly declare its type
    Zoo::class.type.new(0.objectTyped, 10) shouldBe Zoo(nullableId = 0, 10)
  }
}
