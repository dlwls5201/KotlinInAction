package chapter8

import java.util.concurrent.locks.Lock

inline fun <T> synchronized(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    }
    finally {
        lock.unlock()
        lock.unlock()
        lock.unlock()
    }
}

fun main() {

}