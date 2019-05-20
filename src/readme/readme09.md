# 제네릭스

## 제네릭 타입 파라미터

제네릭스를 사용하면 타입 파라미터를 받는 타입을 정의할 수 있다. 제네릭타입의 인스턴스를 만들려면 타입 파라미터를 구체적인 타입 인자로 치환해야 한다.

### 제네릭 함수와 프로퍼티

```kotlin
public fun <T> List<T>.slice(indices: IntRange): List<T>
```
**T는 순서대로 다음과 같은 역활을 한다.**

1. 타입파라미터 선언<br>
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
List&lt; Int&gt;나 List&lt; Double&gt;에 그 함수를 적용할 수 있지만 List&lt; String&gt;등에는 그 함수를 적용할 수 없다.
sum 함수가 타입 파라미터로 숫자 타입만을 허용하게 정의하면 이런 조건을 표현할 수 있다.
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

### 클래스, 타입, 하위 타입

타입 사이의 관계를 논하기 위해 하위 타입이라는 개념을 잘 알아야 한다. 어떤 타입 A의 값이 필요한 모든 장소에 어떤 타입 B의 값을 넣어도 아무 문제가 없다면
타입 B는 타입 A의 하위 타입이다. 예를 들어 Int는 Number의 하위 타입이지만 String의 하위 타입은 아니다.

상위 타입은 하위 타입의 반대다. A 타입이 B 타입의 하위 타입이라면 B는 A의 상위 타입 이다.

## 공변성: 하위 타입 관계를 유지

Producrer<T>를 예로 공변성 클래스를 설명하자. A가 B의 하위 타입일 때 Producer<A>가 Producer<B>의 하위 타입이면 Producer는 공변적이다.
이를 하위 타입 관계가 유지된다고 말한다. 예를 들어 Cat가 Animal의 하위 타입이기 때문에 Producer<Cat>은 Producer<Animal>의 하위 타입이다.

코틀린에서 제네릭 클래스가 타입 파라미터에 대해 공변적임을 표시하려면 타입 파라미터 이름 앞에 out을 넣어야 한다.
```kotlin
interface Producer<out T> { // 클래스가 T에 대해 공변적이라고 선언한다.
    fun produce(): T
}
```
클래스의 타입 파라미터를 공변적으로 만들면 함수 정의에 사용한 파라미터 타입과 타입 인자의 타입이 정확히 일치하지 않더라도 그 클래스의 인스턴스를
함수 인자나 반환 값으로 사용할 수 있다. 예를 들어 Herd 클래스로 표현되는 동물 무리의 사육을 담당하는 함수가 있다고 생각해보자.
Head 클래스의 타입 파라미터는 그 떼가 어떤 동물 무리인지 알려준다.

```kotlin
open class Animal {
    fun feed() {...}
}

class Herd<T: Animal> {
    private val animals = arrayListOf<T>()

    val size: Int get() = animals.size

    operator fun get(i: Int): T {
        return animals[i]
    }
}

fun feedAll(animals: Herd<Animal>) {
    for (i in 0 until animals.size) {
        animals[i].feed()
    }
}
```

사용자 코드가 고양이 무리를 만들어서 관리한다고 하자.

```kotlin
class Cat : Animal() {
    fun cleanLitter() {}
}

fun takeCareOfCats(cats: Herd<Cat>) {
    for (i in 0 until cats.size) {
        cats[i].cleanLitter()
        feedAll(cats) // Error: Type mismatch, Required : Herd<Animal> Found : Herd<Cat>
    }
}
```

feedAll 함수에게 고양이 무리를 넘기면 타입 불일치 오류를 볼 수 있다. Herd 클래스의 T 타입 파라미터에 대해 아무 변성도 지정하지 않았기 때문에 고양이 무리는
동물 무리의 하위 클래스가 아니다. 명시적으로 타입 캐스팅을 사용하면 이 문제를 풀 수 있긴 하지만 그런 식으로처리하면 코드가 장황해지고 실수를 하기 쉽다.

```kotlin
class Herd<out T: Animal> { // T 는 이제 공변적이다.
    private val animals = arrayListOf<T>()

    val size: Int get() = animals.size

    operator fun get(i: Int): T {
        return animals[i]
    }
}

fun takeCareOfCats(cats: Herd<Cat>) {
    for (i in 0 until cats.size) {
        cats[i].cleanLitter()
    }
    feedAll(cats) // 캐스팅을 할 필요가 없다.
}
```

타입 파라미터를 공변적으로지정하면 클래스 내부에서 그 파라미터를 사용하는 방법을 제한한다. 타입 안전성을 보장하기 위해 공변적 파라미터는 항상 아웃 위치에만 있어야 한다.
이는 클래스가 T 타입의 값을 생산할 수는 있지만 T 타입의 값을 소비할 수는 없다는 뜻이다.

```kotlin
interface Tramsformer<T> {
    fun transform(t: T // in 위치): T // out 위치
}
```

함수 파라미터 타입은 인 위치, 함수 반환 타입은 아웃 위치에 있다.

