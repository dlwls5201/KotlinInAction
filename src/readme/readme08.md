# 고차 함수 : 파라미터와 반환 값으로 람다 사용

**8장에서 다루는 내용**
- 함수 타입
- 고차 함수와 코드를 구조화할 때 고차 함수를 사용하는 방법
- 인라인 함수
- 비로컬 return과 레이블
- 무명 함수

>람다를 인자로 받거나 반환하는 함수가 **고차 함수**이다. 고차 함수로 코드를 더 간결하게 다듬고 코드 중복을 없애고 더 나은 추상화를 구축할 수 있다.
또한 람다를 사용함에 따라 발생할 수 있는 성능상 구바 비용을 없애고 람다 안에서 더 유연하게 흐름을 제어할 수 있는 코틀린 특정인 인라인 함수에 대해 설명한다.

## 고차 함수 정의

고차 함수는 다른 함수를 인자로 받거나 함수를 반환하는 함수다.


### 함수 타입

람다를 인자로 받는 함수를 정의하려면 먼저 람다 인자의 타입을 어떻게 선언할 수 있는지 알아야한다.


```kotlin
val sum = { x: Int, y: Int -> x + y}

val action = { println(42) }
```
위 경우 컴파일러는 sum과 action이 함수 타입임을 추론한다. 이제는 각 변수에 구체적인 타입 선언을 추가하면 어떻게 되는지 살펴보자

```kotlin
//int 파라미터를 2개 받아서 int 값을 반환하는 함수
val sum: (Int, Int) -> Int = { x, y -> x + y}

//아무 인자도 받지 않고 아무 값도 반환하지 않는 함수
val action: () -> Unit = { println(42) }
```
함수 타입을 정의하려면 함수 파라미터의 타입을 괄호 안에 넣고, 그 뒤에 화살표를 추가한 다음, 함수의 반환 타입을 지정하면 된다.

**(파라미터타입) -> 반환타입**
**(int, String) -> Unit**

``kotlin
val canReturnNull: (Int, Int) -> Int? =  { _, _ -> null }

var funOrNull: ((Int, Int) -> Int)? = null
```

널이 될 수 있는 함수 타입 변수를 정의할 수도 있다. 다만 함수의 반환 타입이 아니라 함수 타입 전체가 널이 될 수 있는 타입임을 선언하기 위해
함수 타입을 괄호로 감싸고 그 뒤에 물음표를 붙여야만 한다.


### 인자로 받은 함수 호출
```kotlin
fun twoAndThree(operation: (Int, Int) -> Int) {
    val result = operation(2, 3)
    println("The result is $result")
}

//main
 twoAndThree { a, b -> a + b}
 twoAndThree { a, b -> a * b}
```
[The result is 5]
[The result is 6]


**filter 함수 구현**
```kotlin
fun String.filter(predicate: (Char) -> Boolean) : String {
    val sb = StringBuilder()

    for(index in 0 until length) {
        val element = get(index)
        if(predicate(element)) sb.append(element)
    }
    return sb.toString()
}

//main
println("ab1c".filter { it in 'a'..'z' })
```

### 함수를 함수에서 반환

함수가 함수를 반활할 필요가 있는 경우보다는 함수가 함수를 인자로 받아야 할 필요가 있는 경우가 훨씬 더 많다. 하지만 함수를 반환하는 함수도 여전히 유용하다.
프로그램의 상태나 다른 조건에 따라 달라질 수 있는 로직이 있다고 생각해보자. 예를 들어 사용자가 선택한 배송 수단에 따라 배송비를 계산하는 방법이 달라질 수 있다.

```kotlin
fun getShippingCostCalculator(
    delivery: Delivery
): (Order) -> Double { // 함수를 반환하는 함수를 선언한다.
    if(delivery == Delivery.EXPEDITED) {
        return { order -> 6 + 2.1 * order.itemCount }
    }
    return { order -> 1.2 * order.itemCount }
}

//main
val calculator = getShippingCostCalculator(Delivery.EXPEDITED)
println("Shipping costs ${calculator(Order(3))}")
```

연라처 관리 앱을 만드는 데 UI의 상태에 따라 어떤 연락처 정보를 표시할지 결정하는 앱을 만들어 보자.

연락처 목록 표시 로직과 연락처 필터링 UI를 분리하기 위해 연락처 목록을 필터링 하는 술어 함수를 만드는 함수를 정의할 수 있다. 이 술어 함수는
이름과 성의 접두사를 검사하고 필요하면 전화번호가 연락처에 있는지도 검사한다.

```kotlin
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
```

```kotlin
    val contacts = listOf(Person("Dmitry", "Jemerov", "123-4567"),
        Person("Svetlana", "Isakova", null))

    val contactListFilters = ContactListFilters()

    with(contactListFilters) {
        prefix = "Dm"
        onlyWithPhoneNumber = true
    }

    println(contacts.filter (contactListFilters.getPredicate()))
