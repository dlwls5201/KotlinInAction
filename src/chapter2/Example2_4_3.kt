package chapter2

import java.util.*

/**
 * 맵에 대한 이터레이션
 */

//맵을 초기화하고 이터레이션하기
val binaryReps = TreeMap<Char, String>() //키에 대해 정렬하기 위해 TreeMap 사용

fun main() {

    for(c in 'A'..'F') {
        val binary = Integer.toBinaryString(c.toInt())
        binaryReps[c] = binary
    }

    for ((letter, binary) in binaryReps) {
        println("$letter = $binary")
    }

    //맵에 사용했던 구조 분해 구문을 맵이 아닌 컬렉션에도 활용할 수 있다.
    val list = arrayListOf("10", "11", "1001")
    for((index, element) in list.withIndex()) {
        println("$index : $element")
    }
}

