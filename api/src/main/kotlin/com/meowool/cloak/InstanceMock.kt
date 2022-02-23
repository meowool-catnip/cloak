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
@file:Suppress("UNUSED_PARAMETER", "MemberVisibilityCanBePrivate", "NonAsciiCharacters", "SpellCheckingInspection")

package com.meowool.cloak

/**
 * Used to mock a class that inherited from [T] that exist in the run time but cannot be accessed directly.
 *
 * In fact, the behavior of this interface is similar to that of an ordinary boxing class, but it allows you to
 * implicitly write Cloak reflection code to make the behavior simpler and look less "reflect". In short, mock is not a
 * real class, so if you implicitly call the Cloak reflection API in mock class, it will be replaced with mocked actual
 * [type] and instance at compile time.
 * ---
 *
 * For example, the following is an inaccessible class:
 * ```
 * package com.example;
 * private class DataGenerator {
 *   int identifier;
 *
 *   DataGenerator(boolean isLazy) {}
 *
 *   static String generate(DataGenerator generator, Object input) {}
 * }
 * ```
 * If we want to wrap the 'DataGenerator' class so that it can be reflected at any time, we usually like this:
 * ```
 * class DataGeneratorWrapper(val instance: Any) {
 *   val identifier: Int by instance.field()
 *
 *   companion object {
 *     private val Type = "com.example.DataGenerator".type
 *
 *     operator fun invoke(isLazy: Boolean): Any = Type.constructorNew()
 *
 *     fun generate(generator: Any, input: Any): String = Type.staticMethodCall(
 *       params = arrayOf(Type, Any::class.type),
 *     )
 *   }
 * }
 *
 * fun main() {
 *   val generator = DataGeneratorWrapper()
 *   val result = DataGeneratorWrapper.generate(generator.instance, generator.identifier)
 *   println("Generated: $result")
 * }
 * ```
 * But this way is not so good, because it violates the principle of strong typing design. When we call the 'generate'
 * function, we may forget what type the first parameter is, so we prefer it to be type-safe and write less code.
 * So if we use [InstanceMock], we can get the following code:
 * ```
 * class DataGeneratorMock : InstanceMock {
 *   override val type = "com.example.DataGenerator".type
 *
 *   val identifier: Int by field()
 *
 *   companion object {
 *     operator fun invoke(isLazy: Boolean): DataGeneratorMock = constructorNew()
 *     fun generate(generator: DataGeneratorMock, input: Any): String = staticMethodCall()
 *   }
 * }
 *
 * fun main() {
 *   val generator = DataGeneratorMock()
 *   val result = DataGeneratorMock.generate(generator, generator.identifier)
 *   println("Generated: $result")
 * }
 * ```
 * As you can see, we can directly regard 'DataGeneratorMock' as an accessible 'DataGenerator', which internally holds
 * a real instance, so in fact, all reflections are based on the real instance.
 *
 * @param T Accessible superclass of the actual type to mock
 * @author RinOrz
 * @see InstanceMoсk
 */
interface InstanceMock<T : Any> {

  /**
   * The actual type of the class to mock.
   */
  val actualType: Type<T>

  /**
   * The actual instance of the class to mock.
   */
  val actual: T

  /**
   * Returns `true` if the mocked actual instance is equal to the 'other's actual instance.
   */
  /** override fun equals(other: Any?): Boolean = actual == other */

  /**
   * Returns the hash code of the mocked actual instance.
   */
  /** override fun hashCode(): Int = actual.hashCode() */

  /**
   * A synthetic companion interface.
   *
   * All classes that inherit [InstanceMock] will automatically create a companion object and implement this interface,
   * therefore, this interface should not be implemented manually.
   *
   * @author RinOrz
   */
  interface Synthetic<T : Any> {

    /**
     * The actual type of the host mock class.
     *
     * @see InstanceMock.actualType
     */
    val actualType: Type<T>
  }
}

/**
 * Used to mock a class that exists in the run time but cannot be accessed directly.
 *
 * @author RinOrz
 * @see InstanceMock<T: Any>
 */
typealias InstanceMoсk = InstanceMock<Any>