```

### 람다를 활용한 중복 제거

함수 타입과 람다 식은 재활용하기 좋은 코드를 만들 때 쓸 수 있는 훌륭한 도구다.

웹사이트 방문 기록을 분석하는 예를 살펴보자

```kotlin
data class SiteVisit(
    val path: String,
    val duration: Double,
    val os: OS
)

enum class OS { WINDOW, LINUX, MAC, IOS, ANDROID}
```

윈도우 사용자의 평균 방문 시간을 출력하고 싶다면 average 함수를 사용하면 쉽게 구할 수 있다.

```kotlin
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
```

이제 맥 사용자에 대해 같은 통계를 구하고 싶다. 중복을 피하기 위해 OS를 파라미터로 뽑아 낼 수 있다.

```kotlin
fun List<SiteVisit>.averageDurationFor(os: OS) =
        filter { it.os == os }.map(SiteVisit::duration).average()

//main
//MAC 사용자의 평균 방문 시간을 출력하고 싶다.
println(log.averageDurationFor(OS.MAC))
```

모바일 디바이스 사용자의 평균 방문 시간을 구하고 싶다면 어떻게 해야 할까?

```kotlin
val averageMobileDuration = log
        .filter { it.os in setOf(OS.IOS, OS.ANDROID) }
        .map(SiteVisit::duration)
        .average()
```

고차 함수를 사용해 중복을 제거해 보겠다.

```kotlin
fun List<SiteVisit>.averageDurationFor(predicate: (SiteVisit) -> Boolean) =
        filter(predicate).map(SiteVisit::duration).average()

//main
println(log.averageDurationFor { it.os in setOf(OS.IOS, OS.ANDROID) })
```
코드 중복을 줄일 때 함수 타입이 상당히 도움이 된다. 코드의 일부분을 복사해 붙여 넣고 싶은 경우가 깄다면 그 코드를 람다로 만들면 중복을 제거할 수 있을 것이다.
변수, 프로퍼티, 파라미터 등을 사용해 데이터의 중복을 없앨 수 있는 것처럼 람다를 사용하면 코드의 중복을 없앨 수 있다.


## 인라인 함수: 람다의 부가 비용 없애기

5장에서는 코틀린이 보통 람다를 무명 클래스로 컴파일하지만 그렇다고 람다 식을 사용할 때마다 새로운 클래스가 만들어지지는 않는다는 사실을 설명했고, 람다가 변수를 포획하면 람다가 생성되는 시점마다 새로운 무명 클래스 객체가 생긴다.
이런 경우 실행 시점에 무명 클래스 생성에 따른 부가 비용이 든다. 따라서 람다를 사용하는 구현은 똑같은 작업을 수행하는 일반 함수를 사용한 구현보다 덜 효율적이다.

inline 변경자를 어떤 함수에 붙이면 컴파일러는 그 함수를 호출하는 모든 문장을 함수 본문에 해당하는 바이트코드로 바꿔치기 해준다.

**인라인이 작동하는 방식**

함수를 호출하는 코드를 함수를 호출하는 바이트코드 대신에 함수 본문을 번역한 바이트 코드로 컴파일한다는 뜻이다.

## 요약

- 함수 타입을 사용해 함수에 대한 참조를 담는 변수나 파라미터나 반환 값을 만들 수 있다.
- 고차 함수는 다른 함수를 인자로 받거나 함수를 반환한다. 함수의 파라미터 타입이나 반환 타입으로 함수 타입을 사용하면 고차 함수를 선언할 수 있다.
- 인라인 함수를 컴파일할 때 컴파일러는 그 함수의 본분과 그 함수에게 전달된 람다의 본문을 컴파일한 바이트코드를 모든 함수 호출 지점에 삽입해준다. 이렇게 만들어지는 바이트코드는 람다를 활용할 인라인 함수 코드를 풀어서 집접 쓴 경우와 비됴할 때 아무 부가 비용이 들지 않는다.
- 고차 함수를 사용하면 컴포넌트를 이루는 각 부분의 코드를 더 잘 재사용할 수 있다. 또 고차 함수를 활용해 강력한 제네릭 라이브러리를 만들 수 있다.
- 인라인 함수에는 람다 안에 있는 return 문이 바깥쪽 함수를 반환시키는 넌로컬 return을 사용할 수 있다.
- 무명 함수는 람다 식을 대신할 수 있으며 return 식을 처리하는 규칙이 일반 람다 식과는 다르다. 본문 여러 곳에서 retrun 해야 하는 코드 블록을 만들어야 한다면 람다 대신 무명 함수를 쓸 수 있다.

