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
import com.meowool.sweekt.ifNull
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

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
 * @author RinOrz
 */
fun <T> Class<T>.matchBestConstructor(vararg parameters: Class<*>?): Constructor<T>? = ReflectionFactory.lookup {
  getDeclaredConstructor(*parameters)
}.orLookup {
  // Depth matching constructors, the closest result wins
  var closest: Constructor<*>? = null
  var closestDistance = MatchingDistance.Mismatch
  declaredConstructors.forEach {
    val currentDistance = calculateDistanceBetween(
      passed = parameters,
      declared = it.parameterTypes,
      hasVararg = it.isVarArgs
    )
    // Current constructor is closer, so it's better
    if (currentDistance.isMatched && (closest == null || currentDistance < closestDistance)) {
      closest = it
      closestDistance = currentDistance
    }
  }
  closest
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
 * @author RinOrz
 */
fun <T> Class<T>.matchBestField(name: String?, type: Class<*>?): Field? {
  require(name != null || type != null) { "Either name or type must be specified." }

  fun Class<*>.lookupField() = ReflectionFactory
    .lookup { name?.let(::getDeclaredField) }
    .orLookup { name?.let(::getField) }

  return lookupField()?.takeIf { field ->
    // Ignore type
    if (type == null) return@takeIf true
    type canCastTo field.type
  }.orLookup {
    // Depth matching fields, the closest result wins
    var closest: Field? = null
    var closestDistance = MatchingDistance.Mismatch
    var currentClass: Class<*>? = this
    while (currentClass != null) {
      when (type) {
        // Ignore type, use the fastest direct implementation
        null -> currentClass.lookupField()?.also { return it }

        else -> currentClass.declaredFields.forEach {
          // Skip current field if it's name mismatched
          if (name != null && name != it.name) return@forEach
          val currentDistance = calculateDistanceBetween(passed = type, declared = it.type, depth = true)
          // Current field is lighter, so it's better
          if (currentDistance.isMatched && (closest == null || currentDistance < closestDistance)) {
            closest = it
            closestDistance = currentDistance
          }
        }
      }
      // For performance reasons,
      //   the super class will continue to be searched only when no matching field is found in current class.
      if (closest == null) currentClass = currentClass.superclass else break
    }
    closest
  }
}

/**
 * Returns a field that best matches the given signatures.
 *
 * For example:
 * ```
 * interface Organism
 * abstract class Animal
 * class Cat : Animal(), Organism
 *
 * class Zoo {
 *   fun init(): Result<Any>
 *   fun getSameKind(organism: Organism): Organism
 * }
 *
 * // fun init(): Result<Any>
 * Zoo::class.java.matchBestMethod(null, null, returns = Result::class.java)
 * Zoo::class.java.matchBestMethod("init")
 *
 * // fun getSameKind(organism: Organism): Organism
 * Zoo::class.java.matchBestField("getSameKind", Cat::class.java)
 * Zoo::class.java.matchBestField("getSameKind", Cat::class.java, returns = Animal::class.java)
 * ```
 *
 * @author RinOrz
 */
fun <T> Class<T>.matchBestMethod(name: String?, vararg parameters: Class<*>?, returns: Class<*>?): Method? {
  val method = name?.let {
    ReflectionFactory.lookup { getDeclaredMethod(it, *parameters) }
      .orLookup { getMethod(it, *parameters) }
      ?.takeIf { method ->
        // Ignore type
        if (returns == null) return@takeIf true
        returns canCastTo method.returnType
      }
  }

  return method.ifNull {
    // Depth matching methods, the closest result wins
    var closest: Method? = null
    var closestDistance = MatchingDistance.Mismatch
    var currentClass: Class<*>? = this
    while (currentClass != null) {
      currentClass.declaredMethods.forEach {
        // Skip current field if it's name mismatched
        if (name != null && name != it.name) return@forEach
        var currentDistance = calculateDistanceBetween(
          passed = parameters,
          declared = it.parameterTypes,
          hasVararg = it.isVarArgs
        )
        if (returns != null) {
          currentDistance += calculateDistanceBetween(
            passed = returns,
            declared = it.returnType,
            depth = true
          )
        }
        // Current method is lighter, so it's better
        if (currentDistance.isMatched && (closest == null || currentDistance < closestDistance)) {
          closest = it
          closestDistance = currentDistance
        }
      }
      // For performance reasons,
      //   the super class will continue to be searched only when no matching method is found in current class.
      if (closest == null) currentClass = currentClass.superclass else break
    }
    closest
  }
}
