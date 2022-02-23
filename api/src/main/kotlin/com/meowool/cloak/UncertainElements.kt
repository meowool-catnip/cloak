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
 * Creates a type "placeholder" to temporarily represent an uncertain type of reflection.
 *
 * Note that the "placeholder" should be replaced with the real type at run time (or earlier), this replacement step is
 * usually done by some library (such as [conan](https://github.com/meowool-catnip/conan)) or yourself.
 *
 * TODO Example
 *
 * @param id The identifier used to distinguish when replacing, which should be unique in application.
 * @author RinOrz
 */
@CompileOnly fun <T : Any> uncertainType(id: String): Type<T> = compilerImplementation()

/**
 * Creates a type "placeholder" to temporarily represent an uncertain type of reflection.
 *
 * Note that the "placeholder" should be replaced with the real type at run time (or earlier), this replacement step is
 * usually done by some library (such as [conan](https://github.com/meowool-catnip/conan)) or yourself.
 *
 * TODO Example
 *
 * @param id The identifier used to distinguish when replacing, which should be unique in application.
 * @author RinOrz
 */
@JvmName("uncertainAnyType")
@CompileOnly fun uncertainType(id: String): Type<Any> = compilerImplementation()

/**
 * Creates a name "placeholder" to use to reflect the uncertain field.
 *
 * Note that the "placeholder" should be replaced with the real name at run time (or earlier), this replacement step is
 * usually done by some library (such as [conan](https://github.com/meowool-catnip/conan)) or yourself.
 *
 * TODO Example
 *
 * @param id The identifier used to distinguish when replacing, which should be unique in reflection context.
 *   suppose want to reflect a field with an uncertain name, the id can be the class name of the field plus the
 *   possible field name.
 *
 * @author RinOrz
 *
 * @see get::name
 * @see set::name
 * @see field::name
 */
@CompileOnly fun uncertainFieldName(id: String): String = compilerImplementation()

/**
 * Creates a name "placeholder" to use to reflect the uncertain method.
 *
 * Note that the "placeholder" should be replaced with the real name at run time (or earlier), this replacement step is
 * usually done by some library (such as [conan](https://github.com/meowool-catnip/conan)) or yourself.
 *
 * TODO Example
 *
 * @param id The identifier used to distinguish in the future replacement, which should be unique in reflection context.
 *   suppose want to reflect a method with an uncertain name, the id can be the class name of the method plus the
 *   possible method name.
 *
 * @author RinOrz
 *
 * @see call::name
 * @see methodCall::name
 */
@CompileOnly fun uncertainMethodName(id: String): String = compilerImplementation()

/**
 * Create a list "placeholder" of parameter types to use to reflect the uncertain method.
 *
 * Note that the "placeholder" should be replaced with the real list of parameters types at run time (or earlier),
 * this replacement step is usually done by some library (such as [conan](https://github.com/meowool-catnip/conan))
 * or yourself.
 *
 * TODO Example
 *
 * @param id The identifier used to distinguish in the future replacement, which should be unique in reflection context.
 *   suppose want to reflect a method with an uncertain parameter type list, the id can be the class name of the method
 *   plus the possible method name.
 * @param possibleTypes Multiple entries of possible types, should be replaced with an accurate parameter type list
 *   according to their keys in the future. See [String.be].
 *
 * @author RinOrz
 *
 * @see call::parameters
 * @see methodCall::parameters
 * @see constructorNew::parameters
 */
@CompileOnly fun uncertainParameters(
  id: String,
  vararg possibleTypes: UncertainEntry<Type<*>?>
): Array<Type<*>> = compilerImplementation()

/**
 * Create a list "placeholder" of argument values to use to reflect the uncertain method.
 *
 * Note that the "placeholder" should be replaced with the real list of parameters types at run time (or earlier),
 * this replacement step is usually done by some library (such as [conan](https://github.com/meowool-catnip/conan))
 * or yourself.
 *
 * TODO Example
 *
 * @param id The identifier used to distinguish in the future replacement, which should be unique in reflection context.
 *   suppose want to reflect a method with an uncertain parameter type list, the id can be the class name of the method
 *   plus the possible method name.
 *   Some entries should be replaced with an accurate list according to their keys in the future
 * @param possibleArguments Multiple entries of possible argument values, should be replaced with an accurate argument
 *   list according to their keys in the future. See [String.be].
 *
 * @author RinOrz
 *
 * @see call::arguments
 * @see methodCall::arguments
 * @see constructorNew::arguments
 */
@CompileOnly fun uncertainArguments(
  id: String,
  vararg possibleArguments: UncertainEntry<*>
): Array<*> = compilerImplementation()

/**
 * Represents an uncertain entry, which is constituted of a 'key' and a 'value' to distinguish the real value according
 * to the 'key' of this entry during replacement.
 *
 * This entry is very similar to the [Pair], but this is not a real object, this will be inline at compile time.
 *
 * Note that this entry can only be constructed by the [String.be] function.
 *
 * @param E Represents a generic of a [Type] or nullable value.
 * @author RinOrz
 */
@CompileOnly class UncertainEntry<E> @PublishedApi internal constructor()

/**
 * Creates an [UncertainEntry] as a possible element of an uncertain type list or parameter list.
 *
 * Note that this function can only be used by [uncertainParameters] or [uncertainArguments] function.
 *
 * TODO Example
 *
 * @receiver A key that distinguishes between different values during replacement.
 * @param value A nullable value represents a possible type or a possible argument value.
 * @author RinOrz
 */
@CompileOnly infix fun <E> String.be(value: E?): UncertainEntry<E> = compilerImplementation()
