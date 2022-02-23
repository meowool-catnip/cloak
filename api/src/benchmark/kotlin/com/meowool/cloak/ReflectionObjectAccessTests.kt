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
import java.lang.reflect.Member
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * Benchmark                              (useConcurrentHashMap)   Mode  Cnt      Score       Error   Units
 * --------------------------------------------------------------------------------------------------------
 * filterDeclaredMethodDirectly                             true  thrpt    5   9743.435 ±  3150.265  ops/ms
 * filterDeclaredMethodDirectly                            false  thrpt    5   9496.195 ±  2231.011  ops/ms
 * filterDeclaredMethodWithCache                            true  thrpt    5  44323.817 ±  7230.033  ops/ms
 * filterDeclaredMethodWithCache                           false  thrpt    5  19327.989 ±  6040.781  ops/ms
 * filterDeclaredMethodWithStringCache                      true  thrpt    5   5992.899 ±   515.225  ops/ms
 * filterDeclaredMethodWithStringCache                     false  thrpt    5   5673.335 ±   849.686  ops/ms
 * --------------------------------------------------------------------------------------------------------
 * getDeclaredConstructorDirectly                           true  thrpt    5  38587.439 ±  5363.130  ops/ms
 * getDeclaredConstructorDirectly                          false  thrpt    5  38204.169 ±  4026.448  ops/ms
 * getDeclaredConstructorWithCache                          true  thrpt    5  43941.816 ±  7403.931  ops/ms
 * getDeclaredConstructorWithCache                         false  thrpt    5  47575.009 ± 11822.117  ops/ms
 * getDeclaredConstructorWithStringCache                    true  thrpt    5   2679.211 ±   334.335  ops/ms
 * getDeclaredConstructorWithStringCache                   false  thrpt    5   2444.858 ±   829.690  ops/ms
 * --------------------------------------------------------------------------------------------------------
 * getDeclaredFieldDirectly                                 true  thrpt    5  41429.840 ±  3722.272  ops/ms
 * getDeclaredFieldDirectly                                false  thrpt    5  41218.756 ± 12568.078  ops/ms
 * getDeclaredFieldWithCache                                true  thrpt    5  39348.196 ±  2902.307  ops/ms
 * getDeclaredFieldWithCache                               false  thrpt    5  40037.868 ±   646.407  ops/ms
 * getDeclaredFieldWithStringCache                          true  thrpt    5   9191.624 ±    16.188  ops/ms
 * getDeclaredFieldWithStringCache                         false  thrpt    5  10240.912 ±    26.364  ops/ms
 * --------------------------------------------------------------------------------------------------------
 * getDeclaredMethodDirectly                                true  thrpt    5  18378.404 ±   174.204  ops/ms
 * getDeclaredMethodDirectly                               false  thrpt    5  18385.413 ±   169.897  ops/ms
 * getDeclaredMethodWithCache                               true  thrpt    5  24015.300 ±   172.085  ops/ms
 * getDeclaredMethodWithCache                              false  thrpt    5  25206.794 ±   134.474  ops/ms
 * getDeclaredMethodWithStringCache                         true  thrpt    5   6123.770 ±    22.751  ops/ms
 * getDeclaredMethodWithStringCache                        false  thrpt    5   5891.994 ±    26.947  ops/ms
 *
 * @author RinOrz
 */
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.Throughput)
open class ReflectionObjectAccessTests {
  @Param("true", "false")
  var useConcurrentHashMap: Boolean = false

  private lateinit var membersCache: MutableMap<MemberCacheKey, Member?>
  private lateinit var membersStringCache: MutableMap<String, Member?>

  @Setup fun init() {
    if (useConcurrentHashMap) {
      membersCache = ConcurrentHashMap()
      membersStringCache = ConcurrentHashMap()
    } else {
      membersCache = HashMap()
      membersStringCache = HashMap()
    }
  }

  @Benchmark fun getDeclaredConstructorDirectly() {
    Case::class.java.getDeclaredConstructor(Int::class.java, Boolean::class.javaObjectType)
    Case::class.java.getDeclaredConstructor(String::class.java, Array<Cloneable>::class.java)
  }

  @Benchmark fun getDeclaredConstructorWithCache() {
    fun reflect(vararg params: Class<*>) = membersCache.getOrPut(MemberCacheKey.Constructor(Case::class.java, params)) {
      Case::class.java.getDeclaredConstructor(*params)
    }

    reflect(Int::class.java, Boolean::class.javaObjectType)
    reflect(String::class.java, Array<Cloneable>::class.java)
  }

  @Benchmark fun getDeclaredConstructorWithStringCache() {
    fun reflect(vararg params: Class<*>) =
      membersStringCache.getOrPut(Case::class.java.name + params.contentToString()) {
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
      membersCache.getOrPut(MemberCacheKey.Method(Case::class.java, name, params, null)) {
        Case::class.java.getDeclaredMethod(name, *params)
      }

    reflect("getA", Int::class.java)
    reflect("getB")
  }

  @Benchmark fun getDeclaredMethodWithStringCache() {
    fun reflect(name: String, vararg params: Class<*>) =
      membersStringCache.getOrPut(Case::class.java.name + name + params.contentToString()) {
        Case::class.java.getDeclaredMethod(name, *params)
      }

    reflect("getA", Int::class.java)
    reflect("getB")
  }

  @Benchmark fun getDeclaredFieldDirectly() {
    Case::class.java.getDeclaredField("a")
    Case::class.java.getDeclaredField("b")
  }

  @Benchmark fun getDeclaredFieldWithCache() {
    fun reflect(name: String) =
      membersCache.getOrPut(MemberCacheKey.Field(Case::class.java, name, null)) {
        Case::class.java.getDeclaredField(name)
      }

    reflect("a")
    reflect("b")
  }

  @Benchmark fun getDeclaredFieldWithStringCache() {
    fun reflect(name: String) = membersStringCache.getOrPut(Case::class.java.name + name) {
      Case::class.java.getDeclaredField(name)
    }

    reflect("a")
    reflect("b")
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
      membersCache.getOrPut(MemberCacheKey.Method(Case::class.java, name, params, null)) {
        Case::class.java.declaredMethods.first {
          it.name == name && it.parameterTypes.contentEquals(params)
        }
      }

    reflect("getA", Int::class.java)
    reflect("getB")
  }

  @Benchmark fun filterDeclaredMethodWithStringCache() {
    fun reflect(name: String, vararg params: Class<*>) =
      membersStringCache.getOrPut(Case::class.java.name + name + params.contentToString()) {
        Case::class.java.declaredMethods.first {
          it.name == name && it.parameterTypes.contentEquals(params)
        }
      }

    reflect("getA", Int::class.java)
    reflect("getB")
  }

  @Suppress("unused", "UNUSED_PARAMETER")
  private class Case {
    private val a: Int = 0
    private val b: Array<Cloneable> = emptyArray()

    constructor(a: Int, b: Boolean?)
    constructor(a: String, b: Array<Cloneable>)

    fun getA(plus: Int): Int = 0 + plus
    fun getB(): Boolean = false
  }
}
