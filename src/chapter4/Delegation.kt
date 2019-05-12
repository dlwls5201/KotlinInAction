package chapter4

// C를 생성하고, A에서 정의하는 B의 모든 메서드를 C에 위임합니다.
// class C : A by b


interface Base {
    fun printX()
}

class BaseImpl(val x: Int) : Base {
    override fun printX() { println(x) }

    private var y : Int = 100

    fun printY() { println(y) }
}
val baseImpl = BaseImpl(10)

//Derived 를 생성하고 Base 에서 정의하는 baseImpl 의 모든 메소드를 Derived 에 위임하겠다.
class Derived(baseImpl: Base) : Base by baseImpl {

    private var z : Int = 1000

    fun printZ() { println(z)}
}

fun main() {

    Derived(baseImpl).printX()

    baseImpl.printY()

    Derived(baseImpl).printZ()

}




/*interface Vehicle {
    fun go(): String
}
class CarImpl(val where: String): Vehicle {
    override fun go() = "is going to $where"
}
class AirplaneImpl(val where: String): Vehicle {
    override fun go() = "is flying to $where"
}
class CarOrAirplane(
    val model: String,
    impl: Vehicle
): Vehicle by impl {
    fun tellMeYourTrip() {
        println("$model ${go()}")
    }
}

fun main() {

    val myAirbus330
            = CarOrAirplane("Lamborghini", CarImpl("Seoul"))
    val myBoeing337
            = CarOrAirplane("Boeing 337", AirplaneImpl("Seoul"))

    myAirbus330.tellMeYourTrip()
    myBoeing337.tellMeYourTrip()

}*/
