package chapter9

//(P) -> R 은 Function<P, R>을 더 알아보기 쉽게 적은 것이다. -> 함수 타입 (T) -> R 은 인자의 타입에 대해서는 반공변적이면서 반환 타입에 대해서는 공변적이다.
//P(함수 파라미터의 타입)는 오직 인 위치, R(함수 반환 타임)은 오직 아웃 위치에 사용된다는 사실과 그에 따라 P와 R에 각각 in과 out 표시가 붙어 있을을 볼 수 있다.
interface Function1<in P, out R> {
    operator fun invoke(p: P) : R
}

//정리 예제
//Cat 은 in 이고 Number 은 out 이다.
fun enumerateCats(f: (Cat) -> Number) { }

fun Animal.getIndex(): Int = 0

fun main() {
    //Animal 은 Cat 의 상위 타입이며 Int 는 Number 의 하위 타입이므로,
    //이 코드는 올바른 코틀린 식이다.
    enumerateCats(Animal::getIndex)
}