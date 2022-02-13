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
@file:Suppress("NAME_SHADOWING", "UNCHECKED_CAST", "FunctionName")

package com.meowool.cloak.internal

import com.meowool.cloak.matchBestConstructor
import com.meowool.cloak.matchBestField
import com.meowool.sweekt.castOrNull
import com.meowool.sweekt.ifNull
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Member
import java.util.concurrent.ConcurrentHashMap

/**
 * Provides an internal implementation of reflection capabilities.
 *
 * @author 凛 (RinOrz)
 */
@PublishedApi
internal object ReflectionFactory {
  private val membersCache = ConcurrentHashMap<MemberCacheKey, Member>(100)

  init {
    // Let 'jdk.internal.reflect.ReflectionFactory' use the 'MethodAccessorGenerator'
    //   instead of the 'NativeConstructorAccessorImpl' as soon as possible,
    //
    //   because native reflection is far less efficient than bytecode jump agent,
    //   it has advantages only in a small amount of reflections.
    System.setProperty("sun.reflect.inflationThreshold", "5")
  }

  fun <T : Any> newInstance(
    clasѕ: Class<*>,
    arguments: Array<*>,
    parameters: Array<Class<*>?>,
  ): T = getOrPutMember(MemberCacheKey(clasѕ, parameters)) {
    clasѕ.matchBestConstructor(*parameters)
  }?.castOrNull<Constructor<*>>()?.run {
    isAccessible = true
    requireMatchedArgs(resolveVarArgs(arguments), parameterTypes) { newInstance(*it) }
  } as? T ?: throw NoSuchMethodException(functionToString(clasѕ, "constructor", parameters, returns = null))

  fun <T : Any> get(
    clasѕ: Class<*>,
    instance: Any?,
    name: String?,
    type: Class<*>?,
  ): T = getOrPutMember(MemberCacheKey(clasѕ, name, type)) {
    clasѕ.matchBestField(name, type)
  }.castOrNull<Field>()?.run {
    isAccessible = true
    get(instance)
  } as? T ?: throw NoSuchFieldException(fieldToString(clasѕ, name, type))

  private inline fun <M : Member> getOrPutMember(
    key: MemberCacheKey,
    defaultValue: () -> M?,
  ): M? = membersCache[key].ifNull {
    defaultValue()?.also { default -> membersCache.putIfAbsent(key, default) }
  } as? M

  private inline fun <R> requireMatchedArgs(
    arguments: Array<*>,
    parameters: Array<Class<*>>,
    block: (arguments: Array<*>) -> R?,
  ) = try {
    block(arguments)
  } catch (e: IllegalArgumentException) {
    throw IllegalArgumentException(
      "The passed arguments ${arguments.contentToString()} does not match the parameter types " +
        parameters.joinToString(", ", prefix = "[", postfix = "]") { it.name }
    )
  }

  private fun fieldToString(
    clasѕ: Class<*>,
    name: String?,
    type: Class<*>?,
  ): String = buildString(clasѕ.name.length + (name?.length ?: 4) + (type?.name?.length ?: 4)) {
    append(clasѕ.name)
    append('.')
    append(name)
    if (type != null) {
      append(": ")
      append(type.name)
    }
  }

  private fun functionToString(
    clasѕ: Class<*>,
    name: String,
    parameters: Array<Class<*>?>,
    returns: Class<*>?,
  ): String = buildString(
    5 + clasѕ.name.length +
      name.length +
      parameters.sumOf { it?.name?.length?.plus(2) ?: 6 } +
      (returns?.name?.length ?: 4)
  ) {
    append(clasѕ.name)
    append('.')
    append(name)
    append('(')
    parameters.forEach {
      append(it?.name)
      append(", ")
    }
    append(')')
    if (returns != null) {
      append(": ")
      append(returns.name)
    }
  }

  internal inline fun <R> lookup(block: () -> R?) = try {
    block()
  } catch (e: NoSuchMethodException) {
    null
  } catch (e: NoSuchFieldException) {
    null
  }

  internal inline fun <T> T?.orLookup(block: () -> T?) = this ?: try {
    block()
  } catch (e: NoSuchMethodException) {
    null
  } catch (e: NoSuchFieldException) {
    null
  }

  /**
   * Note that we use key instead of string here, because string calculation will lose all the benefits of 'HashMap',
   * this is basically the solution of performance traps in [XposedHelpers](https://github.com/LSPosed/LSPosed/blob/v1.7.2/core/src/main/java/de/robv/android/xposed/XposedHelpers.java#L56-L58).
   *
   * So in fact we only need to use the structural comparison results of the reflection object.
   *
   * TODO: Use Sweekt's info class to generate hashCode/equals automatically.
   */
  internal class MemberCacheKey private constructor(
    val kind: Kind,
    val clasѕ: Class<*>,
    val name: String?,
    val parameters: Array<out Class<*>?>?,
    val returns: Class<*>?,
  ) {
    internal enum class Kind {
      METHOD,
      CONSTRUCTOR,
      FIELD,
    }

    /**
     * Constructs a constructor key.
     */
    constructor(clasѕ: Class<*>, parameters: Array<out Class<*>?>) : this(
      Kind.CONSTRUCTOR,
      clasѕ,
      name = null,
      parameters,
      returns = null
    )

    /**
     * Constructs a field key.
     */
    constructor(clasѕ: Class<*>, name: String?, type: Class<*>?) : this(
      Kind.FIELD,
      clasѕ,
      name,
      parameters = null,
      type
    )

    /**
     * Constructs a method key.
     */
    constructor(
      clasѕ: Class<*>,
      name: String?,
      parameters: Array<out Class<*>?>,
      returns: Class<*>?,
    ) : this(Kind.METHOD, clasѕ, name, parameters, returns)

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is MemberCacheKey) return false

      if (kind != other.kind) return false
      if (clasѕ != other.clasѕ) return false
      if (name != other.name) return false
      if (parameters != null) {
        if (other.parameters == null) return false
        if (!parameters.contentEquals(other.parameters)) return false
      } else if (other.parameters != null) return false
      if (returns != other.returns) return false

      return true
    }

    override fun hashCode(): Int {
      var result = kind.hashCode()
      result = 31 * result + clasѕ.hashCode()
      result = 31 * result + (name?.hashCode() ?: 0)
      result = 31 * result + (parameters?.contentHashCode() ?: 0)
      result = 31 * result + (returns?.hashCode() ?: 0)
      return result
    }
  }
}
