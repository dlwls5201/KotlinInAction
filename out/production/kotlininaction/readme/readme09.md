# 제네릭스

## 제네릭 타입 파라미터

제네릭스를사용하면 타입 파라미터를 받는 타입을 정의할 수 있다. 제네릭타입의 인스턴스를 만들려면 타입 파라미터를 구체적인 타입 인자로 치환해야 한다.

### 제네릭 함수와 프로퍼티

```kotlin
public fun <T> List<T>.slice(indices: IntRange): List<T>
```
**T는 순서대로 다음과 같은 역활을 한다.**

1. 타입파라미터 선언
2,3 타입 파라미터가 수신 객체와 반환타입에 쓰인다.

이런 함수를 구체적인 리스트에 대해 호출할 때 타입 인자를 명시적으로 지정할 수 있다. 하지만 실제로는 대부분 컴파일러가 타입 인자를 추론할 수 있으며 그럴 필요가 없다.

### 제네릭 클래스 선언

자바와 마찬가지로 코틀린에서도 타입 파라미터를 넣은 꺽쇠 기호를 클래스 이름 뒤에 붙이면 클래스를 제네릭하게 만들 수 있다.

```kotlin
interface List<T> {
    operator fun get(index: Int): T //인터페이스 안에서 T를 일반 타입처럼 사용할 수 있다.
}
```

### 타입 파라미터 제약

클래스나 함수에 사용할 수 있는 타입 인자를 제한하는 기능이다. 예를 들어 리스트에 속한 모든 원소의 합을 구하는 sum 함수를 생각해보자.
List<Int>나 List<Double>에 그 함수를 적용할 수 있지만 List<String>등에는 그 함수를 적용할 수 없다. sum 함수가 타입 파라미터로 숫자 타입만을 허용하게 정의하면 이런 조건을 표현할 수 있다.
어떤 타입을 제네릭 타입의 타입 파라미터에 대한 상한으로 지정하면 그 제네릭 타입을 인스턴스화 할 때 사용하는 타입 인자는 반드시 그 상한 타입이거나 그 상한 타입의 하위 타입이어야 한다.

**제약을 가하려면 타입 파라미터 이름 뒤에 콜론(:)을 표시하고 그 뒤에 상한 타입을 적으면 된다.**
```java
<T extends Number> T sum(List<T> list)
```

```kotlin
fun <T : Number> List<T>.sum : T
```

**타입 파라미터를 제약하는 함수 선언하기**

이제 두 파라미터 사이에서 더 큰 값을 찾는 제네릭 함수를 작성해보자.

```kotlin
fun <T: Comparable<T>> max(first: T, second: T): T {
    return if (first > second) first else second
}
```

**타입 파라미터에 여러 제약을 가하기**

```kotlin
fun <T> ensureTrailingPeriod(seq: T)
    where T : CharSequence, T : Appendable {
        if(!seq.endsWith('.') {
            seq.append('.')
        }
}
```

이 예제는 타입 인자가 CharSequence와 Appendable 인터페이스를 반드시 구현해야한다.

### 타입 파라미터를 널이 될 수 없는 타입으로 한정

아무런 상환을 정하지 않은 타입 파라미터는 결과적으로 Any?를 상한으로 정한 파라미터와 같다.

```kotlin
class Processor<T> {
    fun process(value: T) {
        value?.hashCode()   // value는 널이 될 수 있다.
    }
}

항상 널이 될 수 없는 타입만 타입 인자로 받게 만들려면 타입 파라미터에 제약을 가해야 한다. 널 가능성을 제외한 아무런 제약도 필요 없다면 Any? 대신 Any를 상한으로 사용하라.

```kotlin
class Processor<T : Any> {
    fun process(value: T) {
        value.hashCode()    // T 타입의 value는 null이 될 수 없다.
    }
}
```

## 실행 시 제네릭스의 동작: 소거된 타입 파라미터와 실체화된 타입 파라미터

함수는 inline으로 만들면 타입 인자가 지워지지 않게 할 수 있다.

### 실행 시점의 제네릭: 타입 검사와 캐스트

```kotlin
val list1: List<String> = listOf("a","b")

