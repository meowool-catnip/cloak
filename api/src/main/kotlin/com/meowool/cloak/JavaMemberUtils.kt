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
@file:Suppress("UNCHECKED_CAST")

package com.meowool.cloak

import com.meowool.cloak.internal.MatchingDistance
import com.meowool.cloak.internal.MatchingDistance.isMatched
import com.meowool.cloak.internal.ReflectionFactory
import com.meowool.cloak.internal.ReflectionFactory.orLookup
import com.meowool.cloak.internal.calculateDistanceBetween
import java.lang.reflect.Constructor
import java.lang.reflect.Field

/**
 * Returns a constructor that best matches the given signatures.
 *
 * This function largely refers to [commons-lang3](https://github.com/apache/commons-lang/blob/master/src/main/java/org/apache/commons/lang3/reflect/ConstructorUtils.java#L240),
 * but considers more details.
 *
 * For example:
 * ```
 * interface Organism
 * abstract class Animal
 * class Cat : Animal(), Organism
 * class Dog : Animal(), Organism
 * class Grass : Organism
 *
 * class Zoo {
 *   constructor(id: Int, vararg animals: Animal)
 *   constructor(id: Int, vararg organisms: Organism)
 *   constructor(name: String, vararg animals: Animal)
 *   constructor(name: String, vararg cats: Cat)
 * }
 *
 * // constructor(id: Int, vararg organisms: Organism)
 * Zoo::class.java.matchBestConstructor(Int::class.java, Grass::class.java)
 *
 * // constructor(id: Int, vararg animals: Animal)
 * Zoo::class.java.matchBestConstructor(Long::class.java, null)
 * Zoo::class.java.matchBestConstructor(Int::class.java, Grass::class.java)
 * Zoo::class.java.matchBestConstructor(Double::class.java, Array<Cat>::class.java)
 * Zoo::class.java.matchBestConstructor(Float::class.java, Array<Animal>::class.java)
 *
 * // constructor(name: String, vararg animals: Animal)
 * Zoo::class.java.matchBestConstructor(String::class.java, Cat::class.java, Dog::class.java)
 * Zoo::class.java.matchBestConstructor(String::class.java, Dog::class.java, Dog::class.java)
 * Zoo::class.java.matchBestConstructor(String::class.java, Array<Dog>::class.java)
 *
 * // constructor(name: String, vararg cats: Cat)
 * Zoo::class.java.matchBestConstructor(String::class.java, Cat::class.java, Cat::class.java)
 * Zoo::class.java.matchBestConstructor(String::class.java, Array<Cat>::class.java)
 * ```
 *
 * @author 凛 (RinOrz)
 */
fun <T> Class<T>.matchBestConstructor(vararg parameters: Class<*>?): Constructor<T>? = ReflectionFactory.lookup {
  getDeclaredConstructor(*parameters)
}.orLookup {
  // Depth matching constructors, the lightest result wins
  var lightest: Constructor<*>? = null
  var lightestWeight = MatchingDistance.Mismatch
  declaredConstructors.forEach {
    val currentWeight = calculateDistanceBetween(
      passed = parameters,
      declared = it.parameterTypes,
      hasVararg = it.isVarArgs
    )
    // Current constructor is lighter, so it's better
    if (currentWeight.isMatched && (lightest == null || currentWeight < lightestWeight)) {
      lightest = it
      lightestWeight = currentWeight
    }
  }
  lightest
} as? Constructor<T>

/**
 * Returns a field that best matches the given signatures.
 *
 * For example:
 * ```
 * interface Organism
 * abstract class Animal
 * class Cat : Animal(), Organism
 * class Dog : Animal(), Organism
 *
 * class Habitat {
 *   val id: Int
 * }
 *
 * class Zoo : Habitat() {
 *   val animals: Array<Animal>
 *   val organisms: Array<Organism>
 * }
 *
 * // in Habitat: val id: Int
 * Zoo::class.java.matchBestField("id")
 *
 * // in Zoo: val organisms: Array<Organism>
 * Zoo::class.java.matchBestField("organisms")
 * Zoo::class.java.matchBestField("organisms", Array<Animal>::class.java)
 * Zoo::class.java.matchBestField(null, Array<Organism>::class.java)
 *
 * // in Zoo: val animals: Array<Animal>
 * Zoo::class.java.matchBestField(null, Array<Animal>::class.java)
 * ```
 *
 * @author 凛 (RinOrz)
 */
fun <T> Class<T>.matchBestField(name: String?, type: Class<*>?): Field? = ReflectionFactory
  .lookup { name?.let(::getDeclaredField) }
  .orLookup { name?.let(::getField) }
  .takeIf { it?.type == type }
  .orLookup {
    // Depth matching fields, the lightest result wins
    var lightest: Field? = null
    var lightestWeight = MatchingDistance.Mismatch
    var currentClass: Class<*>? = this
    while (currentClass != null) {
      currentClass.declaredFields.forEach {
        // Skip current field if it's name mismatched
        if (name != null && name != it.name) return@forEach
        val currentWeight = calculateDistanceBetween(passed = type, declared = it.type, depth = true)
        // Current field is lighter, so it's better
        if (currentWeight.isMatched && (lightest == null || currentWeight < lightestWeight)) {
          lightest = it
          lightestWeight = currentWeight
        }
      }
      // For performance reasons,
      //   the parent class will continue to be searched only when no matching field is found in current class.
      if (lightest == null) currentClass = currentClass.superclass else break
    }
    lightest
  }
