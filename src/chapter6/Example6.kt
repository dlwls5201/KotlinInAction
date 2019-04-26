package chapter6

class Person(val firstName: String, val lastName: String) {
    override fun equals(other: Any?): Boolean {

        val otherPerson = other as? Person ?: return false

        return otherPerson.firstName == firstName &&
                otherPerson.lastName == lastName
    }

    override fun hashCode(): Int =
            firstName.hashCode() * 37 + lastName.hashCode()
}

fun verifyUserInput(input: String?) {
    if(input.isNullOrBlank()) {
        println("Please fill in the required fields")
    }
}

fun addValidNumbers1(numbers: List<Int?>) {
    var sumOfValidNumbers = 0
    var invalidNumbers = 0

    for(number in numbers) {
        if(number != null) {
            sumOfValidNumbers += number
        } else {
            invalidNumbers++
        }
    }

    println("Sum of valid numbers: $sumOfValidNumbers")
    println("Invalid numbers: $invalidNumbers")
}

fun addValidNumbers2(numbers: List<Int?>) {
    val validNumbers = numbers.filterNotNull()
    println("Sum of valid numbers: ${validNumbers.sum()}")
    println("Invalid numbers: ${numbers.size - validNumbers.size}")
}

fun main() {
    verifyUserInput(" ")
    verifyUserInput(null)

    val letters = Array(26) { i -> ('a' + i).toString()}
    println(letters.joinToString(""))

    val fiveZeros = IntArray(5)
    val fiveZerosToo = intArrayOf(0, 0, 0, 0, 0)

    println(fiveZeros.joinToString(""))
    println(fiveZerosToo.joinToString(""))

}