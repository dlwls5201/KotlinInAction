package chapter9

//https://medium.com/@lazysoul/%EA%B3%B5%EB%B3%80%EA%B3%BC-%EB%B6%88%EB%B3%80-297cadba191
interface Some<T> {
    fun something()
}

open class Language

open class JVM : Language()

class Kotlin : JVM()

fun main() {

    val language: Some<Language> = object : Some<Language> {
        override fun something() {
            println("language")
        }
    }

    val jvm: Some<JVM> = object : Some<JVM> {
        override fun something() {
            println("jvm")
        }
    }

    val kotlin: Some<Kotlin> = object : Some<Kotlin> {
        override fun something() {
            println("kotlin")
        }
    }

    //test(language)
    //test(jvm)
    //test(kotlin)
}

//무공변성 ? 상속 관계에 상관없이, 자기 타입만을 혀용하는 것
/**
 *  test(language) // error
 *  test(jvm) // ok
 *  test(kotlin) // error
 */
//fun test(value: Some<JVM>) { }


//공변성 ? 타입생성자에게 리스코프 치환 법칙을 허용한다는 의미
/**
 *  test(language) // error
 *  test(jvm) // ok
 *  test(kotlin) // ok
 */
//fun test(value: Some<out JVM>) { }

//반공변성 : 공변성의 반대 개념으로 자기 자신과 부모 객체만 허용하는 것
/**
 *  test(language) // ok
 *  test(jvm) // ok
 *  test(kotlin) // error
 */
//fun test(value: Some<in JVM>) { }