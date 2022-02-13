package builtin

import org.junit.jupiter.api.Test
import java.io.File
import java.io.Serializable
import java.util.Observer
import java.util.Vector

/**
 * @author 凛 (RinOrz)
 */
class JvmTypesGenerator : TypesGenerator(Platform.Jvm) {
  @Test fun generateJdkTypes(): Unit = generateTypes(
    "Jdk",
    Any::class,
    Void::class,
    Throwable::class,
    Enum::class,

    CharSequence::class,
    Number::class,
    Byte::class,
    Short::class,
    Int::class,
    Long::class,
    Float::class,
    Double::class,

    Boolean::class,
    Char::class,
    String::class,

    ByteArray::class,
    Array<Byte>::class,
    ShortArray::class,
    Array<Short>::class,
    IntArray::class,
    Array<Int>::class,
    LongArray::class,
    Array<Long>::class,
    FloatArray::class,
    Array<Float>::class,
    DoubleArray::class,
    Array<Double>::class,
    BooleanArray::class,
    Array<Boolean>::class,
    CharArray::class,
    Array<Char>::class,
    Array<String>::class,

    RandomAccess::class,
    Comparable::class,
    Serializable::class,
    Cloneable::class,

    Iterable::class,
    Iterator::class,
    ListIterator::class,
    Collection::class,
    List::class,
    Set::class,
    Map::class,
    Map.Entry::class,

    HashSet::class,
    HashMap::class,
    ArrayList::class,
    LinkedHashMap::class,
    LinkedHashSet::class,

    Observer::class,
    File::class,
    Vector::class,
  )
}