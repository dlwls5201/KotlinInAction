package chapter7


data class Point(val x: Int, val y: Int) {
    //...
}

operator fun Point.plus(other: Point) : Point {
    return Point(x + other.x, y + other.y)
}

fun main() {

    val p1 = Point(10,20)
    val p2 = Point(30,40)
    println(p1 + p2)

    var point = Point(1,2)
    point += Point(3,4)
    println(point)

    val list = arrayListOf(1,2)
    list += 3 // +=는 'list'를 변경한다.

    val newList = list + listOf(4,5) // +는 두 리스트의 모든 원소를 포함하는 새로운 리스트를 반환한다.

    println(list)
    println(newList)
}