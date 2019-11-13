package chapter7

class Demo2 {

    interface Vehicle {
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

    fun test() {
        val myAirbus330
                = CarOrAirplane("Lamborghini", CarImpl("Seoul"))
        val myBoeing337
                = CarOrAirplane("Boeing 337", AirplaneImpl("Seoul"))

        myAirbus330.tellMeYourTrip()
        myBoeing337.tellMeYourTrip()
    }

}