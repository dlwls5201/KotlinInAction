package chapter7

class Demo {

    interface Base {
        fun printX()
    }

    class BaseImpl(val x: Int) : Base {
        override fun printX() { print(x) }

        private var y : Int = 100
        fun printY() { print(y) }
    }

    val baseImpl = BaseImpl(10)

    class Derived(base: Base) : Base by base

    fun test() {
        Derived(baseImpl).printX()
    }

    data class Length(var centimeters: Int = 0) {

        var temp = 0
            set(value) {
                if(value > 0) field = value
            }
    }

    var Length.meters: Float
        get() {
            return centimeters / 100.0f
        }
        set(meters: Float) {
            this.centimeters = (meters * 100.0f).toInt()
        }

    fun test2() {
        println("centimeters : ${Length(100).centimeters}")
        println("meters : ${Length(100).meters}")
    }
}
