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
@file:Suppress("unused", "UNUSED_PARAMETER")

package com.meowool.cloak

import com.meowool.cloak.internal.CompileOnly
import com.meowool.cloak.internal.compilerImplementation

/**
 * Creates a dynamic delegate to conveniently create the instance through the best matching constructor in this [Type]
 * at some time.
 *
 * Note that this function can only be called through an initialization expression of function declaration. The
 * caller's declaration signature defaults to the signature of the reflected target constructor. Suppose this function
 * is initialized in the function with parameters list ['String'], the default parameters list of the reflected target
 * constructor is also ['String'].
 *
 * For example, the following main function reflects all constructors of the `User` class using [ExplicitTypeInstance]
 * and [InstanceMock] and [field]:
 * ```
 * package com.meowool.cloak.example
 *
 * class Id {
 *   constructor() {
 *     println("Id has been created.")
 *   }
 * }
 * class User {
 *   val message = "Hello, Cloak!"
 *
 *   constructor(id: Id) {
 *     println("User has been created.")
 *   }
 * }
 *
 * class IdMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.Id")
 *   companion object {
 *     operator fun invoke() = constructor()
 *   }
 * }
 *
 * class UserMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.User")
 *   val message = field()
 *   companion object {
 *     operator fun invoke(id: IdMock) = constructor()
 *   }
 * }
 *
 * // Prints:
 * //   Id has been created.
 * //   User has been created.
 * //   Hello, Cloak!
 * fun main() {
 *   val user = UserMock(IdMock())
 *   println(user.message)
 * }
 * ```
 *
 * @receiver A class type where the constructor to be reflected is located (aka. constructor's parent class).
 * @param arguments The arguments to pass to the constructor to be reflected, the default is used the passed in
 *   arguments of the caller's declaration.
 *
 * @param parameters The parameter types of the constructor to be reflected,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @author 凛 (RinOrz)
 *
 * @see Type.new
 */
@CompileOnly fun <T : Any> Type<T>.constructor(
  arguments: Array<*> = compilerImplementation(),
  parameters: Array<Type<*>?>? = null,
): T = compilerImplementation()

/**
 * Creates a dynamic delegate to conveniently create the instance through the best matching constructor in the actual
 * type of this [InstanceMock] at some time.
 *
 * Note that this function can only be called through an initialization expression of function declaration. The
 * caller's declaration signature defaults to the signature of the reflected target constructor. Suppose this function
 * is initialized in the function with parameters list ['String'], the default parameters list of the reflected target
 * constructor is also ['String'].
 *
 * For example, the following main function reflects all constructors of the `User` class using [ExplicitTypeInstance]
 * and [InstanceMock] and [field]:
 * ```
 * package com.meowool.cloak.example
 *
 * class Id {
 *   constructor() {
 *     println("Id has been created.")
 *   }
 * }
 * class User {
 *   val message = "Hello, Cloak!"
 *
 *   constructor(id: Id) {
 *     println("User has been created.")
 *   }
 * }
 *
 * class IdMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.Id")
 *   companion object {
 *     operator fun invoke() = constructor()
 *   }
 * }
 *
 * class UserMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.User")
 *   val message = field()
 *   companion object {
 *     operator fun invoke(id: IdMock) = constructor()
 *   }
 * }
 *
 * // Prints:
 * //   Id has been created.
 * //   User has been created.
 * //   Hello, Cloak!
 * fun main() {
 *   val user = UserMock(IdMock())
 *   println(user.message)
 * }
 * ```
 *
 * @receiver A mocked class that mocks the class type where the constructor to be reflected is located
 *   (aka. static constructor's parent class).
 * @param arguments The arguments to pass to the constructor to be reflected, the default is used the passed in
 *   arguments of the caller's declaration.
 *
 * @param parameters The parameter types of the constructor to be reflected,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @author 凛 (RinOrz)
 *
 * @see InstanceMock.Synthetic.new
 */
@CompileOnly fun <T : Any> InstanceMock.Synthetic<T>.constructor(
  arguments: Array<*> = compilerImplementation(),
  parameters: Array<Type<*>?>? = null,
): T = this.actualType.constructor(arguments, parameters)
