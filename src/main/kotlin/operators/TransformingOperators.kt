package operators

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable

// map
// flatMap, concatMap, and flatMapIterable
// switchMap
// switchIfEmpty
// scan
// groupBy
// startWith
// defaultIfEmpty
// sorted
// buffer
// window
// cast
// delay
// repeat

fun main() {
    //map()
    //cast()
    //flatmap()
    //defaultIfEmpty()
    //switchIfEmpty()
    //startWith()
    //sorted()
    //scan() // accumulating the emitted item with previous emissions and emit one by one
    //reducingOperators() // accumulating the emitted item with previous emissions and emit only once in the end
    collectionOperators() //TODO
    errorHandlingOperators() // TODO
}

fun map() {
    // performs on every emitted item and push it to downstream
    Observable.just(1, 2, 3).map {
        it.plus(100).toFloat()
    }.subscribe {
        // here we got the transformed value after using map lambda function
        println(it.toString())
    }
}

fun cast() {
    // TODO
}

fun flatmap() {
    // Where the map operator takes each emission and transforms them, the flatMap operator
    // creates a new producer, applying the function you passed to each emission of the source producer.

    val observable = listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1).toObservable()
    observable.flatMap { number ->
        Observable.just("Transforming Int to String $number")
    }.subscribe { item ->
        println("Received $item")
    }

    // Another good example of flatmap
    val observable2 = listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1).toObservable()
    observable2.flatMap { number ->
        Observable.create<String> {
            it.onNext("The Number $number")
            it.onNext("number/2 ${number / 2}")
            it.onNext("number%2 ${number % 2}")
            it.onComplete()
        }
    }.subscribeBy(
        onNext = { item ->
            println("Received $item")
        },
        onComplete = {
            println("Complete")
        }
    )
}

fun defaultIfEmpty() {

    // If producer/emitter is emitting nothing or empty then we can set default values
    Observable.range(0, 10)
        .filter { it > 15 }
        .defaultIfEmpty(155)
        .subscribe {
            println("Received $it")
        }
}

fun switchIfEmpty() {

    // If the producer is empty, then it starts producing from a different producer which we have to add in
    // wow
    Observable.range(0,10)
        .filter{it>15}
        .switchIfEmpty(Observable.range(11,10))
        .subscribe{
            println("Received $it")
        }
}

fun startWith()
{
    // It adds an element above all the elements of the producer
    // we can start with an iterator or an observable or a single item
    Observable.range(0,10).startWith(Observable.just(-1))
        .subscribe {
            println("Received $it")
        }

    // or
    Observable.range(0,10).startWithItem(-1)
        .subscribe {
            println("Received $it")
        }



}

fun sorted()
{
    // makes sorting easy
    // default ascending
    // sort operator takes all the emissions and then sort it and re emit [performance issue for large data set]
    listOf("alpha","gamma","beta","theta")
        .toObservable()
        .sorted()
        .subscribe { println("Received $it") }

    // for descending
    listOf(2,6,7,1,3,4,5,8,10,9)
        .toObservable()
        .sorted { item1, item2 -> if(item1>item2) -1 else 1 }// for descending order
        .subscribe { println("Received $it") }
}

fun scan()
{
    // It adds up the latest emission with previous accumulation of all emissions

    Observable.range(1,10)
        .scan { previousAccumulation, newEmission ->
            previousAccumulation+newEmission }
        .subscribe { println("Received $it") }
}

fun reducingOperators()
{
    // count operator counts all emissions and give a count in the end
    listOf(1,5,9,7,6,4,3,2,4,6,9).toObservable()
        .count()
        .subscribeBy { println("count $it") }

    // // It works same like scan but it will give the accumulative output only in the end [not one by one like scan]  .
    Observable.range(1,10)
        .reduce { previousAccumulation, newEmission ->
            previousAccumulation+newEmission }
        .subscribeBy { println("accumulation using reduce operator $it") }

}

fun collectionOperators()
{
    // these operators are subset of reduce operator
    // they accumulate emissions and put them into collection object like follows
    // toList and toSortedList
    // toMap
    // toMultiMap
    // collect
    // We will cover it later
}

fun errorHandlingOperators()
{
    //onErrorResumeNext( )
    //onErrorReturn( )
    //onExceptionResumeNext( )
    //retry( )
    //retryWhen( )
}

fun utilityOperators()
{
    //doOnNext, doOnComplete, and doOnError
    //doOnSubscribe, doOnDispose, and doOnSuccess
    //serialize
    //cache
}