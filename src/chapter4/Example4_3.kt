package chapter4

/**
 * 4.3 컴파일러가 생성한 메소드: 데이터 클래스와 클래스 위임
 *
 * 자바 플랫폼에서는 클래스가 equals, hashCode, toString 등의 메소드를 구현해야 한다.
 */
class Client(val name: String, val postalCode: Int) {
    override fun equals(other: Any?): Boolean {
        if(other == null || other !is Client)
            return false
        return name == other.name &&
                postalCode == other.postalCode
    }

    override fun toString() = "Client(name=$name, postalCode=$postalCode)"

    override fun hashCode() = name.hashCode() * 31 + postalCode

    fun copy(name: String = this.name,
             postalCode: Int = this.postalCode) = Client(name, postalCode)
}

//문자열 표현 : toString

//객체의 동등성 : equals()
//서로 다른 두 객체가 내부에 동일한 데이터를 포항히는 경우 그 둘을 동등한 객체로 간주해야 할 수 있다.

//해시 컨테이너: hashCode()
//자바에서는 equals 를 오버라이드할 때 반드시 hashCode 도 함게 오버라이드해야 한다.

/**
 * data class
 *
 * 인스턴스 간 비교를 위한 equals
 * HashMap 과 같은 해시 기반 컨테이너에서 키로 사용할 수 있는 hashCode
 * 클래스의 각 필드를 선언 순서대로 표시하는 문자열 표현을 만들어 주는 toString
 *
 * -> equals와 hashCode는 주생성자에 나열된 모든 프로퍼티를 고려해 만들어 진다. 이때 주 생성자 밖에 정의된 프로퍼티는 고려의 대상이 아니다.
 */

/**
 *  데이터 클래스와 불변성: copy() 메소드
 *
 *  데이터 클래스의 모든 프로퍼티를 읽기 전용으로 만들어서 데이터 클래스를 불변 클래스로 만들라고 권장한다.
 *  불변 객체를 사용하면 프로그램에 대해 훨씬 쉽게 추론할 수 있다. 특히 다중스레드 프로그램의 경우 이런 성질은 더 중요하다.
 *
 *  객체를 복사하면서 일부 프로퍼티를 바꿀 수 있게 해주는 copy 메소드 객체를 메모리상에서 직접 바꾸는 대신 복사본을 만드는 편이 더 낫다.
 *  복사본은 제거해도 프로그램에서 원본을 참조하는 다른 부분에 전혀 영향을 끼치지 않는다.
 */

fun main() {

    val client1 = Client("BlackJin", 123)
    val client2 = Client("BlackJin", 123)

    // 코틀린 에서 == 연산자는 참조 동일성을 검사하지 않고 객체의 동등성을 검사한다
    // 따라서 == 연상은 equals 를 호출하는 식으로 컴파일 된다.
    /**
     *  자바에서는 ==를 원시 타입과 참조 타입을 비교할 때 사용한다. 원시 타입의 경우 ==는 두 피연산자의 값이 같은지 비교한다.
     *  반면 참조 타입의 경우는 ==는 두 피연산자의 주소가 같은지를 비교한다.
     *  따라서 자바에서는 두 객체의 동등성을 알려면 equals 를 호출해야 한다.
     *
     *  코틀린에서는 == 연산자가 두 객체를 비교하는 기본적인 방법이다. == 는 내부적으로 equals 를 호출해서 객체를 비교하낟.
     *  따라서 클래스가 equals 를 오버라이드 하면 == 를 통해 안전하게 그 클래스의 인스턴스를 비교할 수 있다.
     *  참조 비교를 위해서는 === 연산자를 사용할 수 있다.
     */
    println(client1 == client2)
    println(client1 === client2)

    /**
     * 해시 컨테이너
     *
     * equals() true 를 반환하는 두 객체는 반드시 같은 hashCode() 를 반환해야 한다.
     * HashSet 은 원소를 비교할 때 비용을 줄이기 위해 먼저 객체의 해시 코드를 비교하고 해시 코드가 같은 경우에만 실제 값을 비교한다.
     */
    val processed = hashSetOf(client1)
    println(processed.contains(client2))

    /**
     * copy
     */
    val lee = Client("이계영", 4122)
    println(lee.copy(postalCode = 4000))
}


/**
 * 4.3.3 클래스 위임: by 키워드 사용
 *
 * 인터페이스를 구현할 때 by 키워드를 통해 그 인터페이스데 대한 구현을 다른 객체에 위임 중이라는 사실을 명시할 수 있다.
 */
class CountingSet<T> (
    val innerSet: MutableCollection<T> = HashSet<T>()
) : MutableCollection<T> by innerSet { //MutableCollection의 구현을 innerSet에게 위임한다.

    var objectsAdded = 0

    // add, addAll 이 두 메소드는 위임하지 않고 새로운 구현을 제공한다.
    override fun add(element: T): Boolean {
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        objectsAdded += elements.size
        return innerSet.addAll(elements)
    }
}