# 코클린 타입 시스템

- 널이 될 수 있는 타입과 널을 처리하는 구문의 문제
- 코틀린 원시 타입 소개와 자바 타입과 코틀린 원시 타입의 관계
- 코틀린 컬렉션 소개와 자바 컬렉션과 코틀린 컬렉션의 관계

**널 가능성**

실행 시점에 널이 될 수 있는 타입이나 널이 될 수 없는 타입의 객체는 같다. 널이 될 수 있는 타입은 널이 될 수 없는 타입을 감싼 래퍼 타입이 아니다.
모든 검사는 컴파일 시점에 수행된다. 따라서 코틀린에서는 널이 될 수 있는 타입을 처리하는데 별도의 실행 시점 부가 비용이 들지 않는다.


```kotlin
class Person(val firstName: String, val lastName: String) {
    override fun equals(other: Any?): Boolean {

        val otherPerson = other as? Person ?: return false

        return otherPerson.firstName == firstName &&
                otherPerson.lastName == lastName
    }

    override fun hashCode(): Int =
            firstName.hashCode() * 37 + lastName.hashCode()
}
```

안전한 호출, 안전한 캐스트, 엘비스 연산자(?:)로 유용하다.

**널 아님 단언: !!**

느낌표를 이중(!!)으로 사용하면 어떤 값이든 널이 될 수 없는 타임으로 바꿀 수 있다.

```kotlin
fun ignoreNulls(s: String?) {
    val sNotNull: String = s!!
    println(sNotNull.length)
```

**나중에 초기화할 프로퍼티**

lateinit 프로퍼티를 의존관계 주입(DI) 프레임워크와 함께 사용하는 경우가 많다.

**널이 될 수 있는 타입 확장**

```kotlin
fun verifyUserInput(input: String?) {
    if(input.isNullOrBlank()) {
        println("Please fill in the required fields")
    }
}

fun main() {
    verifyUserInput(" ")
    verifyUserInput(null)
}

```

## 코틀린의 원시 타입

**원시 타입: Int, Boolean 등**

자바는 원시 타입과 참조 타입을 구분 한다. **원시 타입**(primitiva type)의 변수에는 그 값이 직접 들어가지만, **참조 타입**(reference type)의 변수에는 메모리상에 객체 위치가 들어간다.
원시 타입의 값을 더 효율적으로 저장하고 여기저기 전달할 수 있다. 하지만 그런 값에 대해 메소드를 호출하거나 컬렉션에 원시 타입 값을 담을 수는 없다.

코틀린은 원시 타입과 래퍼 타입을 구분하지 않으므로 항상 같은 타입을 사용한다.

원시 타입과 참조 타입이 같으면 코틀이 그들을 항상 객체로 표현하는 걸까? 하지만 코틀린을 그러지 않는다. 대부분의 경우 코틀린의 Int 타입은 자바 int 타입으로 컴파일 된다.

**숫자 변환**

코틀린은 모든 원시 타입(단 Boolean은 제외)에 대한 변환 함수를 제공한다.
코틀린은 개발자의 혼란을 피하기 위해 타입 변환을 명시하기로 결정했다.


## 컬렉션과 배열

**널 가능성과 컬렉션**

```kotlin
fun addValidNumbers1(numbers: List<Int?>) {
    var sumOfValidNumbers = 0
    var invalidNumbers = 0

    for(number in numbers) {
        if(number != null) {
            sumOfValidNumbers += number
        } else {
            invalidNumbers++
        }
    }

    println("Sum of valid numbers: $sumOfValidNumbers")
    println("Invalid numbers: $invalidNumbers")
}

fun addValidNumbers2(numbers: List<Int?>) {
    val validNumbers = numbers.filterNotNull()
    println("Sum of valid numbers: ${validNumbers.sum()}")
    println("Invalid numbers: ${numbers.size - validNumbers.size}")
}
```

**읽기 전용과 변경 가능한 컬렉션**

코드에서 가능하면 항상 읽기 전용 인터페이스를 사용하는 것을 일반적인 규칙으로 삼아라.<br>
val과 var의 구별과 마찬가지로 컬렉션의 읽기 전용 인터페이스와 변경 가능한 인터페이스를 구별한 이유는 프로그램에서 데이터에 어떤 일이 벌어지는지를 더 쉽게 이해하기 위함이다.

**코틀린 컬렉션과 자바**

모든 코틀린 컬렉션은 그에 상응하는 자바 컬렉션 인터페이스의 인스턴스라는 점은 사실이다. 따라서 코틀린과 자바 사이를 오갈 때 아무 변환도 필요 없다.

