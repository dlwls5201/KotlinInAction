# 람다로 프로그래밍

람다 식 또는 람다는 기본적으로 다른 함수에 넘길 수 있는 작은 코드 조각을 뜻한다.

## 람다 식과 맴버 참조

람다 식을 사용하면 함수를 선언할 필요가 없고 코드 블록을 직접 함수의 인자로 전달할 수 있다.


**람다와 컬렉션**

사람들로 이뤄진 리스트가 있고 그중에 가장 연장자를 찾아보자.

사람 클래스
```kotlin
data class Person(val name: String, val age: Int)
```

컬렉션을 직접 검색하기
```kotlin
fun findTheOldest(people: List<Person>) {
        var maxAge = 0 // 가장 많은 나이를 저장한다.
    var theOldest: Person? = null // 가장 연장자인 사람을 저장한다.

    for(person in people) {
        if(person.age > maxAge) {
            maxAge = person.age
            theOldest = person
        }
    }

    println(theOldest)
}
```

람다를 사용해 컬렉션 검색하기
```kotlin
println(people.maxBy { it.age })
```
모든 컬렉션 대해 maxBy 함수를 호출할 수 있다. maxBy는 가장 큰 원소를 찾기 위해 비교에 사용할 값을 돌려주는 함수를 인자로 받는다.

```kotlin
println(people.maxBy(Person::age))
```
이런식으로 단지 함수나 프로퍼티를 반환하는 역활을 수행하는 람다는 멤버 참조로 대치할 수 있다.

**람다 식의 문법**

```kotlin
    //코틀린 람다 식은 항상 중괄호로 둘러싸여 있다.
    //화살표가 인자 목록과 람다 본문을 구분해준다.
    val sum = { x:Int, y: Int -> x + y}
    println(sum(1,2))

    //sum 과 같은 기능
    people.maxBy({ p: Person -> p.age })

    //파라미터 타입 생략
    people.maxBy({ p -> p.age })

    //람다가 유일한 인자이므로 마지막 인자이이기도 하다. 따라서 괄호 뒤에 람다를 둘 수 있다.
    people.maxBy() { p -> p.age }

    //람다가 어떤 함수의 유일한 인자이고 괄호 뒤에 람다를 썼다면 호출 시 빈 괄호를 없애도 된다.
    people.maxBy { p -> p.age }
```

실행 시점에 코틀린 람다 호출에는 아무 부가 비용이 들지 않으며, 프로그램의 기본 구성 요소와 비슷한 성능을 낸다. 8.2절에서는 그 이유를 설명한다.

**현재 영역에 있는 변수에 접근**

자바 메소드 안에서 무명 내부 클래스를 정의할 때 메소드의 로컬 변수를 무명 내부 클래스에서 사용할 수 있다. 람다 안에서도 같은 일을 할 수 있다.

함수 파라미터를 람다 안에서 사용하기
```kotlin
fun printMessageWithPrefix(messages: Collection<String>, prefix: String) {
    messages.forEach {
        println("$prefix $it")
    }
}

//호출
val errors = listOf("403 Forbidden", "404 Not Found")
printMessageWithPrefix(errors, "Error:")
```

람다 안에서 바깥 함수의 로컬 변수 변경하기
```kotlin
fun printProblemCounts(responses: Collection<String>) {
    var clientErrors = 0
    var serverErrors = 0
    responses.forEach {
        if(it.startsWith("4")) {
            clientErrors++
        } else if(it.startsWith("5")) {
            serverErrors++
        }
    }
    println("$clientErrors client error , $serverErrors server error")
}

//호출
val responses = listOf("200 OK", "418 I'm a teapot", "500 Internal Server Error")
printProblemCounts(responses)
```
코틀린에서는 자바와 달리 람다에서 람다 밖 함수에 있는 파이널이 아닌 변수에 접근할 수 있고, 그 변수를 변경할 수도 있다.

**멤버 참조**

코틀린에서는 자바 8과 마찬가지로 함수를 값으로 바꿀 수 있다. 이때 이중 콜론(::)을 사용한다.

```kotlin
val getAge = Person::age
```

::를 사용하는 식을 멤버 참조라고 부른다. 멤버 참조는 프로퍼티나 메소드를 단 하나만 호출하는 함수 값을 만들어준다. ::는 클래스 이름과 여러분이 참조하려는 멤버(프로퍼티나 메소드) 이름 사이에 위치한다.

**클래스::맴버**

맴버 참조는 그 멤버를 호출하는 람다와 같은 타입이다. 따라서 다음 예처럼 그 둘을 자유롭게 바꿔 쓸 수 있다.

```kotlin
people.maxBy(Person::age)
people.maxBy { p -> p.age }
people.maxBy { it.age }
```

## 컬렉션 함수형 API

### 필수적인 함수: filter와 map

**filter**

filter함수는 컬렉션을 이터레이션하면서 주어진 람다에 각 원소를 넘겨서 람다가 true를 반환하는 원소만 모은다.
```kotlin
//짝수
val list = listOf(1,2,3,4)
println(list.filter { it % 2 == 0 })

//30살 이상
val people = listOf(Person("Alice",26), Person("Bob",31), Person("Carol",31))
println(people.filter { it.age > 30 })
```

filter 함수는 컬렉션에서 원치 않는 원소를 제거한다. 하지만 filter는 원소를 반환할 수는 없다. 원소를 변환하려면 map 함수를 사용해야 한다.

**map**

map 함수는 주어진 람다를 컬렉션의 각 원소에 적용한 결과를 모아서 새 컬렉션을 만들다.

```kotlin
println(list.map { it * it })
```
각 숫자의 제곱이 모인 리스트를 반환한다.

```kotlin
println(people.filter { it.name })
```
사람의 리스트가 아니라 이름의 리스트를 출력하고 싶다면 map 으로 사람의 리스트를 이름의 리스트로 변환하면 된다.

```kotlin
println(people.filter { it.age > 30 }.map(Person::name))
```
30살 이상인 사람의 이름을 출력하는 예제이다.

### all, any, count, find: 컬렉션에 술어 적용

- all, any : 컬렉션의 모든 원소가 어떤 조건을 만족하는지 판단하는 연산 -> boolean 반환
- count : 조건을 만족하는 원소의 개수를 반환
- find : 조건을 만족하는 첫 번째 원소를 반환

사람의 나이가 27살 이하인지 판단하는 술어 함수
```kotlin
val canBeInClub27 = { p: Person -> p.age <= 27}
```

```kotlin
    //all - 모든 원소가 술어를 만족하는지
    println(people.all(canBeInClub27))

    //any - 술어를 만족하는 원소가 하나라도 있는지
    println(people.any(canBeInClub27))
    println(people.any { it.age <= 27 })

    //count - 조건을 만족하는 원소의 개수를 반환
    println(people.count(canBeInClub27))

   //find - 조건을 만족하는 첫 번째 원소를 반환 없으면 null 반환 firstOrNull() 같다
   //조건을 만독하는 원소가 없으면 null이 나온다는 사실을 더 명확히 하고 싶다면 firstOrNull을 쓸 수 있다.
    println(people.find(canBeInClub27))
    println(people.firstOrNull(canBeInClub27))
```

### groupBy: 리스트를 여러 그룹으로 이뤄진 맵으로 변경

컬렉션의 모든 원소를 어떤 특성에 따라 여러 그룹으로 나누고 싶다고 하자. 예를 들어 사람을 나이에 따라 분류해 보자.
```kotlin
println(people,groupBy { it.age })
```


