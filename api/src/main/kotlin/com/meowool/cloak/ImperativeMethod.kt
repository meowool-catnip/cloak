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
@file:Suppress("NOTHING_TO_INLINE", "RedundantUnitReturnType", "unused")

package com.meowool.cloak

import com.meowool.cloak.builtin.types._void
import com.meowool.cloak.internal.ReflectionFactory
import com.meowool.cloak.internal.fastMap
import com.meowool.cloak.internal.resolveClass
import com.meowool.cloak.internal.resolveInstance

/**
 * Calls (reflects) the best matching method in this [Any] instance and returns its result value.
 *
 * Note that this function will try to find the most appropriate method. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to invoke it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock]:
 * ```
 * package com.meowool.cloak.example
 *
 * class Result(val message: String)
 * class B: Cloneable
 * class A {
 *   fun calculate(b: Cloneable): Result {
 *     ...
 *     return Result("OK!")
 *   }
 * }
 *
 * // Example:
 * class AMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.A")
 * }
 * class BMock : InstanceMock<Cloneable> {
 *   override val actualType = Type("com.meowool.cloak.example.B")
 * }
 * class ResultMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.Result")
 *   val message: String get() = get("message")
 * }
 *
 * // Prints: "OK!"
 * val result = AMock.new().call<ResultMock>("calculate", BMock.new())
 * print(result.message)
 * ```
 *
 * @receiver An instance of the class where the method to be called is located (aka. method's parent class).
 * @param name The name of the method to call.
 * @param arguments The arguments to pass to the method to call.
 * @param parameters The parameter types of the method to call,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @param returns The return type of the method to call, the default is [R].
 * @param holder The class type where the method to call is located, the default is the type of the receiver instance.
 *
 * @author 凛 (RinOrz)
 *
 * @see methodCall
 */
@Throws(NoSuchMethodException::class)
inline fun <reified R : Any> Any.call(
  name: String?,
  vararg arguments: Any?,
  parameters: Array<Type<*>?>? = null,
  returns: Type<R>? = Type(R::class.java),
  holder: Type<*> = this.javaClass.type,
): R = requireNotNull(callNullable(name, *arguments, parameters = parameters, returns = returns, holder = holder))

/**
 * Calls (reflects) the best matching method in this [Any] instance and returns its result nullable value.
 *
 * Note that this function will try to find the most appropriate method. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to invoke it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock]:
 * ```
 * package com.meowool.cloak.example
 *
 * class Result(val message: String)
 * class B: Cloneable
 * class A {
 *   fun calculate(b: Cloneable): Result? {
 *     ...
 *     return null
 *   }
 * }
 *
 * // Example:
 * class AMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.A")
 * }
 * class BMock : InstanceMock<Cloneable> {
 *   override val actualType = Type("com.meowool.cloak.example.B")
 * }
 * class ResultMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.Result")
 * }
 *
 * // Prints: `null`
 * print(AMock.new().callNullable<ResultMock>("calculate", BMock.new()))
 * ```
 *
 * @receiver An instance of the class where the method to be called is located (aka. method's parent class).
 * @param name The name of the method to call.
 * @param arguments The arguments to pass to the method to call.
 * @param parameters The parameter types of the method to call,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @param returns The return type of the method to call, the default is [R].
 * @param holder The class type where the method to call is located, the default is the type of the receiver instance.
 *
 * @author 凛 (RinOrz)
 *
 * @see methodCall
 */
@Throws(NoSuchMethodException::class)
inline fun <reified R : Any> Any.callNullable(
  name: String?,
  vararg arguments: Any?,
  parameters: Array<Type<*>?>? = null,
  returns: Type<R>? = Type(R::class.java),
  holder: Type<*> = this.javaClass.type,
): R? = ReflectionFactory.call(
  clasѕ = holder.javaClass,
  instance = this,
  name = name,
  arguments = arguments.fastMap(Any?::resolveInstance),
  parameters = parameters?.fastMap { it?.javaClass } ?: arguments.fastMap(Any?::resolveClass),
  returns = returns?.javaClass,
  returnType = R::class.java
)

