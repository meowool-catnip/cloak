@file:Suppress("SpellCheckingInspection", "UNCHECKED_CAST", "JAVA_CLASS_ON_COMPANION")

package builtin

import com.meowool.sweekt.castOrNull
import com.meowool.sweekt.datetime.currentYear
import com.meowool.sweekt.toJvmQualifiedTypeName
import com.squareup.kotlinpoet.ANNOTATION
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.ARRAY
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BOOLEAN_ARRAY
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.BYTE_ARRAY
import com.squareup.kotlinpoet.CHAR
import com.squareup.kotlinpoet.CHAR_ARRAY
import com.squareup.kotlinpoet.CHAR_SEQUENCE
import com.squareup.kotlinpoet.COLLECTION
import com.squareup.kotlinpoet.COMPARABLE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.DOUBLE_ARRAY
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.ENUM
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.FLOAT_ARRAY
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.INT_ARRAY
import com.squareup.kotlinpoet.ITERABLE
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.LONG_ARRAY
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.MAP_ENTRY
import com.squareup.kotlinpoet.NUMBER
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.SET
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.SHORT_ARRAY
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.THROWABLE
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import java.io.File
import kotlin.reflect.KClass
import com.squareup.kotlinpoet.CodeBlock.Companion.of as codeOf

/**
 * @author 凛 (RinOrz)
 */
open class TypesGenerator(private val platform: Platform) {

  protected fun generateTypes(fileName: String, vararg types: Any) {
    val file = FileSpec.builder(Package, fileName).addImport("com.meowool.cloak", "type").apply {
      types.forEach {
        val pair = it.castOrNull<Pair<Any, String>>()
        val customName = pair?.second

        when (val type = it as? KClass<*> ?: pair?.first!!) {
          is KClass<*> -> {
            val primitiveType = type.javaPrimitiveType
            if (primitiveType != null) {
              val typeName = primitiveType.javaTypeToKotlinType()
              addProperty(
                name = customName ?: primitiveType.name.substringAfterLast('.'),
                type = typeName,
                initializer = codeOf("%T::class.javaPrimitiveType!!.type", typeName),
                kdoc = codeOf("Represents the primitive type of [%T] class.", typeName)
              )
            }

            val objectType = if (primitiveType == null) type.java else type.javaObjectType
            val typeName = objectType.javaTypeToKotlinType()
            addProperty(
              name = customName ?: objectType.name.substringAfterLast('.'),
              type = getParameterizedTypeName(objectType, typeName),
              initializer = when (primitiveType) {
                null -> codeOf("%T::class.java.type", typeName)
                else -> codeOf("%T::class.javaObjectType.type", typeName)
              },
              kdoc = when (typeName) {
                is ParameterizedTypeName -> codeOf(
                  "Represents the array type of [%T] class.",
                  objectType.componentType.javaTypeToKotlinType()
                )
                else -> codeOf("Represents the type of [%T] class.", typeName)
              }
            )
          }
          is String -> {
            addProperty(
              name = type.substringAfter('.'),
              type = ANY,
              initializer = codeOf("%S.type", type),
              kdoc = codeOf("Represents the type of '%S' class.", type)
            )
          }
          else -> throw IllegalArgumentException("Unsupported type: $type")
        }
      }
    }.build()

    val sourceCode = """
        /*
         * Copyright (c) $currentYear. The Meowool Organization Open Source Project
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
         
        @file:JvmMultifileClass
        @file:JvmName("BuiltInTypes")
        @file:Suppress("ObjectPropertyName", "SpellCheckingInspection", "DEPRECATION")
        
        /*
         * This file is automatically generated by [meowool-cloak](https://github.com/meowool-catnip/cloak).
         */
      """.trimIndent() + "\n" + file.toString().replace("@LazyInit\npublic", "@LazyInit")

    File(javaClass.getResource("/${platform.path}")!!.readText())
      .resolve(Package.replace('.', '/'))
      .resolve(file.name + ".kt")
      .also { println("Generated to " + it.absolutePath) }
      .writeText(sourceCode)
  }

