package chapter2

/**
 * 수에 대한 이터레이션: 범위와 수열
 */

//when을 사용해 피즈버즈 게임 구현하기
fun fizzBuzz(i: Int) = when {
    i % 15 == 0 -> "FizzBuzz"
    i % 3 == 0 -> "Fizz"
    i % 5 == 0 -> "Buzz"
    else -> "$i"
}

fun main() {
    //1 ~ 100 까지 수헹
    for(i in 1..100) {
        //println(fizzBuzz(i))
    }

    //역순으로 2씩 증가
    for(i in 100 downTo 1 step 2) {
        //println(fizzBuzz(i))
    }

    //100을 포함하지 않는경우 until 사용
    for(i in 1 until 100) {
        //println(fizzBuzz(i))
    }
}