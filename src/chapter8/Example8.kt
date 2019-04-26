package chapter8

import java.lang.StringBuilder

val sum1 = { x: Int, y: Int -> x + y}

val action1 = { println(42) }

val sum2: (Int, Int) -> Int = { x, y -> x + y}

val action2: () -> Unit = { println(42) }

val canReturnNull: (Int, Int) -> Int? =  { _, _ -> null }

var funOrNull: ((Int, Int) -> Int)? = null


private fun twoAndThree(operation: (Int, Int) -> Int) {
    val result = operation(2, 3)
    println("The result is $result")
}

private fun String.filter(predicate: (Char) -> Boolean) : String {
    val sb = StringBuilder()

    for(index in 0 until length) {
        val element = get(index)
        if(predicate(element)) sb.append(element)
    }
    return sb.toString()
}

fun main() {
    twoAndThree { a, b -> a + b}
    twoAndThree { a, b -> a * b}

    println("ab1c".filter { it in 'a'..'z' }) // 함다를 predicate 파라미터로 전달한다.
}