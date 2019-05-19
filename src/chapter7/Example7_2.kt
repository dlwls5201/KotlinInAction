package chapter7

/**
 * 비교 연산자 오버로딩
 */
//동등성 연산자 equals
class MyPoint(val x: Int, val y: Int) {
    override fun equals(obj: Any?): Boolean {
        if (obj === this) return true //파라미터가 this와 같은 객체인지
        if(obj !is MyPoint) return false // 파리미터 타입 검사
        return obj.x == x && obj.y == y
    }
}

//순서 연산자 compareTo
class Person(
    val firstName: String, val lastName: String
): Comparable<Person> {
    override fun compareTo(other: Person): Int {
        return compareValuesBy(this, other, Person::lastName, Person::firstName)
    }
}

fun main() {

    //println(MyPoint(10,20) == MyPoint(10, 20))
}