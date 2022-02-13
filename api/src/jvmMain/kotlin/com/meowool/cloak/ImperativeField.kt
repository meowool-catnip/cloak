package com.meowool.cloak

import com.meowool.cloak.internal.ReflectionFactory
import com.meowool.cloak.internal.compilerImplementation

/**
 * Gets (reflects) the value of the field in this [Any] instance.
 *
 * Note that if there is no field matching the signature in this class, the instance is created by reflecting the
 * inherited class(es) and interface(s) of this class.
 *
 * @receiver An instance of the class where the field to be got is located (aka. field's parent class).
 * @param name The name of the field to get.
 * @param type The value type of the field to get, the default is [T].
 * @param parent The class type where the field to get is located, the type of the receiver instance by default.
 *
 * @author 凛 (RinOrz)
 */
inline fun <reified T : Any> Any.get(
  name: String,
  type: Type<T>? = Type(T::class.java),
  parent: Type<*> = this.javaClass.type,
): T = ReflectionFactory.get(clasѕ = parent.javaClass, instance = this, name, type?.javaClass)

/**
 * Sets (reflects) the [value] to the field in this [Type].
 *
 * @receiver A class type where the field to be set is located (aka. field's parent class).
 * @param name The name of the field to set.
 * @param value The new value to be set to the static field.
 * @param type The value type of the field to set, the default is [T].
 * @param parent The class type where the field to set is located, the type of the receiver instance by default.
 *
 * @author 凛 (RinOrz)
 */
inline fun <reified T : Any> Any.set(
  name: String,
  value: T?,
  type: Type<T>? = Type(T::class.java),
  parent: Type<*> = this.javaClass.type,
): T = compilerImplementation()

/**
 * Gets (reflects) the value of the static field in this [Type].
 *
 * @receiver A class type where the static field to be got is located (aka. field's parent class).
 * @param name The name of the static field to get.
 * @param type The value type of the static field to get, the default is [T].
 *
 * @author 凛 (RinOrz)
 */
inline fun <reified T : Any> Type<*>.getStatic(
  name: String,
  type: Type<T>? = Type(T::class.java),
): T = ReflectionFactory.get(clasѕ = this.javaClass, instance = null, name, type?.javaClass)

/**
 * Sets (reflects) the [value] to the static field in this [Type].
 *
 * @receiver A class type where the static field to be set is located (aka. field's parent class).
 * @param name The name of the static field to set.
 * @param value The new value to be set to the static field.
 * @param type The value type of the static field to set, the default is [T].
 *
 * @author 凛 (RinOrz)
 */
inline fun <reified T : Any> Type<*>.setStatic(
  name: String,
  value: T?,
  type: Type<T>? = Type(T::class.java),
): T = compilerImplementation()