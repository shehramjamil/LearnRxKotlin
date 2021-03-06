package operators

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

// List of Filtering Operators
// debounce
// distinct and distinctUntilChanged
// elementAt
// Filter
// first and last
// ignoreElements
// skip, skipLast, skipUntil, and skipWhile
// take, takeLast, takeUntil, and takeWhile

fun main() {

    //debounce()
    //distinct()
    //distinctUntilChanged()
    //elementAt()
    //filter()
    //firstAndLast()
    //ignoreElements()

    //skip()
    //skipLast()
    //skipWhile(), unlike filter, it terminates when condition becomes false and start emission further
    //skipUntil()

    //totally opposite of skip operator, implementation is skipped
    //take()
    //takeLast()
    //takeWhile()
    //takeUntil()
}

fun debounce() {
    // Debounce is useful in a case when we want to wait for the user to stop typing while implementing UI.
    // It waits for the input based on the time specified in its parameter.

    // In the following example, when an item is emitted, debounce wait for 200 ms, if no other item emitted after
    // waiting for 200 ms, that item will be delivered to the subscriber (consumer) and if an item emitted before 200 ms,
    // then debounce ignore tha old item and  hold that new item again for 200 ms and do the same thing again.

    val observable = Observable.create<Int> {
        it.onNext(1)
        runBlocking { delay(50) }
        it.onNext(2)
        runBlocking { delay(100) }
        it.onNext(3)
        runBlocking { delay(150) }
        it.onNext(4)
    }

    observable.debounce(200, TimeUnit.MILLISECONDS).subscribe {
        println("debounce operator =  $it")
    }
    runBlocking { delay(5000) }


}

fun distinct() {
    // Remove the duplicated items
    listOf(1, 2, 2, 3, 4, 5, 5, 5, 6, 7, 8, 9, 3, 10)
        .toObservable()
        .distinct()
        .subscribe { println("Distinct Operator =  $it") }
}

fun distinctUntilChanged() {
    // Remove only the consecutive duplicated items
    listOf(1, 2, 2, 3, 4, 5, 5, 5, 2, 6, 7, 8, 9, 3, 10)
        .toObservable()
        .distinctUntilChanged()
        .subscribe { println("Distinct Until Changed Operator =  $it") }
}

fun elementAt() {
    // to get the element at an index
    val observable = listOf(10, 1, 2, 5, 8, 6, 9)
        .toObservable()
    observable.elementAt(5)
        .subscribe { println("Element at Operator =  $it") }
}

fun filter() {
    // filters according to the condition put in filter lamda function
    listOf(1, 2, 3, 4, 5).toObservable().filter {
        it - 2 == 0
    }.subscribe {
        println("Filter Operator = $it")
    }
}

fun firstAndLast() {
    // To emit first and last value in a list
    // we can also set default value if we get empty list

    val observable = Observable.range(1, 10)
    observable.first(2)
        .subscribeBy { item -> println("Received $item") }
    observable.last(2)
        .subscribeBy { item -> println("Received $item") }
    Observable.empty<Int>().first(2)
        .subscribeBy { item -> println("Received $item") }
}

fun ignoreElements() {
    // Sometimes, you may require to listen only on the onComplete of a producer. The
    // ignoreElements operator helps you to do that.
    val observable = Observable.range(1, 10)
    observable
        .ignoreElements()
        .subscribe { println("Completed") }
}

fun skip() {
    // It takes a count as parameter and skip untill that count reaches

    Observable.range(1, 20).skip(5).subscribe {
        println("Items after skip = $it")
    }

    // Skip also takes time as parameter to skip items for a particular time
    Observable.range(1, 100000000).skip(400, TimeUnit.MILLISECONDS).subscribe {
        println("Items after skip = $it")
    }
    runBlocking { delay(1000) }
}

fun skipLast() {
    Observable.range(1, 20).skipLast(5).subscribe {
        println("Items after skip = $it")
    }

}

fun skipWhile() {
    // it takes a logic to be true for skipping the elements
    Observable.range(1, 200).skipWhile {
        it < 100
    }.subscribe {
        println(it)
    }
}

fun skipUntil() {

    // Think of a situation where you're working with two producers, producer1 and producer2,
    // and want to start processing emissions from producer1 as soon as producer2 starts emitting.

    // here observable start emitting but those emission will be skipped until 100 ms, emissions after 100 ms
    // will be consumed by the subscriber

    val observable1 = Observable.range(1,10000000)
    val observable2 = Observable.interval(100, TimeUnit.MILLISECONDS)
    observable1.skipUntil(observable2)
        .subscribe{
            println(it)
        }
    runBlocking { delay(5000)}

}

fun take(){}
fun takeLast(){}
fun takeWhile(){}
fun takeUntil(){}