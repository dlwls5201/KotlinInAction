package chapter7

import kotlin.properties.Delegates

private var notNullData: String by Delegates.notNull()

private var observableData: String by Delegates.observable("Initial value") {
        property, oldValue, newValue ->
    println("${property.name}: $oldValue -> $newValue")
}

private var vetoableData: Int by Delegates.vetoable(0) {
        property, oldValue, newValue ->
    println("${property.name}: $oldValue -> $newValue")
    newValue >= 0
}

fun main() {

//    println("notNullData = $notNullData")
//    notNullData = "New value"
//    println("notNullData = $notNullData")

//    observableData = "New value"
//    observableData = "Another value"

//    vetoableData = -1
//    println("vetoableData = $vetoableData")
//    vetoableData = 1
//    println("vetoableData = $vetoableData")

}