package chapter3

import java.lang.StringBuilder

/**
 * 메소드를 다른 클래스에 추가: 확장 함수와 확장 프로퍼티
 */

//기존 자바 API를 재작성하지 않고도 코틀린이 제공하는 여러 펴니한 기능을 할 수 있는 개녑이 확장 함수 이다.
//확장 함수는 어떤 클래스의 멤버 메소드인 것처럼 호출할 수 있지만 그 클래스의 밖에 선언된 함수다.

//어떤 문자열의 마지막 문자를 돌려주는 메소드
fun String.lastChar(): Char = this.get(this.length - 1)

//joinToString()를 확장으로 정의하기
fun <T> Collection<T>.joinToString3(
    separator: String = "",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for((index, element) in withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

//확장함수는 오버라이드 할 수 없다.
open class View {
    open fun click() = println("View clicked")
}

class Button: View() {
    override fun click() = println("Button clicked")
}

fun View.showOff() = println("I'm a view!")

fun Button.showOff() = println("I'm a button!")

fun main() {

    //어떤 문자열의 마지막 문자를 돌려주는 메소드
    println("Kotlin".lastChar())

    //joinToString()를 확장으로 정의하기
    val list = listOf(1,2,3)
    println(list.joinToString3())

    //확장함수는 오버라이드 할 수 없다.
    val view: View = Button()
    view.click()
    view.showOff()

    val viewButton = Button()
    viewButton.click()
    viewButton.showOff()

}