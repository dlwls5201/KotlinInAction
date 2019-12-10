package chapter9

//공변
//A가 하위 타입이면 List<A>는 List<B>의 하위타입이다. 그런 클래스나 인터페이스를 공변적이라 말한다.
fun covariant() {

    val list = listOf("abc","bca")
    getCovariant(list)
}

fun getCovariant(list: List<Any>): Any {
    return list[0]
}

//A가 B의 하위 타입일 때 Producer<A>가 Producer<B>의 하위타입이면 Producer 는 공변적이다.
//코틀린에서 제네릭 클래스가 타입 파라미터에 대해 공변적임을 표시하려면 타입 파라미터 앞에 out 을 넣어야 한다.
interface Producer<out T> {
    fun produce(): T
}

open class Animal {
    fun feed() {
        println("feed")
    }
}

class Cat : Animal() {
    fun cleanLitter() {
        println("cleanLitter")
    }
}

//클래스 멤버를 선언할 때 타입 파라미터를 사용할 수 있는 지점은 모두 인과 아웃 위치로 나온다. T라는 타입 파라미터를 선언하고 T를 사용하는 함수가 멤버로 있는 클래스를 생각해보자
//T가 함수의 반환 타입에 쓰인다면 T는 아웃 위치에 있다. 그 함수는 T 타입의 값을 생산한다.
//T가 함수의 파라미터 타입에 쓰인다면 T는 인 위치에 있다. 그런 함수는 T 타입의 값을 소비한다.
class Herd<out T: Animal> {
    private val animals = arrayListOf<T>()

    val size: Int get() = animals.size


    operator fun get(i: Int): T {
        return animals[i]
    }
}

fun takeCareOfCats(cats: Herd<Cat>) {
    for(i in 0 until cats.size) {
        cats[i].cleanLitter()
    }

    //타입 안정성을 보장하기 위해 공변적 파라미터는 항상 아웃 위치에만 있어야 한다.
    //이는 클래스가 T 타입의 값을 생산할 수는 있지만 T 타입의 값을 소비할 수는 없다는 뜻이다.
    //out 을 붙이면 컴파일 에러는 해결된다.
    feedAll(cats)
}

fun feedAll(animals: Herd<Animal>) {
    for (i in 0 until animals.size) {
        animals[i].feed()
    }
}

fun main() {
    val animals = Herd<Cat>()
    takeCareOfCats(animals)
}