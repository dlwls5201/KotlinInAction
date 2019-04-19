package chapter2

/**
 * in 으로 컬렉션이나 범위의 원소 검사
 */
fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'


/**
 * when 에서 in 사용하기
 */
fun recognize(c: Char) = when (c) {
    in '0'..'9' -> "It's a digit!"
    in 'a'..'z' -> "It's a letter!"
    else -> "I don't know"
}


fun main() {

    println(recognize('A'))
    println(isLetter('A'))
}