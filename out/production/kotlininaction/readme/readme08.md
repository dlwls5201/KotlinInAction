# 고차 함수 : 파라미터와 반환 값으로 람다 사용

**8장에서 다루는 내용**
- 함수 타입
- 고차 함수와 코드를 구조화할 때 고차 함수를 사용하는 방법
- 인라인 함수
- 비로컬 return과 레이블
- 무명 함수

## 고차 함수 정의

고차 함수는 다른 함수를 인자로 받거나 함수를 반환하는 함수다.
람다나 함수 참조를 인자로 넘길 수 있거나 람다나 함수 참조를 반환하는 함수다.

**함수 타입**

```kotlin
val sum = { x: Int, y: Int -> x + y}

val action = { println(42) }

val sum: (Int, Int) -> Int = { x, y -> x + y}

val action: () -> Unit = { println(42) }

val canReturnNull: (Int, Int) -> Int? =  { _, _ -> null }

var funOrNull: ((Int, Int) -> Int)? = null
```

**(파라미터타입) -> 반환타입**
**(int, String) -> Unit**

**인자로 받은 함수 호출**
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
```

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

