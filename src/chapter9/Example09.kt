package chapter9

open class Animal {
    fun feed() {}
}

class Herd<out T: Animal> {
    private val animals = arrayListOf<T>()

    val size: Int get() = animals.size

    operator fun get(i: Int): T {
        return animals[i]
    }
}

fun feedAll(animals: Herd<Animal>) {
    for (i in 0 until animals.size) {
        animals[i].feed()
    }
}

class Cat : Animal() {
    fun cleanLitter() {}
}

fun takeCareOfCats(cats: Herd<Cat>) {
    for (i in 0 until cats.size) {
        cats[i].cleanLitter()
        feedAll(cats) // Error: Type mismatch, Required : Herd<Animal> Found : Herd<Cat>
    }
}

fun main() {


}