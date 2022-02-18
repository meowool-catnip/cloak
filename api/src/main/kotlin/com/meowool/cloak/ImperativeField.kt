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

import com.meowool.cloak.internal.ReflectionFactory

/**
 * Gets (reflects) the value of the best matching field in this [Any] instance.
 *
 * Note that this function will try to find the most appropriate field. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to get it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock]:
 * ```
 * package com.meowool.cloak.example
 *
 * class B(val message: String)
 * class A: Cloneable {
 *   val foo: B = B("Hello, cloak!")
 * }
 *
 * // Example:
 * class AMock : InstanceMock<Cloneable> {
 *   override val actualType = Type("com.meowool.cloak.example.A")
 *   val data: BMock get() = get("foo")
 * }
 * class BMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.B")
 *   val msg: String get() = get("message")
 * }
 *
 * // Prints: "Hello, cloak!"
 * print(AMock.new().data.msg)
 * ```
 *
 * @receiver An instance of the class where the field to be got is located (aka. field's parent class).
 * @param name The name of the field to get.
 * @param type The value type of the field to get, the default is [T].
 * @param parent The class type where the field to get is located, the default is the type of the receiver instance.
 *
 * @author 凛 (RinOrz)
 *
 * @see field
 */
@Throws(NoSuchFieldException::class)
inline fun <reified T : Any> Any.get(
  name: String?,
  type: Type<T>? = Type(T::class.java),
  parent: Type<*> = this.javaClass.type,
): T = requireNotNull(getNullable(name, type, parent))

/**
 * Gets (reflects) the nullable value of the best matching field in this [Any] instance.
 *
 * Note that this function will try to find the most appropriate field. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to get it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock]:
 * ```
 * package com.meowool.cloak.example
 *
 * class B(val message: String)
 * class A: Cloneable {
 *   val foo: B = B("Hello, cloak!")
 * }
 *
 * // Example:
 * class AMock : InstanceMock<Cloneable> {
 *   override val actualType = Type("com.meowool.cloak.example.A")
 *   val data: BMock get() = get("foo")
 * }
 * class BMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.B")
 *   val msg: String get() = get("message")
 * }
 *
 * // Prints: "Hello, cloak!"
 * print(AMock.new().data.msg)
 * ```
 *
 * @receiver An instance of the class where the field to be got is located (aka. field's parent class).
 * @param name The name of the field to get.
 * @param type The value type of the field to get, the default is [T].
 * @param parent The class type where the field to get is located, the default is the type of the receiver instance.
 *
 * @author 凛 (RinOrz)
 *
 * @see field
 */
@Throws(NoSuchFieldException::class)
inline fun <reified T : Any> Any.getNullable(
  name: String?,
  type: Type<T>? = Type(T::class.java),
  parent: Type<*> = this.javaClass.type,
): T? = ReflectionFactory.get(clasѕ = parent.javaClass, instance = this, name, type?.javaClass)

/**
 * Sets (reflects) the [value] of the best matching field in this [Any] instance.
 *
 * Note that this function will try to find the most appropriate field. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to set it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock]:
 * ```
 * package com.meowool.cloak.example
 *
 * class B(val message: String)
 * class A: Cloneable {
 *   var foo: B = B("Hello, cloak!")
 * }
 *
 * // Example:
 * class AMock : InstanceMock<Cloneable> {
 *   override val actualType = Type("com.meowool.cloak.example.A")
 *   var data: BMock
 *     set(value) = set("foo", value)
 *     get() = get("foo")
 * }
 * class BMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.B")
 *   val msg: String get() = get("message")
 * }
 *
 * // Prints: "Hello, world!"
 * val instance: AMock = AMock.new()
 * instance.data = BMock.new("Hello, world!")
 * print(instance.data.msg)
 * ```
 *
 * @receiver An instance of the class where the field to be set is located (aka. field's parent class).
 * @param name The name of the field to set.
 * @param value The new value to be set to the field.
 * @param type The value type of the field to set, the default is [T].
 * @param parent The class type where the field to set is located, the default is the type of the receiver instance.
 *
 * @author 凛 (RinOrz)
 *
 * @see field
 */
