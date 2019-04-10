package chapter3

//확장 함수는 오버라이드할 수 없다.
open class View {
    open fun click() = println("View clicked")
}

class Button: View() {
    override fun click() = println("Button clicked")
}

fun View.showOff() = println("I'm a view!")

fun Button.showOff() = println("I'm a button!")

fun main() {
    //println("Kotlin".lastChar())

    val list = listOf(1,2,3)
    //println(list.joinToString())

    val view: View = Button()
    view.click()
    view.showOff()

    val viewButton = Button()
    viewButton.click()
    viewButton.showOff()

}