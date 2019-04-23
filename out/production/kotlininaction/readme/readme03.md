# 함수 정의와 호출

## 코틀린에서 컬렉션 만들기

코틀린 컬렉션은 자바 컬렉션과 똑같은 클래스다. 하지만 코틀린에서는 자바보다 더 많은 기능을 쓸 수 있다.

```kotlin
val set = hashSetOf(1,7,53)
val list = arrayListOf(1,7,53)
val map = hashMapOf(1 to "one", 7 to "seven", 53 to "fifty-three")
```

## 함수를 호출하기 쉽게 만들기

**JoinToString 을 제너릭을 사용해 만들어보기**

```kotlin
fun <T> joinToString(
    collection: Collection<T>,
    separator: String,
    prefix: String,
    postfix: String
): String {
    val result = StringBuilder(prefix)
    for((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

  //함수 호출
  val list = listOf(1,2,3)
  println(joinToString(list, "; ", "(", ")"))

```

**디폴트 파라미터 값을 사용해 joinToString() 정의하기**

```kotlin
fun <T> joinToString2(
    collection: Collection<T>,
    separator: String = "",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

  //이름 붙인 인자
  val list = listOf(1,2,3)
  println(joinToString(list, separator = "; ", prefix = "(", postfix = ")"))

```


## 메소드를 다른 클래스에 추가: 확장 함수와 확장 프로퍼티

기존 자바 API를 재작성하지 않고도 코틀린이 제공하는 여러 펴니한 기능을 할 수 있는 개녑이 확장 함수이다.
**확장 함수는 어떤 클래스의 멤버 메소드인 것처럼 호출할 수 있지만 그 클래스의 밖에 선언된 함수다.**

어떤 문자열의 마지막 문자를 돌려주는 메소드
```kotlin
fun String.lastChar(): Char = this.get(this.length - 1)
```

joinToString()를 확장으로 정의하기
```kotlin
fun <T> Collection<T>.joinToString3(
    separator: String = "",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for((index, element) in withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

//main
    //확장함수는 오버라이드 할 수 없다.
    val view: View = Button()
    view.click()
    view.showOff()

    val viewButton = Button()
    viewButton.click()
    viewButton.showOff()
```kotlin

확장함수는 오버라이드 할 수 없다.
```kotlin
open class View {
    open fun click() = println("View clicked")
}

class Button: View() {
    override fun click() = println("Button clicked")
}

fun View.showOff() = println("I'm a view!")

fun Button.showOff() = println("I'm a button!")
```


## 코드 다듬기: 로컬 함수와 확장

코틀린에서는 함수에서 추출한 함수를 원 함수 내부에 중첩시킬 수 있다.

```kotlin
class User3(val id: Int, val name: String, val address: String)
```

```kotlin
fun saveUser(user3: User3) {
    if(user3.name.isEmpty()) {
        throw IllegalArgumentException(
            "Can't save user3 ${user3.id}: empty Name"
        )
    }

    if(user3.address.isEmpty()) {
        throw IllegalArgumentException(
            "Can't save user3 ${user3.address}: empty Address"
        )
    }
}
```

로컬 함수를 사용해 코드 중복 줄이기

```kotlin
fun saveUser1(user3: User3) {

    fun validate(user3: User3, value: String, fieldName: String) {
        if(value.isEmpty()) {
            throw  IllegalArgumentException(
                "Can't save user3 ${user3.id}: empty $fieldName"
            )
        }
    }

    validate(user3, user3.name, "Name")
    validate(user3, user3.address, "Address")
}
```

로컬 함수에서 바깥 함수의 파라미터 접근하기
```kotlin
fun saveUser2(user3: User3) {

    fun validate(value: String, fieldName: String) {
        if(value.isEmpty()) {
            throw  IllegalArgumentException(
                "Can't save user3 ${user3.id}: empty $fieldName"
            )
        }
    }

    validate(user3.name, "Name")
    validate(user3.address, "Address")
}
```

검증 로직을 확장 함수로 추출하기
```kotlin
fun User3.validateBeforeSave() {
    fun validate(value: String, fieldName: String) {
        if(value.isEmpty()) {
            throw  IllegalArgumentException(
                "Can't save user $id: empty $fieldName")
        }
    }

    validate(name, "Name")
    validate(address, "Address")
}

fun saveUser3(user3: User3) {
    user3.validateBeforeSave()
}

fun main() {
    saveUser(User3(1,"",""))
}
```