package chapter3


/**
 * 메소드를 다른 클래스에 추가: 확장 함수와 확장 프로퍼티
 */

//확장 함수는 어떤 클래스의 멤버 메소드인 것처럼 호출할 수 있지만 그 클래스의 밖에 선언된 함수다.
fun String.lastChar() : Char = get(length - 1)

fun <T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
) : String {
    val result = StringBuffer(prefix)

    for((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}

fun main() {
    println("Kotlin".lastChar())

    val list = listOf(1,2,3)
    println(list.joinToString())
}

