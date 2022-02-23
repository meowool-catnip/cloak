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
package com.meowool.cloak.case

open class FieldsParent {
  private val intField = 1
  var baseBooleanField = false
  var baseBooleanObjectField: Boolean? = null
  open val baseStringField = "5"
}

class FieldsContainer : FieldsParent() {
  val intField = 100
  var booleanField = true
  val interfaceField: Animal? = null
  val interfaceLowField: Organism? = null
}

object StaticFieldsContainer {
  val ImmutablePublic: Any = "public"
  internal val ImmutableInternal: Any = "internal"
  private val ImmutablePrivate: Any = "private"

  var MutablePublic: Any = "public"
  internal var MutableInternal: Any = "internal"
  private var MutablePrivate: Any = "private"

  var isStatic: Boolean = true
}
