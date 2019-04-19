package chapter3

import java.lang.IllegalArgumentException

/**
 * 코드 다듬기: 로컬 함수와 확장
 */
//코틀린에서는 함수에서 추출한 함수를 원 함수 내부에 중첩시킬 수 있다.

class User3(val id: Int, val name: String, val address: String)

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

//로컬 함수를 사용해 코드 중복 줄이기
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

//로컬 함수에서 바깥 함수의 파라미터 접근하기
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

//검증 로직을 확장 함수로 추출하기
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