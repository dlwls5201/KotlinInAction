package chapter9

//반공변
//반공변 클래스의 하위 타입 관계는 공변 클래스의 경우와 반대다
//Comparator 인터페이스의 메소드는 T 타입의 값을 소비하기만 한다. 이는 T가 인 위치에서만 쓰인다는 뜻이다.
//in 이라는 키워드는 그 키워드가 붙은 타입이 이 클래스의 메소드 안으로 전달돼 메소드에 의해 소비된다는 뜻이다.
interface Comparator<in T> {
    fun compare(e1: T, e2: T): Int
}

fun main() {

    val anyComparator = Comparator<Any> {
            e1, e2 -> e1.hashCode() - e2.hashCode()
    }

    //Any 는 String 의 상위 타입이지만 Comparator<Any>가 Comparator<String>의 하위 타입이 되므로 반공변성이라 할 수 있다.
    listOf<String>().sortedWith(anyComparator)
}