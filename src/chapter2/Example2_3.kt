package chapter2

import java.lang.IllegalArgumentException

/**
 * 선택 표현과 처리: enum과 when
 */
//프로퍼티와 메소드가 있는 enum 클래스 선언하기
enum class Color(
    val r:Int, val g:Int, val b:Int //상수 프로퍼티를 정의한다.
) {
    RED(200,0,0), ORANGE(255,165,0),
    YELLOW(255,255,0) , GREEN(0,255,0), BLUE(0,0,255),
    INDIGO(75,0,130), VIOLET(238,130,238); // enum 클래스 안에 메소드를 정의하는 경우 세미콜론을 사용한다.

    fun rgb() = (r * 256 + g) * 256 + b
}

//자바와 달리 분기 끝에 break를 넣지 않아도 된다.
fun getMnemonic(color: Color) =
    when(color) {
        Color.RED ->  "Richard"
        Color.ORANGE -> "Of"
        Color.YELLOW -> "York"
        Color.GREEN -> "Gave"
        Color.BLUE -> "Battle"
        Color.INDIGO -> "In"
        Color.VIOLET -> "Vain"
    }

//when의 분기 조건에 여러 다른 객체 사용하기
fun mix(c1: Color, c2: Color) =
    when(setOf(c1, c2)) {
        setOf(Color.RED, Color.YELLOW) -> Color.ORANGE
        setOf(Color.YELLOW, Color.BLUE) -> Color.GREEN
        else -> throw Exception("Dirty color")
    }

//인자가 없는 when
//mix 함수 보다 가독성은 떨어지나 Set 인스턴스를 매번 생성할 필요가 없어 성능은 더 좋다.
fun mixOptimized(c1: Color, c2: Color) =
        when {
            (c1 == Color.RED && c2 ==Color.YELLOW)
                    || (c1 == Color.YELLOW && c2 == Color.RED) -> Color.ORANGE

            (c1 == Color.YELLOW && c2 ==Color.BLUE)
                    || (c1 == Color.BLUE && c2 == Color.YELLOW) -> Color.GREEN

            else -> throw Exception("Dirty color")
        }

//스마트 캐스트: 타입 검사와 타입 캐스트를 조합
interface Expr

class Num(val value: Int) : Expr

class Sum(val left: Expr, val right: Expr) : Expr

fun eval(e: Expr) : Int =
        when(e) {
            is Num ->
                e.value
            is Sum ->
                eval(e.left) + eval(e.right)
            else ->
                throw IllegalArgumentException("UnKnown expression")
        }

//블록의 마지막 식이 블록의 결과
fun evalWithLogging(e: Expr) : Int =
        when(e) {
            is Num -> {
                println("num: ${e.value}")
                e.value
            }
            is Sum -> {
                val left = evalWithLogging(e.left)
                val right = evalWithLogging(e.right)
                println("sum: $left + $right")
                left + right
            }
            else ->
                throw IllegalArgumentException("UnKnown expression")
        }



fun main() {
    //println(Color.BLUE.rgb())

    //println(getMnemonic(Color.BLUE))

    //println(mix(Color.BLUE, Color.YELLOW))

    //println(mixOptimized(Color.BLUE, Color.YELLOW))

    //println(evalWithLogging(Sum(Sum(Num(1), Num(2)), Num(4))))

}