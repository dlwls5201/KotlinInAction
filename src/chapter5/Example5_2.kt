package chapter5

import java.lang.StringBuilder

/**
 *  컬렉션 함수형 API
 *
 *  함수형 프로그래밍 스타일을 사용하면 컬렉션을 다룰 때 편리하다.
 */
fun main() {

    /**
     * filter
     */
    val list = listOf(1,2,3,4)
    //println(list.filter { it % 2 == 0 })

    val people = listOf(Person("Alice",26), Person("Bob",31), Person("Carol",31))
    //println(people.filter { it.age > 30 })

    /**
     * map
     * 주어진 람다를 컬렉션의 각 원소에 적용한 결과를 모아서 새 컬렉션을 만든다.
     */
    //println(list.map { it * it })

    //println(people.map { it.name })

    //println(people.filter { it.age > 30 }.map(Person::name))



    val canBeInClub27 = { p: Person -> p.age <= 27}

    /**
     * all, any
     * 컬렉션의 모든 원소가 어떤 조건을 만족하는지 판단
     */
    //all - 모든 원소가 술어를 만족하는지
    println(people.all(canBeInClub27))

    //술어를 만족하는 원소가 하나라도 있는지
    println(people.any(canBeInClub27))
    println(people.any { it.age <= 27 })

    /**
     * count
     * 조건을 만족하는 원소의 개수를 반환
     */
    println(people.count(canBeInClub27))

    /**
     * find
     * 조건을 만족하는 첫 번째 원소를 반환
     * 없으면 null 반환 (firstOrNull() 같다)
     */
    println(people.find(canBeInClub27))
    println(people.firstOrNull(canBeInClub27))

    /**
     * groupBy: 리스트를 여러 그룹으로 이뤄진 맵으로 변경
     * 컬렉션의 모든 원소를 어떤 특성에 따라 여러 그룹으로 나구고 싶을 때 사용
     */
    //groupBy 의 결과 타입은 Map<Int, List<Person>> 으로 나옵니다.
    println(people.groupBy { it.age })

    val groupBySampleList = listOf("a","ab","b")
    println(groupBySampleList.groupBy(String::first))



    class Book(val title:String, val authors: List<String>)
    /**
     * flatMap
     * 인자로 주어진 람다를 컬렉션의 모든 객체에 적용하고 람다를 적용한 결과 얻어지는 여러 리스트를 한 리스트로 한데 모은다
     */
    val strings = listOf("abc","def")
    println(strings.flatMap { it.toList() })

    val books = listOf(Book("Thursday Next", listOf("Jasper Fforde")),
        Book("Mort", listOf("Terry Prachett")),
        Book("Good Omens", listOf("Terry Pratchett","Neil Gaiman")))

    println(books.flatMap { it.authors }.toSet())


    /**
     *  지연 계산 컬렉션 연산
     */
    //시퀀스 인터페이스의 강점은 인터페이스 위에 구현된 연산이 계산을 수행하는 방법 떄문에 생긴다.
    //시퀀스 원소는 필요할 때 비로소 계산된다. 따라서 중간 처리 결과를 저장하지 않고도 연산으 ㄹ연쇄적으로 적용해서 효율적으로 계산을 수행 할 수 있다.
    //큰 컬렉션에 대해서 연산으 ㄹ연쇄시킬 때는 시퀀스를 사용하는 것을 규칙으로 삼는다.

    //map, filter 은 모두 list 를 반환하므로 2개의 리스트가 생성된다.
    people.map(Person::name).filter { it.startsWith("A") }

    //시퀀스로 변환하여 list 를 새로 생성하지 않으므로 성능이 더 좋다
    people.asSequence()
        .map(Person::name)
        .filter { it.startsWith("A") }
        .toList()

    /**
     * 시퀀스 연산 실행: 중간 연산과 최종 연산
     *
     * toList() 즉 최종 연산을 호출하지 않으면 중간 연산은 실행되지 않는다.
     */
    /*listOf(1,2,3,4).asSequence()
        .map { print("map($it)"); it*it }
        .filter { print("filter($it)"); it % 2 == 0 }
        .toList()*/

    //map을 먼저 하면 모든 원소를 변환한다. 하지만 filter를 먼저 하면 부적절한 원소를 먼저 제외하기 때문에 그런 원소는 변환되지 않는다.
    println(people.asSequence().map(Person::name)
        .filter { it.length < 4 }.toList())

    println(people.asSequence().filter { it.name.length < 4}
        .map(Person::name).toList())


    //with, apply
    println(alphabet1())
    println(alphabet2())
}

/**
 * with, apply
 */

fun alphabet1() = with(StringBuilder()) {
    for(letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet!")
    toString()
}

fun alphabet2() = StringBuilder().apply {
    for(letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet!!")
}.toString()