/**
 * Calls (reflects) the best matching void method in this [Any] instance and returns its result value.
 *
 * Note that this function will try to find the most appropriate method. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to invoke it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock]:
 * ```
 * package com.meowool.cloak.example
 *
 * class B: Cloneable
 * class A {
 *   fun load(b: Cloneable) {
 *     print("Loaded!")
 *   }
 * }
 *
 * // Example:
 * class AMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.A")
 * }
 * class BMock : InstanceMock<Cloneable> {
 *   override val actualType = Type("com.meowool.cloak.example.B")
 * }
 *
 * // Prints: "Loaded!"
 * AMock.new().callVoid("load", BMock.new())
 * ```
 *
 * @receiver An instance of the class where the method to be called is located (aka. method's parent class).
 * @param name The name of the method to call.
 * @param arguments The arguments to pass to the method to call.
 * @param parameters The parameter types of the method to call,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @param holder The class type where the method to call is located, the default is the type of the receiver instance.
 *
 * @author 凛 (RinOrz)
 *
 * @see methodCall
 */
@Throws(NoSuchMethodException::class)
inline fun Any.callVoid(
  name: String?,
  vararg arguments: Any?,
  parameters: Array<Type<*>?>? = null,
  holder: Type<*> = this.javaClass.type,
) {
  callNullable(name, *arguments, parameters = parameters, returns = _void, holder = holder)
}

/**
 * Calls (reflects) the best matching static method in this [Type] and returns its result value.
 *
 * Note that this function will try to find the most appropriate static method. Therefore, even if the passed arguments
 * have any ambiguity (`null` or subclass), we do our best to invoke it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [call], this usage is similar to that.
 *
 * @receiver A class type where the static method to be called is located (aka. static method's parent class).
 * @param name The name of the static method to call.
 * @param arguments The arguments to pass to the static method to call.
 * @param parameters The parameter types of the static method to call,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @param returns The return type of the static method to call, the default is [R].
 *
 * @author 凛 (RinOrz)
 *
 * @see Type.staticMethodCall
 */
@Throws(NoSuchMethodException::class)
inline fun <reified R : Any> Type<*>.callStatic(
  name: String?,
  vararg arguments: Any?,
  parameters: Array<Type<*>?>? = null,
  returns: Type<R>? = Type(R::class.java),
): R = requireNotNull(callStaticNullable(name, *arguments, parameters = parameters, returns = returns))

/**
 * Calls (reflects) the best matching static method in this [Type] and returns its result nullable value.
 *
 * Note that this function will try to find the most appropriate static method. Therefore, even if the passed arguments
 * have any ambiguity (`null` or subclass), we do our best to invoke it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [callNullable], this usage is similar to that.
 *
 * @receiver A class type where the static method to be called is located (aka. static method's parent class).
 * @param name The name of the static method to call.
 * @param arguments The arguments to pass to the static method to call.
 * @param parameters The parameter types of the static method to call,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @param returns The return type of the static method to call, the default is [R].
 *
 * @author 凛 (RinOrz)
 *
 * @see Type.staticMethodCall
 */
@Throws(NoSuchMethodException::class)
inline fun <reified R : Any> Type<*>.callStaticNullable(
  name: String?,
  vararg arguments: Any?,
  parameters: Array<Type<*>?>? = null,
  returns: Type<R>? = Type(R::class.java),
): R? = ReflectionFactory.call(
  clasѕ = this.javaClass,
  instance = null,
  name = name,
  arguments = arguments.fastMap(Any?::resolveInstance),
  parameters = parameters?.fastMap { it?.javaClass } ?: arguments.fastMap(Any?::resolveClass),
  returns = returns?.javaClass,
  returnType = R::class.java
)

/**
 * Calls (reflects) the best matching static void method in this [Type].
 *
 * Note that this function will try to find the most appropriate static method. Therefore, even if the passed arguments
 * have any ambiguity (`null` or subclass), we do our best to invoke it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [callVoid], this usage is similar to that.
 *
 * @receiver An instance of the class where the static method to be called is located (aka. method's parent class).
 * @param name The name of the static method to call.
 * @param arguments The arguments to pass to the static method to call.
 * @param parameters The parameter types of the static method to call,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @author 凛 (RinOrz)
 *
 * @see Type.staticMethodCall
 */
