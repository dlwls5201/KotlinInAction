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
    //println(people.all(canBeInClub27))

    //술어를 만족하는 원소가 하나라도 있는지
    //println(people.any(canBeInClub27))
    //println(people.any { it.age <= 27 })

    /**
     * count
     * 조건을 만족하는 원소의 개수를 반환
     */
    //println(people.count(canBeInClub27))

    /**
     * find
     * 조건을 만족하는 첫 번째 원소를 반환
     * 없으면 null 반환 (firstOrNull() 같다)
     */
    //println(people.find(canBeInClub27))
    //println(people.firstOrNull(canBeInClub27))

    /**
     * groupBy: 리스트를 여러 그룹으로 이뤄진 맵으로 변경
     * 컬렉션의 모든 원소를 어떤 특성에 따라 여러 그룹으로 나누고 싶을 때 사용
     */
    //groupBy 의 결과 타입은 Map<Int, List<Person>> 으로 나옵니다.
    //println(people.groupBy { it.age } )
    //println(people.groupBy { it.age }[26])
    //println(people.groupBy { it.age }[31] )

    val groupBySampleList = listOf("a","ab","b")
    //println(groupBySampleList.groupBy(String::first))


    class Book(val title:String, val authors: List<String>)
    /**
     * flatMap
     * 인자로 주어진 람다를 컬렉션의 모든 객체에 적용하고 람다를 적용한 결과 얻어지는 여러 리스트를 한 리스트로 한데 모은다
     */
    val strings = listOf("abc","def")
    //println(strings.flatMap { it.toList() })

    val books = listOf(Book("Thursday Next", listOf("Jasper Fforde")),
        Book("Mort", listOf("Terry Prachett")),
        Book("Good Omens", listOf("Terry Pratchett","Neil Gaiman")))

    //println(books.flatMap { it.authors }) //arrayList
    //println(books.flatMap { it.authors }.toSet()) // LinkedHashSet
}
