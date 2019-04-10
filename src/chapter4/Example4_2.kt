package chapter4

import javax.naming.Context
import javax.swing.text.AttributeSet

/**
 * 4.2.1 클래스 초기화: 주 생성자와 초기화 블록
 */
//클래스 이름 뒤에 오는 괄호로 둘러싸인 코드를 주 생성자라고 부른다.
class User(val nickname: String)

//constructor은 주 생성자나 부 생성자 정의를 시작할 때 사용한다.
//init 키워드는 초기화 블록을 시작한다.
//초기화 블록에는 클래스의 객체가 만들어질 때 실행될 초기화 코드가 들어간다.
class User2 constructor(_nickname: String) {
    val nickname: String

    init {
        nickname = _nickname
    }
}

//클래스를 상속한 하위 클래스느 반드시 상위 클래스의 생성자를 호출해야 한다.
//이 규칙으로 인해 기반 클래스의 이름 뒤에는 꼭 빈 괄호가 들어간다(물론 생성자 인자가 있다면 괄호 안에 인다가 들어간다)
//반면 인터페이스는 생성자가 없기 때문에 어떤 클래스가 인터페이스를 구현하는 경우 그 클래스의 상위 클래스 목록에 있는 인터페이스 목록에서 이름 뒤에 괄호가 붙었는지
//살펴보면 쉽게 기반 클래스와 인터페이스를 구별할 수 있다.

/**
 * 4.2.2 부 생성자: 상위 클래스를 다른 방식으로 초기화
 */
//인자에 대한 디폴트 값을 제공하기 ㅜ이해 부 생성자를 여럿 만들지 말라. 대신 파라미터의 디폴트 값을 생성자 시그니처에 직접 명시하라
open class View {
    constructor(ctx: Context) {

    }
    constructor(ctx: Context, attr: AttributeSet) {

    }
}

class MyButton : View {
    constructor(ctx: Context) : super(ctx) {

    }

    constructor(ctx: Context, attr: AttributeSet) : super(ctx, attr) {

    }
}

/**
 *  4.2.3 인터페이스에 선언된 프로퍼티 구현
 */

interface User3 {
    val nickname: String
}


