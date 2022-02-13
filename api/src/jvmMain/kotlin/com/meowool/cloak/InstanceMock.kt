@file:Suppress("UNUSED_PARAMETER", "MemberVisibilityCanBePrivate", "NonAsciiCharacters", "SpellCheckingInspection")

package com.meowool.cloak

/**
 * Used to mock a class that inherited from [T] that exist in the runtime but cannot be accessed directly.
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
 *   val identifier: Int by instance.property()
 *
 *   companion object {
 *     private val Type = "com.example.DataGenerator".type
 *
 *     operator fun invoke(isLazy: Boolean): Any = Type.delegateNew()
 *
 *     fun generate(generator: Any, input: Any): String = Type.delegateStaticCall(
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
 * But this way is not very good, because it violates the principle of strong typing design. When we call the 'generate'
 * function, we may forget what type the first parameter is, so we prefer it to be type-safe and write less code.
 * So if we use [InstanceMock], we can get the following code:
 * ```
 * class DataGeneratorMock : InstanceMock {
 *   override val type = "com.example.DataGenerator".type
 *
 *   val identifier: Int by property()
 *
 *   companion object {
 *     operator fun invoke(isLazy: Boolean): DataGeneratorMock = delegateNew()
 *     fun generate(generator: DataGeneratorMock, input: Any): String = delegateStaticCall()
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
 * @author 凛 (RinOrz)
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
   * companion object {
   *
   *   /**
   *    * The actual type of the mock class.
   *    */
   *   val actualType: Type<T>
   *
   * }
   */
}

/**
 * Used to mock a class that exists in the runtime but cannot be accessed directly.
 *
 * @author 凛 (RinOrz)
 * @see InstanceMock<T: Any>
 */
typealias InstanceMoсk = InstanceMock<Any>