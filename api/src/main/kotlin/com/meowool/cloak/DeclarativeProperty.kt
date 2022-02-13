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
import kotlin.reflect.KProperty

/**
 * A class that provides the ability to delegate for JVM field reflection.
 *
 * @author 凛 (RinOrz)
 */
@CompileOnly class CloakPropertyReflection<T> {

  /**
   * Returns the value of the JVM field reflected by this delegate.
   *
   * @see property
   * @see staticProperty
   */
  operator fun getValue(thisRef: Any?, property: KProperty<*>): T = compilerImplementation()

  /**
   * Changes the value of the JVM field reflected by this delegate.
   *
   * @see property
   * @see staticProperty
   */
  operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?): Unit = compilerImplementation()
}

/**
 * Creates a delegate to reflect the property in this [Any] at some point.
 *
 * For example, the following main function reflects all fields of the `Person` class:
 * ```
 * data class Person(var name: String, val globalId: com.meowool.cloak.example.Id)
 *
 * fun main() = createPerson().apply {
 *   var name: String by property()
 *   val id: Any by property(name = "globalId", type = "com.meowool.cloak.example.Id".type)
 *
 *   name = "Cloak"
 *   println(name) // "Cloak"
 * }
 * ```
 *
 * @receiver The parent class instance of the property to be reflected.
 * @param name The name of the property to be reflected, the name of the delegation source (caller) is used by default.
 * @param type The actual type of the property to be reflected, the default is [T] (that is: `T::class.type`).
 * @param parent The parent class type of the property to be reflected, the type of [Any] receiver that uses this
 *   function by default (that is: `this.javaClass.type`).
 *
 * @author 凛 (RinOrz)
 */
@CompileOnly inline fun <reified T : Any> Any.property(
  name: String = compilerImplementation(),
  type: Type<T> = compilerImplementation(),
  parent: Type<*> = compilerImplementation(),
): CloakPropertyReflection<T> = compilerImplementation()

/**
 * Creates a delegate to reflect the static property in this [Type] at some point.
 *
 * For example, the following main function reflects the 'UID' field of the `Utils` static class:
 * ```
 * @file:JvmName("Utils")
 * package com.meowool.cloak.example
 * import com.meowool.cloak.example.UUID
 *
 * val ID: UUID = generateUUID(base = 100)
 *
 * fun main() {
 *   val uuid: Any by Type("com.meowool.cloak.example.Utils").staticProperty(
 *     name = "ID",
 *     type = "com.meowool.cloak.example.UUID".type,
 *   )
 *
 *   println(uuid) // UUID(base = 100)
 * }
 * ```
 *
 * @receiver The parent type where the property to be reflected is located.
 * @param name The name of the property to be reflected, the name of the delegation source (caller) is used by default.
 * @param type The actual type of the property to be reflected, the default is [T] (that is: `T::class.type`).
 *
 * @author 凛 (RinOrz)
 */
@CompileOnly inline fun <reified T : Any> Type<*>.staticProperty(
  name: String = compilerImplementation(),
  type: Type<T> = compilerImplementation(),
): CloakPropertyReflection<T> = compilerImplementation()
