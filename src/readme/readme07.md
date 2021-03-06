# 연산자 오버로딩과 기타 관례

- 연산자 오버로딩
- 관례: 여러 연산을 지원하기 위해 특별한 이름이 붙은 메소드
- 위임 프로퍼티

## 산술 연산자 오버로딩

### 이항 상술 연산 오버로딩

```kotlin
data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

fun main() {

    val p1 = Point(10,20)
    val p2 = Point(30,40)
    println(p1 + p2)
}
```
[Point(x=40, y=60)]
plus 함수 앞에 operator 키워드를 붙여야 한다. operator 키워드를 붙임으로써 어떤 함수가 관례를 따르는 함수임을 명확히 할 수 있다.

**연산자를 확장 함수로 정의하기**

```kotlin
data class Point(val x: Int, val y: Int) {
    //...
}

operator fun Point.plus(other: Point) : Point {
    return Point(x + other.x, y + other.y)
}

operator fun Point.times(scale: Double) : Point {
    return Point((x * scale).toInt(), (y * scale).toInt())
}
>>  val p = Point(10, 20)
>>  println(p * 1.5)
```
[Point(x=15, y=30]]

외부 함수의 클래스에 대한 연산자를 정의할 때는 관례를 따르는 이름의 확장 함수로 구현하는 게 일반적인 패턴이다.

**오버로딩 가능한 이항 상술 연산자**

식 | 함수 이름 | 기타
---|:---:|---:
`a * b` | times |
`a / b` | div |
`a % b` | mod | (1.1부터 rem)
`a + b` | plus |
`a - b` | minus |

### 복합 대입 연산자 오버로딩

