package chapter7

import java.time.LocalDate

/**
 *  컬렉션과 범위에 대해 쓸 수 있는 관레
 */
//in 관례
data class Rectangle(val upperLeft: Point, val lowerRight: Point)

operator fun Rectangle.contains(p: Point): Boolean {
    return p.x in upperLeft.x until lowerRight.x &&
            p.y in upperLeft.y until lowerRight.y
}

//rangeTo 관레

//for 루프를 위한 iterator 관례
operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
        object : Iterator<LocalDate> {
            var current = start
            override fun hasNext() = current <= endInclusive

            override fun next() = current.apply {
                current = plusDays(1) //현재 날짜를 1일 뒤로 변경한다.
            }
        }


fun main() {

    val rect = Rectangle(Point(10,20), Point(50,50))
    println(Point(20, 30) in rect)
}