val list2: List<String> = listOf(1,2,3)
```

컴파일러는 두 리스트를 서로 다른 타입으로 인식하지만 실행 시점에 그 둘은 완전히 같은 타입의 객체다. 실행 시점에 어떤 값이 List인지 여부는 확실히 알아낼 수 있지만 그 리스트가 어떤 타입의 리스트인지는 알 수가 없다.

코틀린에서는 타입 인자를 명시하지 않고 제네릭 타입을 사용할 수 없다. 그렇다면 어떤 값이 집합이나 맵이 아니라 리스트라는 사실을 어떻게 확인할 수 있을까? 바로 **스타 프로젝션**을 사용하하면 된다.

```kotlin
if (value is List<*>) { ... }
```

**제네릭 타입으로 타입 캐스팅 하기**

```kotlin
fun printSum(c: Collection<*>) {
    val intList = c as? List<Int>
        ?: throw IllegalArgumentException("List is expected")
    println(intList.sum())
}
```

printSum(setOf(1,2,3))
집합은 리스트가 아니므로 예외가 발생한다.

printSum(listOf("1","2"))
as? 캐스팅은 성공하지만 나중에 다른 예외가 발생한다.

### 실체화한 타입 파라미터를 사용한 함수 선언

코틀린 제네릭 타입의 타입 인자 정보는 실행 시점에 지워진다. 따라서 제네릭 클래스의 인스턴스가 있어도 그 인스턴스를 만들 때 사용한 타입 인자를 알아낼 수 없다.
제네릭 함수의 타입 인자도 마찬가지다. 제네릭 함수가 호출되어도 그 함수의 본문에서는 호출 시 쓰인 타입 인자를 알 수 없다.

```kotlin
fun <T> isA(value: Any) = value is T
Error: Cannot check for instance of erased type: T
```

인라인 함수의 타입 파라미터는 실체화되므로 실행 시점에 인라인 함수의 타입 인자를 알 수 있다.

어떤 함수에 inline 키워드를 붙이면 컴파일러는 그 함수를 호출한 식을 모두 함수 본문으로 바꾼다. 함수가 람다를 인자로 사용하는 경우 그 함수를 인라인 함수로 만들면 람다 코드도 함께 인라이닝되고,
그에 따라 무명 클래스와 객체가 생성되지 않아서 성능이 더 좋아질 수 있다.

방금 살펴본 isA 함수를 인라인 함수로 만들고 타입 파라미터를 reified로 지정하면 value의 타입이 T의 인스턴스인지를 실행 시점에 검사할 수 있다.

```kotlin
inline fun <reified T> isA(value: Any) = value is T // 이제는 이 코드가 컴파일 된다.
```
**"reified"키워드는 이 타입 파라미터가 실행 시점에 지워지지 않을을 표시한다.**


실체화한 타입 파라미터를 사용하는 예를 살펴보자

```kotlin
    val items = listOf("one",2, "three")
    println(items.filterIsInstance<String>())
    >>> [one, three]
```

filterIsInstance의 타입 인자로 String을 지점함으로써 문자열만 필요하다는 사실을 기술한다. 여기서 타입 인자를 실행 시점에 알 수 있고
filterIsInatance는 그 타입 인자를 사용해 리스트의 원소 중에 타입 인자와 타입이 일치하는 원소만을 추려낼 수 있다.

## 변성: 제레릭과 하위 타입

변성개념은 List<String>와 List<Any>와 같이 기저 타입이 같고 타입 인자가 다른 여러 타입이 서로 어떤 관계가 있는지 설명하는 개념이다. 변성을 잘 활용하면
사용에 불편하지 않으면서 타입 안전성을 보장하는 API를 만들 수 있다.

### 변셩이 있는 이유: 인자를 함수에 넘기기



