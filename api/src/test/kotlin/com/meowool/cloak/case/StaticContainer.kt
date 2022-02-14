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