클래스 타입 파라미터 T 앞에 out 키워드를 붙이면 클래스 안에서 T를 사용하는 메소드가 아웃 위치에서만 T를 사용하게 허용하고 인 위치에서는 T를 사용하지 못하게 막는다.
out 키워드는 T의 사용법을 제한하며 T로 인해 생기는 하위 타입 관계의 타입 안전성을 보장한다.

타입 파라미터 T에 붙은 out 키워드는 다음 두 가지를 함께 의미한다.
- 공변성 : 하위 타입 관계가 유지된다.
- 사용 제한 : T를 아웃 위치에서만 사용할 수 있다.

이제 List<T> 인터페이스를 보자. 코틀린 List는 읽기 전용이다. 따라서 그 안에는 T 타입의 원소를 반환하는 get 메소드는 있지만 리스트에 T 타입의 값을 추가하거나 리스트에 있는
기존 값을 변경하는 메소드는 없다. 따라서 List는 T에 대해 공변적이다.
```kotlin
interface List<out T> : Collection<T> {
    operator fun get(index: Int): T // 일기 전용 메소드로 T를 반환하는 메소드만 정의한다.(따라서 T는 항상 아웃 위치에 쓰인다.)
    { ... }
}
```

MutableList<T>를 타입 파라미터 T에 대해 공변적인 클래스를 선언할 수는 없다.
```kotlin
interface MutableList<T>
    : List<T>, MutableCollection<T> {
        override fun add(element: T): Boolean // T가 인 위치에 쓰이기 때문에 MutableList는 T에 대해 공변적일 수 없다.
    }
```

생성자 파라미터는 인이나 아웃 어느 쪽도 아니라는 사실에 유의하라. 타입 파라미터가 out 이라 해도 그 타입을 여전히 생성자 파라미터 선언데 사용할 수 있다.
```kotlin
class Herd<out T: Animal>(vararg animals: T) { ... }
```

변성은 코드에서 위험할 여지가 있는 메소드를 호출할 수 없게 만듦으로써 제네릭 타입의 인스턴스 역활을 하는 클래스 인스턴스를 잘못 사용하는 일이 없게 방지하는 역활을 한다.
생성자는 (인스턴스를 생성한 뒤) 나중에 호출할 수 있는 메소드가 아니다. 따라서 생성자는 위험할 여지가 없다.

하지만 val 이나  var 키워드를 생성자 파라미터에 적는다면 게터나 세터를 정의하는 것과 같다. 따라서 읽기 전용 프로퍼티는 아웃 위치, 변경 가능 프로퍼티는 아웃과 인 위치 모두에 해당한다.
```kotlin
class Herd<T: Animal>(var leadAnimal: T, vararg animals: T) { ... }
```

변성 규칙은 클래스 외부의 사용자가 클래스를 잘못 사용하는 일을 막기 위한 것으로 클래스 내부 구현에는 적용되지 않는다. 클래스의 타입 파리미터가 인 위치에서만 쓰이는 경우에는
어떤 일이 생길지 궁금할 것이다. 그런 경우 타입 파라미터의 하위 타입 관계와 제네릭 타입의 하위 타입 관계가 서로 역전된다.

### 반공변성: 뒤집힌 하위 타입 관계

반공변 클래스의 하위 타입 관계는 공변 클래스의 경우와 반대다. Comparator 인터페이스를 살펴보자. 이 인터페이스에는 compare라는 메소드가 있다. 이 메소드는 주어진 두 객체를 비교한다.
```kotlin
interface Comparator<in T> {
    fun compare(e1: T, e2: T): Int { ... } // T를 인 위치에 사용한다.
}
```
이 인터페이스의 메소드는 T 타입의 값을 소비하기만 한다. 이는 T가 인 위치에서만 쓰인다는 뜻이다. 따라서 T 앞에는 in 키워드를 붙여야만 한다.

in이라는 키워드는 그 키워드가 붙은 타입이 이 클래스의 메소드 안으로 전달돼 메소드에 의해 소비된다는 뜻이다. 공변성의 경우와 마찬가지로 타입 파라미터의 사용을 제한함으로써
특정 하위 타입 관계에 도달할 수 있다. in 키워드를 타입 인자에 붙이면 그 타입 인자를 오직 인 위치에서만 사용할 수 있다는 뜻이다.

### 사용 지점 변셩: 타입이 언듭되는 지점에서 변성 지정

클래스를 선언하면서 변성을 지정하면 그 클래스를 사용하는 모든 장소에 변성 지정자가 영향을 끼치므로 편리하다. 이런 방식을 **선언 지점 변성**이라 부른다. 자바의 와일드카드 타입
(? extends 나 ? super)에 익숙하다면 자바는 변성을 다른 방식으로 다룬다는 점을 깨달았을 것이다. 자바에서는 타입 파라미터가 있는 타입을 사용할 때마다 해당 타입 파라미터를
하위 타입이나 상위 타입 중 어떤 타입으로 대치할 수 있는지 명시해야 한다. 이런 방식을 **사용 지점 변성**이라 부른다.

