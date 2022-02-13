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
 * @author 凛 (RinOrz)
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
    // ktlint-disable
    // @formatter:off
    Zoo::class.type.new(true)                                shouldBe Zoo(true)
    Zoo::class.type.new(false)                               shouldBe Zoo(false)
    Zoo::class.type.new(0)                                   shouldBe Zoo(0)
    Zoo::class.type.new(0, 10)                               shouldBe Zoo(id = 0, 10)
    Zoo::class.type.new(0, arrayOf(10, Integer.valueOf(20))) shouldBe Zoo(0, 10, 20)
    Zoo::class.type.new(0, Dog())                            shouldBe Zoo(0, Dog())
    Zoo::class.type.new(Integer.valueOf(0), Dog())           shouldBe Zoo(0, Dog())
    Zoo::class.type.new("single")                            shouldBe Zoo("single")
    Zoo::class.type.new("name", Cat(), Dog())                shouldBe Zoo("name", Cat(), Dog())
    Zoo::class.type.new("name", Cat())                       shouldBe Zoo("name", Cat())
    Zoo::class.type.new(Rabbit())                            shouldBe Zoo(animal = Rabbit())
    Zoo::class.type.new(Grass())                             shouldBe Zoo(Grass())
    Zoo::class.type.new(Cat())                               shouldBe Zoo(Cat())
    Zoo::class.type.new(Dog())                               shouldBe Zoo(Dog())

    // We cannot implicitly create this case,
    //   because the arguments are automatically unboxed during the transfer process,
    //   and there are `constructor(Int?, Int)` and `constructor(Int, Int)` in the case,
    //   so it will be ambiguous.
    Zoo::class.type.new(0, 10)                            shouldNotBe Zoo(nullableId = 0, 10)
    // Therefore, we can only explicitly declare its type
    Zoo::class.type.new(0.objectTyped, 10)                   shouldBe Zoo(nullableId = 0, 10)
  }
}
