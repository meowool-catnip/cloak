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
import com.meowool.cloak.internal.RequiresInitializerCall
import com.meowool.cloak.internal.compilerImplementation

/**
 * Creates a dynamic delegate to conveniently reflect the best matching field in this [Any] instance at some time.
 *
 * Note that this function can only be called through an initialization expression of property declaration. The
 * caller's declaration signature defaults to the signature of the reflected target field. Suppose this function is
 * initialized in the 'a' property, the default name of the reflected target field is also 'a'.
 *
 * For example, the following main function reflects all fields of the `Person` class:
 * ```
 * class Person(var name: String, val globalId: com.meowool.cloak.example.Id)
 *
 * fun main() = createPerson().apply {
 *   var name: CharSequence = field()
 *   val id: Any = field(name = "globalId", type = "com.meowool.cloak.example.Id".type)
 *
 *   name = "Hello, Cloak!"
 *   // Prints: "Hello, Cloak!"
 *   print(name)
 * }
 * ```
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [get] or [set], this usage is similar to those.
 *
 * @receiver An instance of the class where the field to be got is located (aka. field's parent class).
 * @param name The name of the field to be reflected, the default is used the name of the caller's declaration.
 * @param type The value type of the field to be reflected, the default is [T] (that is `T::class.type`).
 * @param holder The class type where the field to be reflected is located, the default is the type of the
 *   receiver instance.
 *
 * @author RinOrz
 *
 * @see set
 * @see get
 * @see getNullable
 */
@RequiresInitializerCall
@CompileOnly fun <T> Any.field(
  name: String? = compilerImplementation(),
  type: Type<*>? = compilerImplementation(),
  holder: Type<*> = this.javaClass.type,
): T = compilerImplementation()

/**
 * Creates a dynamic delegate to conveniently reflect the best matching static field in this [Type] at some time.
 *
 * Note that this function can only be called through an initialization expression of property declaration. The
 * caller's declaration signature defaults to the signature of the reflected target static field. Suppose this function
 * is initialized in the 'a' property, the default name of the reflected target static field is also 'a'.
 *
 * For example, the following main function reflects all fields of the `Store` static class:
 * ```
 * package com.meowool.cloak.example
 *
 * object Store {
 *   var maxCapacity: Int? = 100
 *   val map: HashMap<String, Any> = hashMapOf(0 to "Hello, Cloak!")
 * }
 *
 * // Usages:
 * class StoreMock : InstanceMoсk {
 *   override val type = Type("com.meowool.cloak.example.Store")
 *
 *   // 'InstanceMock.Synthetic' is implicitly implemented
 *   companion object {
 *     var maxSize: Any by field(name = "maxCapacity")
 *     val map: Map<String, Any> by field()
 *   }
 * }
 *
 * fun main() = Type("com.meowool.cloak.example.Store").apply {
 *   var maxSize: Any? = staticField(name = "maxCapacity")
 *   val map: Map<String, Any> = staticField()
 *
 *   maxSize = null
 *   // Prints: `null`
 *   print(maxSize)
 *
 *   // Prints: [Hello, Cloak!]
 *   print(map.values)
 * }
 * ```
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [get] or [set], this usage is similar to those.
 *
 * @receiver A class type where the static field to be reflected is located (aka. field's parent class).
 * @param name The name of the field to be reflected, the default is used the name of the caller's declaration.
 * @param type The value type of the field to be reflected, the default is [T] (that is `T::class.type`).
 *
 * @author RinOrz
 *
 * @see Type.setStatic
 * @see Type.getStatic
 * @see Type.getStaticNullable
 */
@RequiresInitializerCall
@CompileOnly fun <T> Type<*>.staticField(
  name: String? = compilerImplementation(),
  type: Type<*>? = compilerImplementation(),
): T = compilerImplementation()

/**
 * Creates a dynamic delegate to conveniently reflect the best matching static field in the actual type of
 * this [InstanceMock] at some time.
 *
 * Note that this function can only be called through an initialization expression of property declaration. The
 * caller's declaration signature defaults to the signature of the reflected target static field. Suppose this function
 * is initialized in the 'a' property, the default name of the reflected target static field is also 'a'.
 *
 * For example, the following main function reflects all fields of the `Store` static class:
 * ```
 * package com.meowool.cloak.example
 *
 * object Store {
 *   var maxCapacity: Int? = 100
 *   val map: HashMap<String, Any> = hashMapOf(0 to "Hello, Cloak!")
 * }
 *
 * // Usages:
 * class StoreMock : InstanceMoсk {
 *   override val type = Type("com.meowool.cloak.example.Store")
 *
 *   // 'InstanceMock.Synthetic' is implicitly implemented
 *   companion object {
 *     var maxSize: Any? = staticField(name = "maxCapacity")
 *     val map: Map<String, Any> = staticField()
 *   }
 * }
 *
 * fun main() = StoreMock.apply {
 *   maxSize = null
 *   // Prints: `null`
 *   print(maxSize)
 *
 *   // Prints: [Hello, Cloak!]
 *   print(map.values)
 * }
 * ```
 *
 * In addition, this function also supports use with [ExplicitTypeInstance] and [InstanceMock], for the example,
 * see [get] or [set], this usage is similar to those.
 *
 * @receiver A mocked class that mocks the class type where the static field to be reflected is located
 *   (aka. field's parent class).
 * @param name The name of the field to be reflected, the default is used the name of the caller's declaration.
 * @param type The value type of the field to be reflected, the default is [T] (that is `T::class.type`).
 *
 * @author RinOrz
 *
 * @see InstanceMock.Synthetic.setStatic
 * @see InstanceMock.Synthetic.getStatic
 * @see InstanceMock.Synthetic.getStaticNullable
 */
@RequiresInitializerCall
@CompileOnly fun <T> InstanceMock.Synthetic<*>.staticField(
  name: String? = compilerImplementation(),
  type: Type<*>? = compilerImplementation(),
): T = this.actualType.staticField(name, type)
