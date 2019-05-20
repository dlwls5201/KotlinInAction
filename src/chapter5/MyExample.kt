package chapter5

fun main() {

    /**
     * map() 함수는 컬렉션 내 인자를 다른 값 혹은 타입으로 변활할 떄 사용합니다.
     */
    val cities = listOf("Seoul", "Tokyo", "Mountain View", "Seoul", "Tokyo")

    //도시 이름을 대문자로 변환합니다.
    cities.map { it.toUpperCase() }.forEach { println(it)}

    /**
     * flatMap() 함수는 map() 함수와 유사하게 컬렉션 내 인자를 다른 형태로 변환해주는 역활을 합니다.
     * 하지만 map() 함수와 달리 flatMap() 함수는 변환 함수의 반환형이 Interable입니다.
     * 따라서 하나의 인자에서 여러 개의 인자로 매핑이 필요한 경우에 사용합니다.
     */
    val numbers = 1..6

    //1부터 시작하여 각 인자를 끝으로 하는 범위를 반환합니다.
    numbers.flatMap { number -> 1..number }.forEach { print("$it ") }

    /**
     * groupBy() 함수는 컬렉션 내 인자들을 지정한 기준에 따라 분류하며, 각 인자들의 리스트를 포함하는 맵 형태로 결과를 반환합니다.
     */
    // 도시 이름의 길이가 5 이하면 A 그룹에, 그렇지 않으면 B 그룹에 대입합니다.
    cities.groupBy { city -> if (city.length <= 5) "A" else "B" }
        .forEach { key, cities -> println("key = $key cities = $cities ") }

    /**
     * distinct() 함수는 컬렉션 내에 포함된 항목 중 중복된 항목을 길러낸 결과를 반환합니다. 이때 항목의 중복 여부는 equals()로 판단하며,
     * distinctBy() 함수를 사용하면 비교에 사용할 키 값을 직접 설정할 수 있습니다.
     */
    // 도시 목록 중 중복된 항목을 제거합니다.
    cities.distinct().forEach { println(it) }

    // 중복된 항목을 판단할 때, 도시 이름의 길이를 판단 기준으로 사용합니다.
    cities.distinctBy { city -> city.length }
        .forEach { println(it) }

    /**
     * zip() 함수는 두 컬렉션 내의 자료들을 조합하여 새로운 자료를 만들 때 사용합니다.
     */
    //도시 코드를 담은 리스트로 4개의 자료를 가지고 있습니다.
    val cityCodes = listOf("SEO","TOK","MTV","NYC")

    //도시 이름을 담은 리스트로 3개의 자료를 가지고 있습니다.
    val cityNames = listOf("Seoul","Tokyo","Mountain View")

    //단순히 zip 함수를 호출하는 경우 Pair 형태로 자료를 조합합니다.
    cityCodes.zip(cityNames)
        .forEach { pair -> println("${pair.first}:${pair.second}")}

    //조합할 자료의 타입을 조합 함수를 통해 지정하면 해당 형태로 바꿔줍니다.
    cityCodes.zip(cityNames) { code, name -> "$code ($name)"}

    /**
     * count() 함수는 컬렉션 내 포함된 자료의 개수를 반환하며, 별도의 조건식을 추가하면 해당 조건을 만족하는 자료의 개수를 반환하도록 할 수 있습니다.
     */
    println(cities.count())

    println(cities.count { city -> city.length <= 5 })

    /**
     * reduce() 함수는 컬렉션 내 자료들을 모두 합쳐 하나의 값으로 만들어주는 역활을 합니다.
     */
    println(cities.reduce { acc, s -> "$acc, $s" })

    /**
     * fold() 함수는 reduce() 함수와 거의 동일한 역활을 하나, 초깃값을 지정할 수 있습니다.
     */
    println(cities.fold("Initial") { acc, s -> "$acc, $s" })
}