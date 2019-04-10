package chapter4

/**
 * 4.1.1
 */
//코틀린 인터페이스 안에는 추상 메소드뿐 아니라 구현이 있는 메소드도 정의할 수 있다.
interface Clickable {
    fun click()

    //디폴트 구현이 있는 메소드
    fun showOff() = println("I'm clickable!")
}

class Button: Clickable {
    override fun click() = println("I was clicked")
}

class Button2: Clickable, Focusable {
    override fun showOff() {
        super<Clickable>.showOff()
        super<Focusable>.showOff()
    }

    override fun click() = println("I was clicked 2")

}
//동일한 메소드를 구현하는 다른 인터페이스 정의
interface Focusable {
    fun setFocus(b: Boolean) =
            println("I ${if (b) "got" else "lost"} focus")

    //Clickable 과 같은 showOff 메소드를 가지고 있다.
    //같이 구현할 경우 컴파일 에러가 발생한다.
    //-> 상위 타입의 구현을 호출할 때는 자바와 마찬가지로 super 을 사용한다.
    fun showOff() = println ("I'm focusable!")
}

fun main() {
    //Button().click()
    //Button().showOff()

    Button2().click()
    Button2().setFocus(false)
    Button2().showOff()
}

/**
 *
 * 4.1.2 open, final, abstract 변경자: 기본적으로 final
 * 취약한 기반 클래스라는 문제는 하위 클래스가 기반 클래스에 대해 가졌던 기반 클래스를 변경함으로써 꺠져버린 경우에 생긴다.
 * 어떤 클래스가 자신을 상속하는 방법에 대해 정확한 규칙을 제공하지 않는다면 그 클래스의 클라이언트는 기반 클래스를 작성한 사람의 의도와 다른 방식으로 메소드를 오버라이드할 위험이 있다.
 *
 * 자바의 클래스와 메소드는 기본적으로 상속에 대해 열려있지만 코틀린의 클래스와 메소드는 기본적으로 final 이다.
 *
 * final -> 오버라이드할 수 없음
 * open -> 오버라이드할 수 있음
 * abstract -> 반드시 오버라이드해야 함
 * override -> 상위 클래스나 상위 인스턴스의 멤버를 오버라이드하는 중
 */

/**
 *
 * 4.1.3 가시성 변경자: 기본적으로 공개
 *
 * public -> 모든 곳에서 볼 수 있다.
 * internal -> 같은 모듈 안에서만 볼 수 있다.
 * protected -> 하위 클래스 안에서만 볼 수 있다.
 * private -> 같은 클래스 안에서만 볼 수 있다.
 */

/**
 * 4.1.4 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스
 *
 * 중첨 클래스는 기본적으로 내부 클래스가 아니다. 바깥쪽 클래스에 대한 참조를 중첩 클래스 안에 포함시키려면 inner 키워드를
 * 중첩 클래스 선언 앞에 붙여서 내부 클래스로 만들어야 한다.
 */
class Outer {

    inner class inner {
        //내부 클래스 안에서 바깥쪽 클래스 참조에 전급하려면 this@Outer 라고 써야 한다.
        fun getOuterReference(): Outer = this@Outer
    }
}

/**
 * 4.1.5 봉인된 클래스: 클래스 계층 정의 시 계층 확장 제한
 *
 * 상위 클래스에 sealed 변경자를 붙이면 그 상위 클래스를 상속한 하위 클래스 정의를 제한할 수 있다.
 * sealed 클래스의 하위 클래스를 정의할 때는 반드시 상위 클래스 안에 중첩시켜야 한다.
 */
sealed class Expr {
    class Num(val value:Int) : Expr()

    class Sum(val left: Expr, val right: Expr) : Expr()
}

fun eval(e: Expr) : Int =
        when(e) {
            //when 식이 모든 하위 클래스를 검사하므로 별도의 else 분기가 없어도 된다.
            is Expr.Num -> e.value
            is Expr.Sum -> eval(e.right) + eval(e.left)
        }





