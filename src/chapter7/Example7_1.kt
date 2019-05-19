package chapter7

/**
 * 산술 연산자 오버로딩
 */
data class Point(val x: Int, val y: Int) {
    //...
}

operator fun Point.plus(other: Point) : Point {
    return Point(x + other.x, y + other.y)
}

operator fun Point.unaryMinus(): Point {
    return Point(-x,-y)
}

fun main() {

    //이항 산술 연산 오버로딩
    val p1 = Point(10,20)
    val p2 = Point(30,40)
    println(p1 + p2)

    //복합 대인 연산자 오버로딩
    var point = Point(1,2)
    point += Point(3,4)
    println(point)

    val list = arrayListOf(1,2)
    list += 3

    val newList = list + listOf(4,5)

    println(list)
    println(newList)

    //단항 연산자 오버로딩
    val p = Point(10,20)
    println(-p)

}