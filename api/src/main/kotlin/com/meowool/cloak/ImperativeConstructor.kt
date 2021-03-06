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
@file:Suppress("unused", "UNUSED_PARAMETER", "NOTHING_TO_INLINE")

package com.meowool.cloak

import com.meowool.cloak.internal.ReflectionFactory
import com.meowool.cloak.internal.fastMap
import com.meowool.cloak.internal.resolveClass
import com.meowool.cloak.internal.resolveInstance

/**
 * Creates (reflects) a class instance of this [Type] via the best matching constructor.
 *
 * Note that this function will try to find the most appropriate field. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to create it.
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
 *   override val actualType = Type("com.meowool.cloak.example.A")
 * }
 * class BMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.B")
 * }
 *
 * // Prints: "Hello, cloak!"
 * val instance: AMock = AMock.actualType.new(
 *   BMock.new(),
 *   null.typed<String>()
 * )
 * ```
 *
 * @receiver A class type to create an instance.
 * @param arguments The arguments to pass to the constructor.
 * @param parameters The parameter types of the constructor,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @author 凛 (RinOrz)
 *
 * @see Type.constructorNew
 */
@Throws(NoSuchMethodException::class)
fun <T : Any> Type<T>.new(
  vararg arguments: Any?,
  parameters: Array<Type<*>?>? = null,
): T = ReflectionFactory.newInstance(
  clasѕ = javaClass,
  arguments = arguments.fastMap(Any?::resolveInstance),
  parameters = parameters?.fastMap { it?.javaClass } ?: arguments.fastMap(Any?::resolveClass)
)

/**
 * Creates (reflects) a class instance of the actual type of this [InstanceMock] via the best matching constructor.
 *
 * Note that this function will try to find the most appropriate field. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to create it.
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
 *   override val actualType = Type("com.meowool.cloak.example.A")
 * }
 * class BMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.B")
 * }
 *
 * // Prints: "Hello, cloak!"
 * val instance: AMock = AMock.actualType.new(
 *   BMock.new(),
 *   null.typed<String>()
 * )
 * ```
 *
 * @receiver A class type to create an instance.
 * @param arguments The arguments to pass to the constructor.
 * @param parameters The parameter types of the constructor,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @author 凛 (RinOrz)
 *
 * @see InstanceMock.Synthetic.constructorNew
 */
@Throws(NoSuchMethodException::class)
inline fun <T : Any> InstanceMock.Synthetic<T>.new(
  vararg arguments: Any?,
  parameters: Array<Type<*>?>? = null,
): T = actualType.new(arguments = arguments, parameters = parameters)
