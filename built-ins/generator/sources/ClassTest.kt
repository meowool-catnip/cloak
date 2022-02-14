@file:Suppress("SpellCheckingInspection")

import com.meowool.sweekt.runOrNull
import com.squareup.kotlinpoet.asClassName
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * @author 凛 (RinOrz)
 */
class ClassTest {
  @Test fun testNames() {
    runOrNull { Class.forName("Lvoid;") } shouldBe null
    runOrNull { Class.forName("void") } shouldBe null
    runOrNull { Class.forName("V") } shouldBe null
    runOrNull { Class.forName("Ljava.lang.Void;") } shouldBe null
    runOrNull { Class.forName("java.lang.Void") } shouldBe Void::class.java
    Array<String>::class.java.name shouldBe "[Ljava.lang.String;"
    Array<String>::class.java.canonicalName shouldBe "java.lang.String[]"
    ByteArray::class.simpleName shouldBe "ByteArray"
    Void.TYPE.name shouldBe "void"
    Array<Void>::class.java.name shouldBe "[Ljava.lang.Void;"
    Void::class.java.toString() shouldBe "class java.lang.Void"
    Void::class.javaObjectType.name shouldBe Void::class.java.name
    Void::class.javaPrimitiveType?.name shouldBe "void"
    Long::class.javaObjectType.name shouldBe "java.lang.Long"
    Long::class.javaPrimitiveType?.name shouldBe "long"
    Any::class.javaObjectType.name shouldBe "java.lang.Object"
    Long::class.javaObjectType.typeParameters.size shouldBe 0
    List::class.javaObjectType.typeParameters.size shouldBe 1
    Map::class.javaObjectType.typeParameters.size shouldBe 2
    Map::class.java.name shouldBe "java.util.Map"
    Map.Entry::class.java.name shouldBe "java.util.Map\$Entry"
    Iterator::class.javaObjectType.name shouldBe "java.util.Iterator"
    ListIterator::class.javaObjectType.name shouldBe "java.util.ListIterator"
    MutableListIterator::class.javaObjectType.name shouldBe "java.util.ListIterator"
    MutableIterable::class.javaObjectType.name shouldBe "java.lang.Iterable"
    MutableIterator::class.javaObjectType.name shouldBe "java.util.Iterator"
    ListIterator::class.asClassName().reflectionName() shouldBe "kotlin.collections.ListIterator"
    Throwable::class.javaObjectType.name shouldBe "java.lang.Throwable"
    Nothing::class.javaObjectType.name shouldBe Void::class.java.name
    Annotation::class.javaObjectType.name shouldBe java.lang.annotation.Annotation::class.java.name
    Array<String>::class.java.canonicalName shouldBe "java.lang.String[]"
    Outer.Inner.Nested::class.java.name.substringAfterLast('.') shouldBe "ClassTest\$Outer\$Inner\$Nested"
  }

  private infix fun <T> T.shouldBe(other: T) = Assertions.assertEquals(this, other)

  private interface Outer {
    interface Inner {
      interface Nested
    }
  }
}