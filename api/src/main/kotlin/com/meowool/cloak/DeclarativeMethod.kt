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
@file:Suppress("UNUSED_PARAMETER", "unused")

package com.meowool.cloak

import com.meowool.cloak.internal.CompileOnly
import com.meowool.cloak.internal.compilerImplementation

/**
 * Creates a dynamic delegate to conveniently reflect the best matching method in this [Any] instance at some time.
 *
 * Note that this function can only be called through an initialization expression of function declaration. The
 * caller's declaration signature defaults to the signature of the reflected target method. Suppose this function is
 * initialized in the 'a' function, the default name of the reflected target method is also 'a'.
 *
 * For example, the following main function reflects all methods of the `Manager` class:
 * ```
 * class Manager {
 *   fun save(name: CharSequence) = println("Saving name: $name")
 *   fun reset(): Boolean = false
 * }
 *
 * fun main() = createManager().apply {
 *   fun save(name: String) = methodCall()
 *   fun restore(): Boolean = methodCall(name = "reset")
 *
 *   // Prints: "Saving name: Rin"
 *   save("Rin")
 *
 *   // Prints: `false`
 *   print(reset())
 * }
 * ```
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [get] or [set], this usage is similar to those.
 *
 * @receiver An instance of the class where the method to be reflected is located (aka. method's parent class).
 * @param name The name of the method to be reflected, the default is used the name of the caller's declaration.
 * @param arguments The arguments to pass to the method to be reflected, the default is used the passed in arguments
 *   of the caller's declaration.
 *
 * @param parameters The parameter types of the method to be reflected,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @param returns The return type of the method to be reflected, the default is [R] (that is `R::class.type`).
 * @param parent The class type where the method to be reflected is located, the default is the type of the
 *   receiver instance.
 *
 * @author 凛 (RinOrz)
 *
 * @see call
 * @see callVoid
 * @see callNullable
 */
@CompileOnly fun <R> Any.methodCall(
  name: String? = compilerImplementation(),
  arguments: Array<*> = compilerImplementation(),
  parameters: Array<Type<*>?>? = null,
  returns: Type<*>? = compilerImplementation(),
  parent: Type<*> = this.javaClass.type,
): R = compilerImplementation()

/**
 * Creates a dynamic delegate to conveniently reflect the best matching static method in this [Type] at some time.
 *
 * Note that this function can only be called through an initialization expression of function declaration. The
 * caller's declaration signature defaults to the signature of the reflected target static method. Suppose this
 * function is initialized in the 'a' function, the default name of the reflected target static method is also 'a'.
 *
 * For example, the following main function reflects all static methods of the `Manager` class:
 * ```
 * object Manager {
 *   fun save(name: CharSequence) = println("Saving name: $name")
 *   fun reset(): Boolean = false
 * }
 *
 * fun main() = Manager.apply {
 *   fun save(name: String) = staticMethodCall()
 *   fun restore(): Boolean = staticMethodCall(name = "reset")
 *
 *   // Prints: "Saving name: Rin"
 *   save("Rin")
 *
 *   // Prints: `false`
 *   print(reset())
 * }
 * ```
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [get] or [set], this usage is similar to those.
 *
 * @receiver A class type where the static method to be reflected is located (aka. method's parent class).
 * @param name The name of the static method to be reflected, the default is used the name of the caller's declaration.
 * @param arguments The arguments to pass to the static method to be reflected, the default is used the passed in
 *   arguments of the caller's declaration.
 *
 * @param parameters The parameter types of the static method to be reflected,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @param returns The return type of the static method to be reflected, the default is [R] (that is `R::class.type`).
 *
 * @author 凛 (RinOrz)
 *
 * @see Type.callStatic
 * @see Type.callStaticVoid
 * @see Type.callStaticNullable
 */
@CompileOnly fun <R> Type<*>.staticMethodCall(
  name: String? = compilerImplementation(),
  arguments: Array<*> = compilerImplementation(),
  parameters: Array<Type<*>?>? = null,
  returns: Type<*>? = compilerImplementation(),
): R = compilerImplementation()

/**
 * Creates a dynamic delegate to conveniently reflect the best matching static method in the actual type of
 * this [InstanceMock] at some time.
 *
 * Note that this function can only be called through an initialization expression of function declaration. The
 * caller's declaration signature defaults to the signature of the reflected target static method. Suppose this
 * function is initialized in the 'a' function, the default name of the reflected target static method is also 'a'.
 *
 * For example, the following main function reflects all static methods of the `Manager` class:
 * ```
 * object Manager {
 *   fun save(name: CharSequence) = println("Saving name: $name")
 *   fun reset(): Boolean = false
 * }
 *
 * fun main() = Manager.apply {
 *   fun save(name: String) = staticMethodCall()
 *   fun restore(): Boolean = staticMethodCall(name = "reset")
 *
 *   // Prints: "Saving name: Rin"
 *   save("Rin")
 *
 *   // Prints: `false`
 *   print(reset())
 * }
 * ```
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [get] or [set], this usage is similar to those.
 *
 * @receiver A mocked class that mocks the class type where the static method to be reflected is located
 *   (aka. static method's parent class).
 * @param name The name of the static method to be reflected, the default is used the name of the caller's declaration.
 * @param arguments The arguments to pass to the static method to be reflected, the default is used the passed in
 *   arguments of the caller's declaration.
 *
 * @param parameters The parameter types of the static method to be reflected,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @param returns The return type of the static method to be reflected, the default is [R] (that is `R::class.type`).
 *
 * @author 凛 (RinOrz)
 *
 * @see InstanceMock.Synthetic.callStatic
 * @see InstanceMock.Synthetic.callStaticVoid
 * @see InstanceMock.Synthetic.callStaticNullable
 */
@CompileOnly fun <R> InstanceMock.Synthetic<*>.staticMethodCall(
  name: String? = compilerImplementation(),
  arguments: Array<*> = compilerImplementation(),
  parameters: Array<Type<*>?>? = null,
  returns: Type<*>? = compilerImplementation(),
): R = compilerImplementation()
