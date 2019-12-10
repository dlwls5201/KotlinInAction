package chapter9

//무공변
//제네릭 타입을 인스턴스화할 때 타입 인자로 서로 다른 타입이 들어가면 인스턴스 타입 사이의 하위 타입 관계가 성립하지 않으면 그 제네릭 타입을 무공변이라고 말한다.
fun invariant() {

    val list = mutableListOf("abc","bca")
    //addInvariant(list) //TODO error
}

fun addInvariant(list: MutableList<Any>) {
    list.add(42)
}