@Throws(NoSuchMethodException::class)
inline fun Type<*>.callStaticVoid(
  name: String?,
  vararg arguments: Any?,
  parameters: Array<Type<*>?>? = null,
) {
  callStaticNullable(name, *arguments, parameters = parameters, returns = _void)
}

/**
 * Calls (reflects) the best matching static method in this [Type] and returns its result value.
 *
 * Note that this function will try to find the most appropriate static method. Therefore, even if the passed arguments
 * have any ambiguity (`null` or subclass), we do our best to invoke it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [call], this usage is similar to that.
 *
 * @receiver A mocked class that mocks the class type where the static method to be called is located
 *   (aka. static method's parent class).
 * @param name The name of the static method to call.
 * @param arguments The arguments to pass to the static method to call.
 * @param parameters The parameter types of the static method to call,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @param returns The return type of the static method to call, the default is [R].
 *
 * @author 凛 (RinOrz)
 *
 * @see InstanceMock.Synthetic.staticMethodCall
 */
@Throws(NoSuchMethodException::class)
inline fun <reified R : Any> InstanceMock.Synthetic<*>.callStatic(
  name: String?,
  vararg arguments: Any?,
  parameters: Array<Type<*>?>? = null,
  returns: Type<R>? = Type(R::class.java),
): R = requireNotNull(callStaticNullable(name, *arguments, parameters = parameters, returns = returns))

/**
 * Calls (reflects) the best matching static method in this [Type] and returns its result nullable value.
 *
 * Note that this function will try to find the most appropriate static method. Therefore, even if the passed arguments
 * have any ambiguity (`null` or subclass), we do our best to invoke it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [callNullable], this usage is similar to that.
 *
 * @receiver A mocked class that mocks the class type where the static method to be called is located
 *   (aka. static method's parent class).
 * @param name The name of the static method to call.
 * @param arguments The arguments to pass to the static method to call.
 * @param parameters The parameter types of the static method to call,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @param returns The return type of the static method to call, the default is [R].
 *
 * @author 凛 (RinOrz)
 *
 * @see InstanceMock.Synthetic.staticMethodCall
 */
@Throws(NoSuchMethodException::class)
inline fun <reified R : Any> InstanceMock.Synthetic<*>.callStaticNullable(
  name: String?,
  vararg arguments: Any?,
  parameters: Array<Type<*>?>? = null,
  returns: Type<R>? = Type(R::class.java),
): R? = ReflectionFactory.call(
  clasѕ = this.javaClass,
  instance = null,
  name = name,
  arguments = arguments.fastMap(Any?::resolveInstance),
  parameters = parameters?.fastMap { it?.javaClass } ?: arguments.fastMap(Any?::resolveClass),
  returns = returns?.javaClass,
  returnType = R::class.java
)

/**
 * Calls (reflects) the best matching static void method in this [Type].
 *
 * Note that this function will try to find the most appropriate static method. Therefore, even if the passed arguments
 * have any ambiguity (`null` or subclass), we do our best to invoke it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [callVoid], this usage is similar to that.
 *
 * @receiver A mocked class that mocks the class type where the static method to be called is located
 *   (aka. static method's parent class).
 * @param name The name of the static method to call.
 * @param arguments The arguments to pass to the static method to call.
 * @param parameters The parameter types of the static method to call,
 *   it should be consistent with the number of [arguments] (except for function with varargs).
 *   the default is the inference of the arguments instances (similar to `args.map { it?.javaClass }`).
 *
 * @author 凛 (RinOrz)
 *
 * @see InstanceMock.Synthetic.staticMethodCall
 */
@Throws(NoSuchMethodException::class)
inline fun InstanceMock.Synthetic<*>.callStaticVoid(
  name: String?,
  vararg arguments: Any?,
  parameters: Array<Type<*>?>? = null,
) {
  callStaticNullable(name, *arguments, parameters = parameters, returns = _void)
}
