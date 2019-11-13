package chapter7

import chapter7.Example7_5_decorator.*

class Example7_5_decorator {

    abstract class Beverage(
        open val description: String = "제목 없음"
    ) {

        abstract fun cost(): Double
    }

    class Espresso: Beverage("에스프레소") {

        override fun cost() = 1.99
    }

    class HouseBlend: Beverage("하우스 블랜드 커피") {

        override fun cost() = 0.89
    }


    class SteamMilk(val beverage: Beverage): Beverage() {

        override val description: String
            get() = "${beverage.description}, 스팀밀크"

        override fun cost() =  0.1 + beverage.cost()

    }

    class Mocha(val beverage: Beverage): Beverage() {

        override val description: String
            get() = "${beverage.description}, 모카"

        override fun cost() =  0.2 + beverage.cost()

    }
}

fun main() {

    //에스프레소 주문
    val beverage: Beverage = Espresso()
    println("${beverage.description} $${beverage.cost()}")

    //하우스블랜드에 스팀밀크, 모카 추가
    var beverage3: Beverage = HouseBlend()
    beverage3 = SteamMilk(beverage3)
    beverage3 = Mocha(beverage3)
    println("${beverage3.description} $${beverage3.cost()}")
}