package chapter8

import java.lang.StringBuilder

/**
 * 고차 함수 정의
 */
//함수타입
val sum1 = { x: Int, y: Int -> x + y}

val action1 = { println(42) }

val sum2: (Int, Int) -> Int = { x, y -> x + y}

val action2: () -> Unit = { println(42) }

val canReturnNull: (Int, Int) -> Int? =  { _, _ -> null }

var funOrNull: ((Int, Int) -> Int)? = null

//인자로 받은 함수 호출
private fun twoAndThree(operation: (Int, Int) -> Int) {
    val result = operation(2, 3)
    println("The result is $result")
}

private fun String.filter(predicate: (Char) -> Boolean) : String {
    val sb = StringBuilder()

    for(index in 0 until length) {
        val element = get(index)
        if(predicate(element)) sb.append(element)
    }
    return sb.toString()
}

//함수를 반환하는 함수 정의하기 예제 1
enum class Delivery { STANDARD, EXPEDITED}

class Order(val itemCount: Int)

fun getShippingCostCalculator(
    delivery: Delivery
): (Order) -> Double { // 함수를 반환하는 함수를 선언한다.
    if(delivery == Delivery.EXPEDITED) {
        return { order -> 6 + 2.1 * order.itemCount }
    }
    return { order -> 1.2 * order.itemCount }
}

//함수를 반환하는 함수 정의하기 예제 2
data class Person(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String?
)

class ContactListFilters {
    var prefix: String = ""
    var onlyWithPhoneNumber: Boolean = false

    fun getPredicate(): (Person) -> Boolean { //함수를 반환하는 함수를 정의한다.
        val startsWithPrefix = { p: Person ->
            p.firstName.startsWith(prefix) || p.lastName.startsWith(prefix)
        }
        if(!onlyWithPhoneNumber) {
            return startsWithPrefix // 함수 타입의 변수를 반환한다.
        }
        return { startsWithPrefix(it) && it.phoneNumber != null } // 람다를 반환한다.
    }
}

//람다를 활용한 중복 제거
data class SiteVisit(
    val path: String,
    val duration: Double,
    val os: OS
)

enum class OS { WINDOW, LINUX, MAC, IOS, ANDROID}

//이제 맥 사용자에 대해 같은 통계를 구하고 싶다. 중복을 피하기 위해 OS를 파라미터로 뽑아 낼 수 있다.
fun List<SiteVisit>.averageDurationFor(os: OS) =
        filter { it.os == os }.map(SiteVisit::duration).average()

//평균을 구하기 위한 고차함수
fun List<SiteVisit>.averageDurationFor2(predicate: (SiteVisit) -> Boolean) =
        filter(predicate).map(SiteVisit::duration).average()

fun main() {
    //twoAndThree { a, b -> a + b }
    //twoAndThree { a, b -> a * b }

    //println("ab1c".filter { it in 'a'..'z' })

    //val calculator = getShippingCostCalculator(Delivery.EXPEDITED)
    //println("Shipping costs ${calculator(Order(3))}")

    /*val contacts = listOf(Person("Dmitry", "Jemerov", "123-4567"),
        Person("Svetlana", "Isakova", null))

    val contactListFilters = ContactListFilters()

    with(contactListFilters) {
        prefix = "Dm"
        onlyWithPhoneNumber = true
    }

    println(contacts.filter (contactListFilters.getPredicate()))*/

    val log = listOf(
        SiteVisit("/", 34.0, OS.WINDOW),
        SiteVisit("/", 22.0, OS.MAC),
        SiteVisit("/login", 12.0, OS.WINDOW),
        SiteVisit("/sinup", 8.0, OS.IOS),
        SiteVisit("/", 16.3, OS.ANDROID)
    )

    //윈도우 사용자의 평균 방문 시간을 출력하고 싶다.
    val averageWindowDuration = log
        .filter { it.os == OS.WINDOW }
        .map(SiteVisit::duration)
        .average()

    println(averageWindowDuration)

    //MAC 사용자의 평균 방문 시간을 출력하고 싶다.
    println(log.averageDurationFor(OS.MAC))

    //모바일 디바이스 사용자의 평균 방문 시간을 구하고 싶다면 어떻게 해야 할까?
    val averageMobileDuration = log
        .filter { it.os in setOf(OS.IOS, OS.ANDROID) }
        .map(SiteVisit::duration)
        .average()

    println(averageMobileDuration)

    //평균을 구하기 위한 고차함수
    println(log.averageDurationFor2 { it.os in setOf(OS.IOS, OS.ANDROID) })
}