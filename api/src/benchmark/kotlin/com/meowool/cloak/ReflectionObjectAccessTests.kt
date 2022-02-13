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

import com.meowool.cloak.internal.ReflectionFactory.MemberCacheKey
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import java.lang.reflect.Executable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * Benchmark                    (useConcurrentHashMap)   Mode  Cnt      Score       Error   Units
 * ----------------------------------------------------------------------------------------------
 * getDeclaredConstructorDirectly                 true  thrpt    5  37209.979 ±   401.071  ops/ms
 * getDeclaredConstructorDirectly                false  thrpt    5  38145.765 ±   394.824  ops/ms
 * getDeclaredConstructorWithCache                true  thrpt    5  36286.185 ±  1130.006  ops/ms
 * getDeclaredConstructorWithCache               false  thrpt    5  39875.199 ±  2538.135  ops/ms
 * getDeclaredConstructorWithStringCache          true  thrpt    5   2526.219 ±   351.689  ops/ms
 * getDeclaredConstructorWithStringCache         false  thrpt    5   2303.449 ±   579.402  ops/ms
 * ----------------------------------------------------------------------------------------------
 * getDeclaredMethodDirectly                      true  thrpt    5  16149.131 ±  6194.534  ops/ms
 * getDeclaredMethodDirectly                     false  thrpt    5  14066.167 ±  8493.408  ops/ms
 * getDeclaredMethodWithCache                     true  thrpt    5  31582.741 ± 13345.444  ops/ms
 * getDeclaredMethodWithCache                    false  thrpt    5  40936.915 ±  6288.993  ops/ms
 * getDeclaredMethodWithStringCache               true  thrpt    5   6073.838 ±    11.078  ops/ms
 * getDeclaredMethodWithStringCache              false  thrpt    5   5825.380 ±   131.154  ops/ms
 * ----------------------------------------------------------------------------------------------
 * filterDeclaredMethodDirectly                   true  thrpt    5   9377.032 ±   923.373  ops/ms
 * filterDeclaredMethodDirectly                  false  thrpt    5   9191.327 ±  2075.706  ops/ms
 * filterDeclaredMethodWithCache                  true  thrpt    5  44113.463 ±  6446.010  ops/ms
 * filterDeclaredMethodWithCache                 false  thrpt    5  41403.127 ±  5742.620  ops/ms
 * filterDeclaredMethodWithStringCache            true  thrpt    5   5953.205 ±   355.962  ops/ms
 * filterDeclaredMethodWithStringCache           false  thrpt    5   5691.164 ±   823.349  ops/ms
 *
 * @author 凛 (RinOrz)
 */
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.Throughput)
open class ReflectionObjectAccessTests {
  @Param("true", "false")
  var useConcurrentHashMap: Boolean = false

  private lateinit var functionsCache: MutableMap<MemberCacheKey, Executable?>
  private lateinit var functionsStringCache: MutableMap<String, Executable?>

  @Setup fun init() {
    if (useConcurrentHashMap) {
      functionsCache = ConcurrentHashMap()
      functionsStringCache = ConcurrentHashMap()
    } else {
      functionsCache = HashMap()
      functionsStringCache = HashMap()
    }
  }

  @Benchmark fun getDeclaredConstructorDirectly() {
    Case::class.java.getDeclaredConstructor(Int::class.java, Boolean::class.javaObjectType)
    Case::class.java.getDeclaredConstructor(String::class.java, Array<Cloneable>::class.java)
  }

  @Benchmark fun getDeclaredConstructorWithCache() {
    fun reflect(vararg params: Class<*>) = functionsCache.getOrPut(MemberCacheKey(Case::class.java, params)) {
      Case::class.java.getDeclaredConstructor(*params)
    }

    reflect(Int::class.java, Boolean::class.javaObjectType)
    reflect(String::class.java, Array<Cloneable>::class.java)
  }

  @Benchmark fun getDeclaredConstructorWithStringCache() {
    fun reflect(vararg params: Class<*>) =
      functionsStringCache.getOrPut(Case::class.java.name + params.contentToString()) {
        Case::class.java.getDeclaredConstructor(*params)
      }

    reflect(Int::class.java, Boolean::class.javaObjectType)
    reflect(String::class.java, Array<Cloneable>::class.java)
  }

  @Benchmark fun getDeclaredMethodDirectly() {
    Case::class.java.getDeclaredMethod("getA", Int::class.java)
    Case::class.java.getDeclaredMethod("getB")
  }

  @Benchmark fun getDeclaredMethodWithCache() {
    fun reflect(name: String, vararg params: Class<*>) =
      functionsCache.getOrPut(MemberCacheKey(Case::class.java, name, params, null)) {
        Case::class.java.getDeclaredMethod(name, *params)
      }

    reflect("getA", Int::class.java)
    reflect("getB")
  }

  @Benchmark fun getDeclaredMethodWithStringCache() {
    fun reflect(name: String, vararg params: Class<*>) =
      functionsStringCache.getOrPut(Case::class.java.name + name + params.contentToString()) {
        Case::class.java.getDeclaredMethod(name, *params)
      }

    reflect("getA", Int::class.java)
    reflect("getB")
  }

  @Benchmark fun filterDeclaredMethodDirectly() {
    fun reflect(name: String, vararg params: Class<*>) = Case::class.java.declaredMethods.first {
      it.name == name && it.parameterTypes.contentEquals(params)
    }

    reflect("getA", Int::class.java)
    reflect("getB")
  }

  @Benchmark fun filterDeclaredMethodWithCache() {
    fun reflect(name: String, vararg params: Class<*>) =
      functionsCache.getOrPut(MemberCacheKey(Case::class.java, name, params, null)) {
        Case::class.java.declaredMethods.first {
          it.name == name && it.parameterTypes.contentEquals(params)
        }
      }

    reflect("getA", Int::class.java)
    reflect("getB")
  }

  @Benchmark fun filterDeclaredMethodWithStringCache() {
    fun reflect(name: String, vararg params: Class<*>) =
      functionsStringCache.getOrPut(Case::class.java.name + name + params.contentToString()) {
        Case::class.java.declaredMethods.first {
          it.name == name && it.parameterTypes.contentEquals(params)
        }
      }

    reflect("getA", Int::class.java)
    reflect("getB")
  }

  @Suppress("unused", "UNUSED_PARAMETER")
  private class Case {
    constructor(a: Int, b: Boolean?)
    constructor(a: String, b: Array<Cloneable>)

    fun getA(plus: Int): Int = 0 + plus
    fun getB(): Boolean = false
  }
}
