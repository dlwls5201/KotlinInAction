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

원시 타입과 참조 타입이 같아면 코톨른이 그들을 항상 객체로 표현하는 걸까? 하지만 코틀린을 그러지 않는다. 대부분의 경우 코틀린의 Int 타입은 자바 int 타입으로 컴파일 된다.

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
val과 var의 구별과 마찬가지로 컬렉션의 읽기 전용 인터페이스와 변경 가능한 인터페이스를 구별한 이유는 프로그램에서 데이터에 어떤 일이 벌지는지를 더 쉽게 이해하기 위함이다.

**코틀린 컬렉션과 자바**

모든 코틀린 컬렉션은 그에 상응하는 자바 컬렉션 인터페이스의 인스턴스라는 점은 사실이다. 따라서 코틀린과 자바 사이를 오갈 때 아무 변환도 필요 없다.

**객체의 배열과 원시 타입의 배열**