  private fun getParameterizedTypeName(type: Class<*>, typeName: TypeName) =
    when (val typeParams = type.typeParameters.size) {
      0 -> typeName
      else -> typeName.castOrNull<ClassName>()?.parameterizedBy(List(typeParams) { STAR }) ?: typeName
    }

  @OptIn(DelicateKotlinPoetApi::class)
  private fun Class<*>.javaTypeToKotlinType(): TypeName = when (this) {
    Void.TYPE, Void::class.java -> ClassName("java.lang", "Void")
    Cloneable::class.javaObjectType -> ClassName("kotlin", "Cloneable")
    CharSequence::class.javaObjectType -> CHAR_SEQUENCE
    Number::class.javaObjectType -> NUMBER
    Iterable::class.javaObjectType -> ITERABLE
    Iterator::class.javaObjectType -> Iterator::class.asClassName()
    ListIterator::class.javaObjectType -> ListIterator::class.asClassName()
    Comparable::class.javaObjectType -> COMPARABLE
    Annotation::class.javaObjectType -> ANNOTATION
    Throwable::class.javaObjectType -> THROWABLE
    Enum::class.javaObjectType -> ENUM
    Collection::class.javaObjectType -> COLLECTION
    List::class.javaObjectType -> LIST
    Set::class.javaObjectType -> SET
    Map::class.javaObjectType -> MAP
    Map.Entry::class.javaObjectType -> MAP_ENTRY
    Any::class.javaObjectType -> ANY
    Boolean::class.javaPrimitiveType, Boolean::class.javaObjectType -> BOOLEAN
    Byte::class.javaPrimitiveType, Byte::class.javaObjectType -> BYTE
    Short::class.javaPrimitiveType, Short::class.javaObjectType -> SHORT
    Int::class.javaPrimitiveType, Int::class.javaObjectType -> INT
    Long::class.javaPrimitiveType, Long::class.javaObjectType -> LONG
    Char::class.javaPrimitiveType, Char::class.javaObjectType -> CHAR
    Float::class.javaPrimitiveType, Float::class.javaObjectType -> FLOAT
    Double::class.javaPrimitiveType, Double::class.javaObjectType -> DOUBLE
    String::class.java -> STRING
    else -> when {
      isArray -> when (componentType) {
        Boolean::class.javaPrimitiveType -> BOOLEAN_ARRAY
        Byte::class.javaPrimitiveType -> BYTE_ARRAY
        Short::class.javaPrimitiveType -> SHORT_ARRAY
        Int::class.javaPrimitiveType -> INT_ARRAY
        Long::class.javaPrimitiveType -> LONG_ARRAY
        Char::class.javaPrimitiveType -> CHAR_ARRAY
        Float::class.javaPrimitiveType -> FLOAT_ARRAY
        Double::class.javaPrimitiveType -> DOUBLE_ARRAY
        else -> ARRAY.parameterizedBy(componentType.javaTypeToKotlinType())
      }
      else -> this.asClassName()
    }

  }

  private fun FileSpec.Builder.addProperty(name: String, type: TypeName, initializer: CodeBlock, kdoc: CodeBlock) {
    addProperty(
      PropertySpec.builder(
        name = "_" + name.replace("$", "_").toJvmQualifiedTypeName(canonical = true).replace("[]", "Array"),
        type = Type.parameterizedBy(type)
      ).addAnnotation(LazyInit).addKdoc(kdoc).initializer(initializer).build()
    )
  }

  enum class Platform(val path: String) {
    Jvm("jvm.src"),
    Android("android.src")
  }

  internal companion object {
    private const val Package = "com.meowool.cloak.builtin.types"
    private val LazyInit = ClassName("com.meowool.sweekt", "LazyInit")
    private val Type = ClassName("com.meowool.cloak", "Type")
  }
}