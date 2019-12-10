package chapter9

//무공변
//제네릭 타입을 인스턴스화할 때 타입 인자로 서로 다른 타입이 들어가면 인스턴스 타입 사이의 하위 타입 관계가 성립하지 않으면 그 제네릭 타입을 무공변이라고 말한다.
fun invariant() {

    val list = mutableListOf("abc","bca")
    //TODO addInvariant(list) // error
}

fun addInvariant(list: MutableList<Any>) {
    list.add(42)
}

//공변
//A가 하위 타입이면 List<A>는 List<B>의 하위타입이다. 그런 클래스나 인터페이스를 공변적이라 말한다.
fun covariant() {

    val list = listOf("abc","bca")
    getCovariant(list)
}

fun getCovariant(list: List<Any>): Any {
    return list[0]
}

//A가 B의 하위 타입일 때 Producer<A>가 Producer<B>의 하위타입이면 Producer는 공변적이다.
//코틀린에서 제네릭 클래스가 타입 파라미터에 대해 공변적임을 표시하려면 타입 파라미터 앞에 out을 넣어야 한다.
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

//클래스 멤버를 선언할 때 타입 파라미터를 사용할 수 있는 지점은 모두 인돠 아웃 위치로 나윈다. T라는 타입 파라미터를 선언하고 T를 사용하는 함수가 멤버로 있는 클래스를 생각해보자
//T가 함수의 반환 타입에 쓰인다면 T는 아웃 위치에 있다. 그 함수는 T 타입의 값을 생산한다.
//T가 함수의 파라미터 타입에 쓰인다면 T는 인 위치에 있다. 그런 함수는 T 타입의 값을 소비한다.
class Herd<T: Animal> {
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

//반공변성
//반공변 클래스의 하위 타입 관계는 공변 클래스의 경우와 반대다

//Comparator 인터페이스의 메소드는 T 타입의 값을 소비하기만 한다. 이는 T가 인 위치에서만 쓰인다는 뜻이다.
//in 이라는 키워드는 그 키워드가 붙은 타입이 이 클래스의 메소드 안으로 전달돼 메소드에 의해 소비된다는 뜻이다.
interface Comparator<in T> {
    fun compare(e1: T, e2: T): Int
}

fun main2() {

    val anyComparator = Comparator<Any> {
        e1, e2 -> e1.hashCode() - e2.hashCode()
    }

    //Any 는 String 의 상위 타입이지만 Comparator<Any>가 Comparator<String>의 하위 타입이 되므로 반공변성이라 할 수 있다.
    listOf<String>().sortedWith(anyComparator)
}

//정리
//(P) -> R 은 Function<P, R>을 더 알아보기 쉽게 적은 것이다. -> 함수 타입 (T) -> R 은 인자의 타입에 대해서는 반공변적이면서 반환 타입에 대해서는 공변적이다.
//P(함수 파라미터의 타입)는 오직 인 위치, R(함수 반환 타임)은 오직 아웃 위치에 사용된다는 사실과 그에 따라 P와 R에 각각 in과 out 표시가 붙어 있을을 볼 수 있다.
interface Function1<in P, out R> {
    operator fun invoke(p: P) : R
}

//정리 예제
//Cat 은 in 이고 Number 은 out 이다.
fun enumerateCats(f: (Cat) -> Number) { }

fun Animal.getIndex(): Int = 0

fun main() {
    //Animal 은 Cat 의 상위 타입이며 Int 는 Number 의 하위 타입이므로,
    //이 코드는 올바른 코틀린 식이다.
    enumerateCats(Animal::getIndex)
}