코틀린도 사용 지점 변성을 지원한다. 따라서 클래스 안에서 어떤 타입 파라미터가 공변적이거나 반공변젹인지 선언할 수 없는 경우에도 틀정 타입 파라미터가 나타나는 지점에서 변성을 정할 수 있다.

MutableList와 같은 상당수의 인터페이스는 타입 파라미터로 지정된 타입을 소비하는 동시에 생산할 수 있기 떄문에 일반적으로 공변적이지도 반공변적이지도 않다. 하지만 그런 인터페이스 타입의
변수가 한 함수 안에서 생산자나 소비자 중 단 하지 역활만을 담당하는 경우가 자주 있다.

**무공면 파라미터 타입을 사용하는 데이터 복사 함수**
```kotlin
fun <T> copyData(source: MutableList<T>, destination: MutableList<T>) {
    for (item in source) {
        destination.add(item)
    }
}
```
**타입 파라미터가 둘인 데이터 복사 함수**
```kotlin
fun <T: R, R> copyData(source: MutableList<T>,  // source 원소 타입은 destination 원소 타입의 하위 타입이어야 한다.
                        destination: MutableList<R>) {
    for(item in source) {
        destination.add(item)
    }
}
```

코틀린에는 이를 더 우아하게 표현할 수 있는 방법이 있다.
함수 구현이 아웃 위치 (또는 인 위치)에 있는 타입 파라미터를 사용하는 메소드만 호출한다면 그런 정보를 바탕으로 함수 정의 시 타입 파라미터에 변성 변경자를 추가할 수 있다.

```kotlin
fun <T> copyData(source: MutableList<out T>, // out 키워드를 타입을 사용하는 위치 앞에 붙이면 T 타입을 in 위치에 사용하는 메소드를 호출하지 않는다는 뜻이다.
                    destination: MutableList<T>) {
    for(item in source) {
        destination.add(item)
    }
}
```

```kotlin
fun <T> copyData(source: MutableList<T>,
                    destination: MutableList<in T>) {  // 원본 리스트 원소 타입의 상위 타입을 대상 리스트 원소 타입으로 허용한다.
    for(item in source) {
        destination.add(item)
    }
}
```

>코틀린의 사용 지점 변성 선언은 자바의 한정 와일드카드와 똑같다. 코틀린 MutableList<out T>는 자바 MutableList<? extends T>와 같고
코틀린 MutableList<in T>는 자바 MutableList<? super T>와 같다.

사용 지점 변성을 사용하면 타입 인자로 사용할 수 있는 타입의 범위가 넓어진다.

### 스타 프로젝셔: 타입 인자 대신 사용

제네릭 타입 인자 정보가 없음을 표현하기 위해 **스타 프로젝션**을 사용한다. 예를 들어 원소 타입이 알려지지 않은 리스트는 List<*>라는 구문으로 표현할 수 있다.

## 요약

- 코틀린 제네릭스는 자바와 아주 비슷하다. 제네릭 함수와 클래스를 자바와 비슷하게 선언할 수 있다.
- 자바와 마찬가지로 제네릭 타입의 타입 인자는 컴파일 시점에만 존재한다.
- 타입 인자가 싱행 시점에 지워지므로 타입 인자가 있는 타입(제네릭 타입)을 is 연산자를 사용해 검사 할 수 없다.
- 인라인 함수의 타입 매개변수를 reified로 표시해서 실체화하면 실행 시점에 그 타입을 is 검사하거나 java.lang.class 인스턴스를 얻을 수 있다.
- 변성은 기저 클래스가 같고 타입 파라마터가 다른 두 제네릭 타입 사이의의 상위/하위 타입 관계가 타입 인자 사이의 상위/하위 타입 관계에 의해 어떤 영향을 받는지를 명시하는 방법이다.
- 제네릭 클래스의 타입 파라미터가 아웃 위치에서만 사용되는 경우(생산자) 그 타입 파라미터를 out으로 표시해서 공변적으로 만들 수 있다.
- 공변적인 경우와 반대로 제네릭 클래스의 타입 파라미터가 인 위치에서만 사용되는 경우(소비자) 그 타입 파라미터를 in으로 표시해서 반공변적으로 만들 수 있다.
- 코틀린의 읽기 전용 List 인터페이스는 공변적이낟. 따라서 List<String>은 List<Any>의 하위 타입이다.
- 함수 인터페이스는 첫 번째 타입 파라미터에 대해서는 반공변적이고, 두 번째 타입 파라미터에 대해서는 공변적인다.
- 코틀린에서는 제네릭 클래스이 공변성을 전체적으로 지정하거나(선언 지점 방식), 구체적인 사용 위치에서 지정할 수 있다.(선언 지점 변성)
- 제네릭 클래스의 타입 인자가 어떤 타입인지 정보가 없거나 타입 인자가 어떤 타입인지가 중요하지 않을 때 스타 프로젝션 구문을 사용할 수 있다.

