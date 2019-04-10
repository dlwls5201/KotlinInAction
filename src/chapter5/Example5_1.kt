package chapter5

data class Person(val name: String, val age: Int)

/**
 *  자바 컬렉션에 대해 수행하던 대부분의 작업은 람다나 맴버 참조를 인자로 취하는 라이브러리 함수를 통해 개선할 수 있다.
 */
fun main() {

    val people = listOf(Person("Alice",29), Person("Bob",31))
    findTheOldest(people)

    //람다를 사용해 컬렉션 검색하기
    //나이 프로퍼티를 비교해서 값이 가장 큰 원소 찾기
    //println(people.maxBy { it.age })
    //println(people.maxBy(Person::age))

    //코틀린 람다 식은 항상 중괄호로 둘러싸여 있다.
    //화살표가 인자 목록과 람다 본문을 구분해준다.
    val sum = { x:Int, y: Int -> x + y}
    //println(sum(1,2))

    //sum 과 같은 기능
    people.maxBy({ p: Person -> p.age })

    //파라미터 타입 생략
    people.maxBy({ p -> p.age })

    //람다가 유일한 인자이므로 마지막 인자이디고 하다. 따라서 괄호 뒤에 람다를 둘 수 있다.
    people.maxBy() { p -> p.age }

    //람다가 어떤 함수의 유일한 인자이고 괄호 뒤에 람다를 썼다면 호출 시 빈 괄호를 없애도 된다.
    people.maxBy { p -> p.age }


    /**
     *  5.1.5 멤버참조
     *
     *  코틀린에서는 자바 8과 마찬가지로 함수를 값으로 바꿀 수 있다. 이때 이중 콜론을 사용한다.
     *  멤버 참조는 프로퍼티나 메소드를 단 하나만 호출하는 함수 값을 만들어 준다.
     *  :: 는 클래스 이름과 참조할러는 멤버(프로퍼티나 메소드) 이름 사이에 위치한다.
     */
    people.maxBy(Person::age)


    //함수 파라미터를 람다 안에서 사용하기
    val errors = listOf("403 Forbidden", "404 Not Found")
    //printMessageWithPrefix(errors, "Error:")

    //람다 안에서 바깥 함수의 로컬 변수 변경하기
    val responses = listOf("200 OK", "418 I;m a teapot", "500 Internal Server Error")
    printProblemCounts(responses)
}

//람다를 사용해 컬렉션 검색하기
fun findTheOldest(people: List<Person>) {
    var maxAge = 0
    var theOldest: Person? = null

    for(person in people) {
        if(person.age > maxAge) {
            maxAge = person.age
            theOldest = person
        }
    }

    println(theOldest)
}

//함수 파라미터를 람다 안에서 사용하기
fun printMessageWithPrefix(messages: Collection<String>, prefix: String) {
    messages.forEach {
        println("$prefix $it")
    }
}

//람다 안에서 바깥 함수의 로컬 변수 변경하기
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
