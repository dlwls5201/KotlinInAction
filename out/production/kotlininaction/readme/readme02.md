# 코클린 기초

## 식이 본문인 함수

**볼록이 본문인 함수**
```kotlin
fun max(a: Int, b: Int): Int {
    return if (a > b) a else b
}
```

**식이 본문인 함수**
```kotlin
fun max(a: Int, b: Int) = if (a > b) a else b
```

사용자가 반환 타입을 적지 않아도 컴파일러가 함수 본문 식을 분석해서 식의 결과 타입을 함수 반환 타입으로 정해준다. 이러한 기능을 **타입 추론**이라 부른다.

**커스텀 접근자**
get()



```kotlin
class Rectangle(val height:Int, val width:Int) {
    val isSquare: Boolean get() {
        return height == width
    }
}
```

## 선택 표현과 처리: enum과 when


**enum 클래스 정의**

```kotlin
enum class Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, INDIFO, VIOLET
}
```

**프로퍼티와 메소드가 있는 enum 클래스 선언하기**

```kotlin
enum class Color(
    val r:Int, val g:Int, val b:Int //상수 프로퍼티를 정의한다.
) {
    RED(200,0,0), ORANGE(255,165,0),
    YELLOW(255,255,0) , GREEN(0,255,0), BLUE(0,0,255),
    INDIGO(75,0,130), VIOLET(238,130,238); // enum 클래스 안에 메소드를 정의하는 경우 세미콜론을 사용한다.

    fun rgb() = (r * 256 + g) * 256 + b
}
```


**스마트 캐스트: 타입 검사와 타입 캐스트를 조합**

```kotlin
interface Expr

class Num(val value: Int) : Expr

class Sum(val left: Expr, val right: Expr) : Expr

fun eval(e: Expr) : Int =
        when(e) {
            is Num ->
                e.value
            is Sum ->
                eval(e.left) + eval(e.right)
            else ->
                throw IllegalArgumentException("UnKnown expression")
        }
```

## 수에 대한 이터레이션: 범위와 수열

**when을 사용해 피즈버즈 게임 구현하기**
```kotlin
fun fizzBuzz(i: Int) = when {
    i % 15 == 0 -> "FizzBuzz"
    i % 3 == 0 -> "Fizz"
    i % 5 == 0 -> "Buzz"
    else -> "$i"
}
```

**맵에 대한 이터레이션**
```kotlin
    for(c in 'A'..'F') {
        val binary = Integer.toBinaryString(c.toInt())
        binaryReps[c] = binary
    }

    for ((letter, binary) in binaryReps) {
        println("$letter = $binary")
    }

    //맵에 사용했던 구조 분해 구문을 맵이 아닌 컬렉션에도 활용할 수 있다.
    val list = arrayListOf("10", "11", "1001")
    for((index, element) in list.withIndex()) {
        println("$index : $element")
    }
```

**in으로 컬렉션이나 범위의 원소 검사**
```kotlin
//1 ~ 100 까지 수헹
    for(i in 1..100) {
        //println(fizzBuzz(i))
    }

    //역순으로 2씩 증가
    for(i in 100 downTo 1 step 2) {
        //println(fizzBuzz(i))
    }

    //100을 포함하지 않는경우 until 사용
    for(i in 1 until 100) {
        //println(fizzBuzz(i))
    }
```

**when 에서 in 사용하기**
```kotlin
fun recognize(c: Char) = when (c) {
    in '0'..'9' -> "It's a digit!"
    in 'a'..'z' -> "It's a letter!"
    else -> "I don't know"
}
```