@file:Suppress("unused", "UNUSED_PARAMETER")

package com.meowool.cloak

import com.meowool.cloak.internal.ReflectionFactory
import com.meowool.cloak.internal.fastMap
import com.meowool.cloak.internal.resolveInstance
import com.meowool.cloak.internal.resolveType

/**
 * Creates (reflects) a class instance of this [Type] via the best matching constructor.
 *
 * Note that this function will try to find the most appropriate constructor. Therefore, even if the passed
 * parameter (argument) list is fuzzy (subclass or `null`), we will try our best to create it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock]:
 * ```
 * package com.meowool.cloak.example
 *
 * class B
 * class A: Cloneable {
 *   constructor(instance: B, str: String?) {
 *     println("Hello, cloak!")
 *   }
 * }
 *
 * // Example:
 * class AMock : InstanceMock<Cloneable> {
 *   val actualType = Type("com.meowool.cloak.example.A")
 * }
 * class BMock : InstanceMoсk {
 *   val actualType = Type("com.meowool.cloak.example.B")
 * }
 *
 * // Prints: "Hello, cloak!"
 * val instance: AMock = AMock.mock().new(
 *   BMock.mock(),
 *   null.typed<String>()
 * )
 * ```
 *
 * @receiver A class type to create an instance.
 * @param arguments The arguments to pass to the constructor.
 * @param parameters The parameter types of the constructor,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to: `args.map { it.javaClass }`).
 * @author 凛 (RinOrz)
 */
@Throws(NoSuchMethodException::class)
fun <T : Any> Type<T>.new(
  vararg arguments: Any?,
  parameters: Array<Type<*>?>? = null,
): T = ReflectionFactory.newInstance(
  clasѕ = javaClass,
  arguments = arguments.fastMap(Any?::resolveInstance),
  parameters = parameters?.fastMap { it?.javaClass } ?: arguments.fastMap(Any?::resolveType)
)
