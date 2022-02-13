@file:Suppress("unused", "UNUSED_PARAMETER", "HasPlatformType")

package com.meowool.cloak.case


interface Organism
abstract class Animal
class Cat : Animal(), Organism {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Cat) return false
    return true
  }

  override fun hashCode(): Int {
    return javaClass.hashCode()
  }
}

class Dog : Animal(), Organism {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Dog) return false
    return true
  }

  override fun hashCode(): Int {
    return javaClass.hashCode()
  }
}

class Rabbit : Animal(), Organism {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Rabbit) return false
    return true
  }

  override fun hashCode(): Int {
    return javaClass.hashCode()
  }
}
class Grass : Organism {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Grass) return false
    return true
  }

  override fun hashCode(): Int {
    return javaClass.hashCode()
  }
}
class Zoo {
  var data: Any? = null
  val animals = mutableListOf<Animal>()
  val organisms = mutableListOf<Organism>()
  val cats = mutableListOf<Cat>()
  val dogs = mutableListOf<Dog>()
  val extra = mutableListOf<Int>()

  constructor(open: Boolean) {
    data = open
  }
  constructor(id: Int) {
    data = id
  }
  constructor(id: Int, id2: Int) {
    data = "id:$id, id2:$id2"
  }
  constructor(nullableId: Int?, id2: Int) {
    data = "idObj:$nullableId, id2:$id2"
  }
  constructor(id: Int?, vararg extra: Int) {
    data = "idObj:$id, extra"
    this.extra.addAll(extra.toList())
  }
  constructor(id: Int?, vararg animals: Animal) {
    data = "idObj:$id, animals"
    this.animals.addAll(animals.toList())
  }
  constructor(name: String) {
    data = name
  }
  constructor(name: String, vararg animals: Animal) {
    data = name + "animals"
    this.animals.addAll(animals.toList())
  }
  constructor(name: String, vararg cats: Cat) {
    data = name + "cats"
    this.cats.addAll(cats.toList())
  }
  constructor(animal: Animal) {
    data = "Animal"
    animals += animal
  }
  constructor(organism: Organism) {
    data = "Organism"
    organisms += organism
  }
  constructor(cat: Cat) {
    data = "Cat"
    cats += cat
  }
  constructor(dog: Dog) {
    data = "Dog"
    dogs += dog
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Zoo) return false

    if (data != other.data) return false
    if (animals != other.animals) return false
    if (organisms != other.organisms) return false
    if (cats != other.cats) return false
    if (dogs != other.dogs) return false
    if (extra != other.extra) return false

    return true
  }

  override fun hashCode(): Int {
    var result = data?.hashCode() ?: 0
    result = 31 * result + animals.hashCode()
    result = 31 * result + organisms.hashCode()
    result = 31 * result + cats.hashCode()
    result = 31 * result + dogs.hashCode()
    result = 31 * result + extra.hashCode()
    return result
  }

  companion object {
    val BOOLEAN = Zoo::class.java.getDeclaredConstructor(Boolean::class.java)
    val INT = Zoo::class.java.getDeclaredConstructor(Int::class.java)
    val INT_OBJ_VARARG_Int = Zoo::class.java.getDeclaredConstructor(
      Int::class.javaObjectType,
      IntArray::class.java
    )
    val INT_OBJ_VARARG_ANIMAL = Zoo::class.java.getDeclaredConstructor(
      Int::class.javaObjectType,
      Array<Animal>::class.java
    )
    val STRING = Zoo::class.java.getDeclaredConstructor(String::class.java)
    val STRING_VARARG_ANIMAL = Zoo::class.java.getDeclaredConstructor(
      String::class.javaObjectType,
      Array<Animal>::class.java
    )
    val STRING_VARARG_CAT = Zoo::class.java.getDeclaredConstructor(
      String::class.javaObjectType,
      Array<Cat>::class.java
    )
    val ORGANISM = Zoo::class.java.getDeclaredConstructor(Organism::class.java)
    val ANIMAL = Zoo::class.java.getDeclaredConstructor(Animal::class.java)
    val CAT = Zoo::class.java.getDeclaredConstructor(Cat::class.java)
    val DOG = Zoo::class.java.getDeclaredConstructor(Dog::class.java)
  }
}