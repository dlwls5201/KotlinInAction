package chapter4

/**
 * 4.4 object 키워드: 클래스 선언과 인스턴스 생성
 *
 * 클래스를 정의하면서 동시에 인스턴스(객체)를 생성한다.
 */
//객체 선언은 싱글턴을 정의하는 방법 중 하나다
//동반 객체는 인스턴스 메소드느 아니지만 어떤 클래스와 관련 있는 메소드와 팩토리 메소드를 담을 때 쓰인다. 동반 객체 메소드에 접근할 때는 동반 객체가 포함된 클래스의 이름을 사용할 수있다.
//객체 식은 자바의 무명 내부 클래스 대신 쓰인다.

//4.4.1 객체 선언: 싱글턴을 쉽게 만들기

//4.4.2 동반 객체: 패토리 메소드와 정적 맴버가 들어갈 장소
class MyUser private constructor(val nickname: String) {
    companion object {
        fun newSubscribingUser(email: String) =
            MyUser(email.substringBefore("@"))
    }
}

fun main() {
    val subscribingUser = MyUser.newSubscribingUser("blackjin@tistory.com")
    println(subscribingUser.nickname)
}