@Throws(NoSuchFieldException::class)
inline fun <reified T : Any> Any.set(
  name: String?,
  value: T,
  type: Type<T>? = Type(T::class.java),
  parent: Type<*> = this.javaClass.type,
): T = value.also {
  ReflectionFactory.set(clasѕ = parent.javaClass, instance = this, name, value, type?.javaClass)
}

/**
 * Sets (reflects) the [value] of the best matching field in this [Any] instance.
 *
 * Note that this function will try to find the most appropriate field. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to set it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock]:
 * ```
 * package com.meowool.cloak.example
 *
 * class B(val message: String)
 * class A: Cloneable {
 *   var foo: B = B("Hello, cloak!")
 * }
 *
 * // Example:
 * class AMock : InstanceMock<Cloneable> {
 *   override val actualType = Type("com.meowool.cloak.example.A")
 *   var data: BMock
 *     set(value) = set("foo", value)
 *     get() = get("foo")
 * }
 * class BMock : InstanceMoсk {
 *   override val actualType = Type("com.meowool.cloak.example.B")
 *   val msg: String get() = get("message")
 * }
 *
 * // Prints: "Hello, world!"
 * val instance: AMock = AMock.new()
 * instance.data = BMock.new("Hello, world!")
 * print(instance.data.msg)
 * ```
 *
 * @receiver An instance of the class where the field to be set is located (aka. field's parent class).
 * @param name The name of the field to set.
 * @param value The new value to be set to the field.
 * @param type The value type of the field to set, the default is [T].
 * @param parent The class type where the field to set is located, the default is the type of the receiver instance.
 *
 * @author 凛 (RinOrz)
 *
 * @see field
 */
@Throws(NoSuchFieldException::class)
inline fun <reified T : Any> Any.set(
  name: String?,
  value: T?,
  type: Type<T>? = Type(T::class.java),
  parent: Type<*> = this.javaClass.type,
): Unit = ReflectionFactory.set(clasѕ = parent.javaClass, instance = this, name, value, type?.javaClass)

/**
 * Gets (reflects) the value of the best matching static field in this [Type].
 *
 * Note that this function will try to find the most appropriate field. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to get it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [get], this usage is similar to that.
 *
 * @receiver A class type where the static field to be got is located (aka. static field's parent class).
 * @param name The name of the static field to get.
 * @param type The value type of the static field to get, the default is [T].
 *
 * @author 凛 (RinOrz)
 *
 * @see Type.staticField
 */
@Throws(NoSuchFieldException::class)
inline fun <reified T : Any> Type<*>.getStatic(
  name: String?,
  type: Type<T>? = Type(T::class.java),
): T = requireNotNull(getStaticNullable(name, type))

/**
 * Gets (reflects) the nullable value of the best matching static field in this [Type].
 *
 * Note that this function will try to find the most appropriate field. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to get it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [get], this usage is similar to that.
 *
 * @receiver A class type where the static field to be got is located (aka. static field's parent class).
 * @param name The name of the static field to get.
 * @param type The value type of the static field to get, the default is [T].
 *
 * @author 凛 (RinOrz)
 *
 * @see Type.staticField
 */
@Throws(NoSuchFieldException::class)
inline fun <reified T : Any> Type<*>.getStaticNullable(
  name: String?,
  type: Type<T>? = Type(T::class.java),
): T? = ReflectionFactory.get(clasѕ = this.javaClass, instance = null, name, type?.javaClass)

/**
 * Gets (reflects) the value of the best matching static field in the actual type of this [InstanceMock].
 *
 * Note that this function will try to find the most appropriate field. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to get it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [get], this usage is similar to that.
 *
 * @receiver A mocked class that mocks the class type where the static field to be got is located
 *   (aka. static field's parent class).
 * @param name The name of the static field to get.
 * @param type The value type of the static field to get, the default is [T].
 *
 * @author 凛 (RinOrz)
 *
 * @see InstanceMock.Synthetic.staticField
 */
@Throws(NoSuchFieldException::class)
inline fun <reified T : Any> InstanceMock.Synthetic<*>.getStatic(
  name: String?,
  type: Type<T>? = Type(T::class.java),
): T = this.actualType.getStatic(name, type)

