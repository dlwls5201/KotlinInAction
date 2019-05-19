package chapter5

import java.lang.StringBuilder

fun main() {

    val people = listOf(Person("Alice",26), Person("Bob",31), Person("Carol",31))

    /**
     *  지연 계산 컬렉션 연산
     */
    //시퀀스 인터페이스의 강점은 인터페이스 위에 구현된 연산이 계산을 수행하는 방법 떄문에 생긴다.
    //시퀀스 원소는 필요할 때 비로소 계산된다. 따라서 중간 처리 결과를 저장하지 않고도 연산을 연쇄적으로 적용해서 효율적으로 계산을 수행 할 수 있다.
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
    //println(people.asSequence().map(Person::name).filter { it.length < 4 }.toList())

    //println(people.asSequence().filter { it.name.length < 4}.map(Person::name).toList())

}