**객체의 배열과 원시 타입의 배열**

- arrayOf 함수에 원소를 넘기면 배열을 만들 수 있다.
- arrayOfNulls 함수에 정수 값을 인자로 넘기면 모든 원소가 null이고 인자로 넘긴 값과 크기가 같은 배열을 만들 수 있다. 물론 원소 타입이 널이 될 수 있는 타입인 경우에만 이 함수를 쓸 수 있다.
- Array 생성자는 배열의 크기와 람다를 인자로 받아서 람다를 호출해서 각 배열 원소를 초기화해준다. arrayOf를 쓰지 않고 각 원소가 널이 아닌 배열을 만들어야 하는 경우 이 생성자를 사용한다.

## 커니의 코틀린 - 배열 생성 법

배열

함수의 인자로 받은 값으로 구성된 배열을 반환합니다.
```kotlin
    fun <T> arrayOf(varag elements: T): Array<T>

    val list = arrayOf(1,2,3,4,5)
```

특정 타입을 갖는 빈 배열을 반환합니다.
```kotlin
    fun <T> emptyArray(): Array<T>

    val list = emptyArray<String>()
```

배열 내 각 값들이 모두 널 값으로 초기화되어 있고, 인자로 받은 size 만큼의 크기를 갖는 배열을 반환합니다.
```kotlin
    fun <T> arrayOfNulls(size: Int): Array<T?>

    val list = arrayOfNulls<String>(3)
```

배열 내 각 값들을 특정 값으로 초기화 하는 size 만큼의 크기를 갖는 배열을 반환합니다.
```kotlin
    //Array constructor
    public inline constructor(size: Int, init: (Int) -> T)

    val array = Array<Int>(5) { -1 }
```

자바 원시 타입을 포함하는 배열
```kotlin
    fun intArrayOf(varag elements: Int): IntArray

    val list = intArrayOf(1,2,3,4,5)
```

Array 생성자는 배열의 크기와 람다를 인자로 받아서 람다를 호출해서 각 배열 원소를 초기화해준다.
```kotlin
    public inline constructor(size: Int, init: (Int) -> T)

    val list = Array(5) { i -> i * i}
```

## 요약

- 코틀린은 널이 될 수 는 타입을 지원해 NullPointerException 오류를 컴파일 시점에 감지할 수 있다.
- 코틀린의 안전한 호출(?.), 엘비스 연산자(?:), 널 아님 단언(!!), let 함수등을 사용하면 널이 될 수 있는 타입을 간결한 코드로 다룰 수 있다.
- as? 연산자를 사용하면 값을 다른 타입으로 변환하는 것과 변환이 불가능한 경우를 처리하는 것을 한꺼번에 편리하게 처리할 수 있다.
- 자바에서 가져온 타입은 코틀린에서 플랫폼 타입으로 취급된다. 개발자는 플랫폼 타입을 널이 될 수 있는 타입으로도, 널이 될 수 없는 타입으로도 사용할 수 있다.
- 코틀리에서는 수를 표현하는 타입(Int 등)이 일반 클래스와 똑같이 생겼고 일반 클래스와 똑같이 동작한다. 하지만 대부부 컴파일러는 숫자 타입을 자바 원시 타입(int 등)으로 컴파일 한다.
- 널이 될 수 있는 원시 타입(Int? 등)은 자바의 박싱한 원시 타입(java.lang.Integer 등)에 대응한다.
- Any 타입은 다른 모든 타입의 조상 타입이며, 자바의 Object에 해당하낟. Unit은 자바의 void와 비슷하다.
- 정상적으로 끝나지 않는 함수의 반환 타입을 지정할 때 Nothing 타입을 사용한다.
- 코틀린 컬렉션은 표준 자바 컬렉션 클래스를 사용한다. 하지만 코틀린은 자바보다 컬렉션을 더 개선해서 읽기 전용 컬렉션과 변경 가능한 컬렉션을 구별해 제공한다.
- 자바 클래스를 코틀린에서 확장하거나 자바 인터페이스를 코틀린에서 구현하는 경우 메소드 파라미터의 널 가능성과 변경 가능성에 대해 깊이 생각해야 한다.
- 코틀린의 Array 클래스는 일반 제네릭 클래스처럼 보인다. 하지만 Array 는 자바 배열로 컴파일된다.
- 원시 타입의 배열은 IntArray와 같이 각 타입에 대한 특별한 배열로 표현된다.




