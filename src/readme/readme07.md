#연산자 오버로딩과 기타 관례

>연산자 오버로딩
>관례: 여러 연산을 지원하기 위해 특별한 이름이 붙은 메소드
>위임 프로퍼티

##산술 연산자 오버로딩

**이항 상술 연산 오버로딩**

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

plus 함수 앞에 operator 키워드를 붙여야 한다. operator 키워드를 붙임으로써 어떤 함수가 관례를 따르는 함수임을 명확히 할 수 있다.

**연산자를 확장 함수로 정의하기**

```kotlin
data class Point(val x: Int, val y: Int) {
    //...
}

operator fun Point.plus(other: Point) : Point {
    return Point(x + other.x, y + other.y)
}
```

외부 함수의 클래스에 대한 연산자를 정의할 때는 관례를 따르는 이름의 확장 함수로 구현하는 게 일반적인 패턴이다.

**오버로딩 가능한 이항 상술 연산자**

식 | 함수 이름 | 기타
---|:---:|---:
`a * b` | times |
`a / b` | div |
`a % b` | mod | (1.1부터 rem)
`a + b` | plus |
`a - b` | minus |

**복합 대입 연산자 오버로딩**

```kotlin
 var point = Point(1,2)
    point += Point(3,4)
    println(point)
```

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
```


##프로퍼티 접근자 로직 재활용: 위임 프로퍼티

위임 프로퍼티는 사용하면 값을 뒷받침하는 필드에 단순히 저장하는 겂보다 더 복잡한 방식으로 작동한느 프로퍼티를 쉽게 구현 할 수 있다.