```kotlin
    var point = Point(1,2)
    point += Point(3,4)
    println(point)
```
[Point(x=4, y=6]

코틀린 표준 라이브러리는 변경 가능한 컬렉션에 대해 plusAssign을 정의하며, 앞의 예제는 그 plusAssign을 사용한다.

```kotlin
operator fun <T> MutableCollection<T>.plusAssign(element: T) {
    this.add(element)
)
```

```kotlin
val list = arrayListOf(1,2)
list += 3 // +=는 'list'를 변경한다.

val newList = list + listOf(4,5) // +는 두 리스트의 모든 원소를 포함하는 새로운 리스트를 반환한다.
println(list)
println(newList)
```
[1,2,3]
[1,2,3,4,5]

### 단항 연상자 오버로딩

단항 연산자를 오버로딩하는 절차도 이항 연산자와 마찬가지다. 미리 정해진 이름의 함수를(멤버 확장 함수로)선언하면서 operator로 표시하면 된다.

```kotlin
operator fun Point.unaryMinus(): Point {
    return Point(-x,-y)
}
>>  val p = Point(10,20)
>>  println(-p)
```
[Point(x=10, y=-20)]

식 | 함수 이름
---|:---:|
`+a` | unaryPlus |
`-a` | unaryMinus |
`!a` | not |
`++a, a++` | inc |
`--a, a--` | dec |

## 비교 연산자 오버로딩

### 동등성 연산자: equals

코틀린이 == 연산자 호출을 equals 메소드 호출로 컴파일한다는 사실을 배웠다. ==와 !=는 내부에서 인자가 널인지 검사하므로 다른 연산과 달리 널이 될 수 있는 값에도 적용할 수 있다.

```kotlin
class Point(val x: Int, val y: Int) {
    override fun equals(obj: Any?): Boolean {
        if (obj === this) return true //파라미터가 this와 같은 객체인지
        if(obj !is Point) return false // 파리미터 타입 검사
        return obj.x == x && obj.y == y
    }
}

//equals 를 구현 해줘야만 true 값이 나옵니다.
Point(10,20) == Point(10, 20)

```
**정리**

Java 에서는 equals 는 대상의 내용물 자체를 비교하지만 == 연산자는 비교하는 대상의 주소값을 비교 합니다.

Kotlin 에서는 equals 는 == 와 매칭되며 객체의 주소값을 비교하기 위해서는 === 연산자를 사용해야 합니다.


### 순서 연산자: compareTo

Comparable에 들어있는 compareTo 메소드는 한 객체와 다른 객체의 크기를 비교해 정수로 나타내준다.

```kotlin
class Person(
    val firstName: String, val lastName: String
): Comparable<Person> {
    override fun compareTo(other: Person): Int {
        return compareValuesBy(this, other, Person::lastName, Person::firstName)
    }
}
```

## 컬렉션과 범위에 대해 쓸 수 있는 관례

인덱스를 사용해 원소를 설정하거나 가져오고 싶을 때는 a[b]라는 식을 사용한다.(이를 인덱스 연산자라고 부른다). in 연산자는 원소가 컬렉션이나 범위에 속하는지 검사하거나 컬렉션에 원소를 이터레이션할 때 사용한다.

### 인덱스로 원소에 접근: get과 set

인덱스 연산자를 사용해 원소를 읽는 연산은 get 연산자와 메소드로 변환되고, 원소를 쓰는 연산은 set 연산자 메소드로 변환된다.
Map과 MutableMap 인터페이스에는 그 두 메소드가 이미 들어있다. 이제 Point 클래스에 이런 메소드를 추가해보자.

```kotlin
operator fun Point.get(index: Int) : Int { // get 연산자 함수를 정의한다.
    return when(index) {
        0 -> x
        1 -> y
        else ->
        throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}
>> val p = Point(10,20)
>> println(p[1])
```
[20]

### in 관례

>(a in c) -> c.contains(a)<br>
in 연산자는 contains 함수 호출로 변환된다.

- in은 객체가 컬렉션에 들어있는지 검사한다.
- in 연산자와 대응하는 함수는 contains다
- 어떤 점이 사각형 영역에 들어가는지 판단할 때 in 연산자를 사용하게 구현해보자

```kotlin
data class Rectangle(val upperLeft: Point, val lowerRight: Point)

operator fun Rectangle.contains(p: Point): Boolean {
    return p.x in upperLeft.x until lowerRight.x &&
            p.y in upperLeft.y until lowerRight.y
}

fun main() {

    val rect = Rectangle(Point(10,20), Point(50,50))
    println(Point(20, 30) in rect)
}
```
### rangeTo 관례

>(start..end) -> start.rangeTo(end)<br>
.. 연산자는 rangeTo 함수 호출로 컴파일 된다.

- 이 연산자를 아무 클래스에나 정의할 수 있다. 하지만 어떤 클래스가 Comparable 인터페이스를 구현하면 rangeTo를 정의할 필요가 없다.
- 코틀린 표준 라이브러리에는 모든 Comparable 객체에 대해 적용 가능한 rangeTo 함수가 들어있다.

```kotlin
    operator fun <T: Comparable<T>> T.rangeTo(that: T): ClosedRange<T>
```

### for 루프를 위한 iterator 관례

앞서 했듯이 코틀린의 for 루프는 범위 검사와 똑같이 in 연산자를 사용한다. 하지만 이 경우 in의 의미는 다르다.
for(x in list) {...}와 같은 문장은 list.iterator()을 호출해서 이터레이터를 얻은 다음, 자바와 마찬가지로 그 이터레이터에 대해 hasNext와 next 호출은 반복하는 식으로 변환된다.
하지만 코틀린에서는 이 또한 관례이므로 iterator 메소드를 확장 함수로 정의할 수 있다.

```kotlin
    operator fun CharSequence.iterator(): CharIterator
    >>> for (c in "abv") {}
```

클래스 안에 직접 iterator 메소드를 구현할 수도 있다. 예를 들어 날짜에 대해 이터레이션하는 다음 코드를 살펴보자

```kotlin
operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
        object : Iterator<LocalDate> {
            var current = start
            override fun hasNext() = current <= endInclusive

            override fun next() = current.apply {
                current = plusDays(1) //현재 날짜를 1일 뒤로 변경한다.
            }
        }
```

## 구조 분해 선언과 component 함수

구조 분해를 사용하면 복합적인 값을 분해해서 여러 다른 변수를 한꺼번에 초기화할 수 있다.

###  구조 분해 선언을 사용해 여러 값 반환하기

```kotlin
    data class NameComponents(val name: String, val extension: String)

    fun splitFilename(fullName: String): NameComponents {
        val result = fullName.split('.',limit = 2)
        return NameComponents(result[0], result[1])
    }

//main
    val (name, ext) = splitFilename("example.kt")
    println(name)
    >> example
    println(ext)
    >> kt

```

### 구조 분해 선언과 루프

아래 간단한 예제는 두 가지 코틀린 관례를 활용한다. 하나는 객체를 이터레이션하는 관례고, 다른 하나는 구조 분해 선언이다.
코틀린 표준 라이브러리에는 맵에 대한 확장 함수로 iterator가 들어있다. 그 iterator는 맵 원소에 대한 이터레이터를 반환한다.
또한 코틀린 라이브러리 Map.Entry에 대한 확장 함수로 component1과 component2를 제공한다.

```kotlin
    fun printEntries(map: Map<String, String>) {
        for((key, value) in map) { // 루프 변수에 구조 분해 선언을 사용한다.
            println("$key -> $value")
        }
    }

//main
    val map = mapOf("Oracle" to "Java", "JetBrains" to "Kotlin")
    printEntries(map)
```

코틀린 표준 라이브러이에서는 맨 앞의 다섯 원소에 대한 componentN을 제공한다.
```kotlin
    val map = mapOf("Oracle" to "Java", "JetBrains" to "Kotlin")
    for (entry in map.entries) {
        val key = entry.component1()
    }
```


## 프로퍼티 접근자 로직 재활용: 위임 프로퍼티
위임 프로퍼티는 사용하면 값을 뒷받침하는 필드에 단순히 저장하는 겂보다 더 복잡한 방식으로 작동하는 프로퍼티를 쉽게 구현 할 수 있다. 또한 그 과정에서 접근자 로직을 매번 재구현할 필요도 없다. 예를 들어 프로퍼티는 위임을 사용해 자신의 값을 필드가 아니라 데이터베이스 테이블이나 브라우저 세션, 맵 등에 저장할 수 있다.
위임은 객체가 직접 작업을 수행하지 않고 다른 도우미 객체가 그 작업을 처리하게 맡기는 디자인 패턴을 말한다. 이때 작업을 처리하는 도우미 객체를 위임 객체(delegate)라고 부른다.

### 위임 프로퍼티 소개

위임 프로퍼티의 일반적인 문법은 다음과 같다.

```kotlin
class Foo {
    var p: Type by Delegate()
}
```

### 위임 프로퍼티 사용: by lazy()를 사용한 프로퍼티 초기화 지연

```kotlin
class Person(val name: String) {
    private var _emails: List<Email>? = null // 데이터를 저장하고 emails 에 위임, 객체 역활을 하는 _emails 프로퍼티
    val emails: List<Email>
        get() {
            if(_emails == null) {
                _emails = loadEmails(this) // 최초 접근 시 이메일을 가져온다.
            }
            return _emails!! // 저장해 둔 데이터가 있으면 그 데이터를 반환한다.
        }
}
```

여기서는 뒷받침하는 프로퍼티라는 기법을 사용한다. _emails 라는 프로퍼티는 값을 저장하고, 다른 프로퍼티인 emails 는 _emails 라는 프로퍼티에 대한 읽기 연산을 제공한다.
_emails 는 널이 될 수 있는 타입인 반면 emails 는 널이 될 수 없는 타입이므로 프로퍼티를 두 개 사용해야 한다. 하지만 이런 코드를 만드는 일은 약산 성가시다. 이 구현은 스레드 안전하지 않아서 언제나 제대로 작동한다고 말할 수도 없다. 코틀린은 더 나은 해법을 제공한다.

위임 프로퍼티를 사용하면 이 코드가 훨씬 더 간단해지나. 위임 프로퍼티는 데이터를 저장할 때 쓰이는 뒷받침하는 프로퍼티와 값이 오직 한 번만 초기화됨을 보장하는 게터 로직을 함께 캡슐화해준다.

지연 초기화를 위임 프로퍼티를 통해 구현하기
```kotlin
class Person(val name: String) {
    val emails by lazy { loadEmails (this) }
}
```

## 요약

- 코틀린에서는 정해진 이름의 함수를 오버로딩함으로써 표준 수학 연산자를 오버로딩할 수 있다. 하지만 직접 새로운 연산자를 만들 수는 없다.
- 비교 연산자는 equals와 compareTo 메소드로 변환된다.
- 클래스에 get, set, contains라는 함수를 정의하면 그 클래스의 인스턴스에 대해 []와 in 연산을 사용할 수 있다고, 그 객체를 코틀린 컬렉션 객체와 비슷하게 다룰 수 있다.
- 미리 정해진 관례를 따라 rangeTo, iterator 함수를 정의하면 범위를 만들거나 컬렉션과 배열의 원소를 이터레이션할 수 있다.
- 구조 분해 선언을 통해 한 객체의 상태를 분해해서 여러 변수에 대입할 수 있다. 함수가 여러 값을 한꺼번에 반환해야 하는 경우 구조 분해가 유용하다. 데이터 클래스에 대한 구조 분해는 거저 사용할 수 있지만,
커스텀 클래스의 인스턴스에서 구조 분해를 사용하려면 componentN 함수를 정의해야 한다.
- 위임 프로퍼티를 통해 프로퍼티 값을 저장하거나 초기화하거나 읽거나 변경할 때 사용하는 로직을 재활용할 수 있다. 위임 프로퍼티는 프레임워크를 만들 때 아주 유용하다.
- 표준 라이브러리 함수인 lazy를 통해 지연 초기화 프로퍼티를 쉽게 수현할 수 있다.
- Delegates.observable 함수를 사용하면 프로퍼티 변경을 관찰할 수 있는 관찰자를 쉽게 추가할 수 있다.
- 맵을 위임 객체로 사용하는 위임 프로퍼티를 통해 다양한 속성을 제공하는 객체를 유연하게 다룰 수 있다.