/**
 * Gets (reflects) the nullable value of the best matching static field in the actual type of this [InstanceMock].
 *
 * Note that this function will try to find the most appropriate field. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to get it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [get], this usage is similar to that.
 *
 * @receiver A mocked class that mocks the class type where the static field to be got is located
 *   (aka. static field's parent class).
 * @param name The name of the static field to get.
 * @param type The value type of the static field to get, the default is [T].
 *
 * @author 凛 (RinOrz)
 *
 * @see InstanceMock.Synthetic.staticField
 */
@Throws(NoSuchFieldException::class)
inline fun <reified T : Any> InstanceMock.Synthetic<*>.getStaticNullable(
  name: String?,
  type: Type<T>? = Type(T::class.java),
): T? = this.actualType.getStaticNullable(name, type)

/**
 * Sets (reflects) the [value] of the best matching static field in this [Type].
 *
 * Note that this function will try to find the most appropriate field. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to set it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [set], this usage is similar to that.
 *
 * @receiver A class type where the static field to be set is located (aka. static field's parent class).
 * @param name The name of the static field to set.
 * @param value The new value to be set to the static field.
 * @param type The value type of the static field to set, the default is [T].
 *
 * @author 凛 (RinOrz)
 *
 * @see Type.staticField
 */
@Throws(NoSuchFieldException::class)
inline fun <reified T : Any> Type<*>.setStatic(
  name: String?,
  value: T,
  type: Type<T>? = Type(T::class.java),
): T = value.also {
  ReflectionFactory.set(clasѕ = this.javaClass, instance = null, name, value, type?.javaClass)
}

/**
 * Sets (reflects) the [value] of the best matching static field in this [Type].
 *
 * Note that this function will try to find the most appropriate field. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to set it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [set], this usage is similar to that.
 *
 * @receiver A class type where the static field to be set is located (aka. static field's parent class).
 * @param name The name of the static field to set.
 * @param value The new value to be set to the static field.
 * @param type The value type of the static field to set, the default is [T].
 *
 * @author 凛 (RinOrz)
 *
 * @see Type.staticField
 */
@Throws(NoSuchFieldException::class)
inline fun <reified T : Any> Type<*>.setStatic(
  name: String?,
  value: T?,
  type: Type<T>? = Type(T::class.java),
): Unit = ReflectionFactory.set(clasѕ = this.javaClass, instance = null, name, value, type?.javaClass)

/**
 * Sets (reflects) the [value] of the best matching static field in the actual type of this [InstanceMock].
 *
 * Note that this function will try to find the most appropriate field. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to set it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [set], this usage is similar to that.
 *
 * @receiver A mocked class that mocks the class type where the static field to be set is located
 *   (aka. static field's parent class).
 * @param name The name of the static field to set.
 * @param value The new value to be set to the static field.
 * @param type The value type of the static field to set, the default is [T].
 *
 * @author 凛 (RinOrz)
 *
 * @see InstanceMock.Synthetic.staticField
 */
@Throws(NoSuchFieldException::class)
inline fun <reified T : Any> InstanceMock.Synthetic<*>.setStatic(
  name: String?,
  value: T,
  type: Type<T>? = Type(T::class.java),
): T = this.actualType.setStatic(name, value, type)

/**
 * Sets (reflects) the [value] of the best matching static field in the actual type of this [InstanceMock].
 *
 * Note that this function will try to find the most appropriate field. Therefore, even if the passed arguments have
 * any ambiguity (`null` or subclass), we do our best to set it.
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [set], this usage is similar to that.
 *
 * @receiver A mocked class that mocks the class type where the static field to be set is located
 *   (aka. static field's parent class).
 * @param name The name of the static field to set.
 * @param value The new value to be set to the static field.
 * @param type The value type of the static field to set, the default is [T].
 *
 * @author 凛 (RinOrz)
 *
 * @see InstanceMock.Synthetic.staticField
 */
@Throws(NoSuchFieldException::class)
inline fun <reified T : Any> InstanceMock.Synthetic<*>.setStatic(
  name: String?,
  value: T?,
  type: Type<T>? = Type(T::class.java),
): Unit = this.actualType.setStatic(name, value, type)
