package chapter2

/**
 * in 으로 컬렉션이나 범위의 원소 검사
 */

fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'

fun main() {

    println(isLetter('A'))
}