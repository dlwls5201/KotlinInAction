package chapter7

/**
 *  컬렉션과 범위에 대해 쓸 수 있는 관레
 */
//in 관례
//in은 객체가 컬렉션에 들어있는지 검사한다.
//in 연산자와 대응하는 함수는 contains다
//어떤 점이 사각형 영역에 들어가는지 판단할 때 in 연산자를 사용하게 구현해보자
data class Rectangle(val upperLeft: Point, val lowerRight: Point)

operator fun Rectangle.contains(p: Point): Boolean {
    return p.x in upperLeft.x until lowerRight.x &&
            p.y in upperLeft.y until lowerRight.y
}

//rangeTo 관레
//
fun main() {

    val rect = Rectangle(Point(10,20), Point(50,50))
    println(Point(20, 30) in rect)
}