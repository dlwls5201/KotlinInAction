package chapter3

import java.lang.StringBuilder

/**
 *  코틀린에서 컬렉션 만들기
 */

// 함수를 호출하기 쉽게 만들기
fun <T> joinToString(
    collection: Collection<T>,
    separator: String,
    prefix: String,
    postfix: String
): String {
    val result = StringBuilder(prefix)
    for((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

//디폴트 파라미터 값을 사용해 joinToString() 정의하기
fun <T> joinToString2(
    collection: Collection<T>,
    separator: String = "",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

fun main() {
    val list = listOf(1,2,3)
    println(joinToString(list, "; ", "(", ")"))

    //이름 붙인 인자
    println(joinToString(list, separator = "; ", prefix = "(", postfix = ")"))

}

