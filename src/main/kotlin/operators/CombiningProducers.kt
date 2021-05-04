package operators

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.kotlin.toObservable
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

// Sometimes we receive data from different sources and needs to combine them [like from server and local database]
// For this we have following operators

//startWith()
//merge(), mergeDelayError()
//concat()
//zip()
//combineLatest()

fun main() {

    //startWithOperator()
    //zip()
    //zipWith()
    //combineLatest()

    // both are same except
    //merge() // do not maintain order
    //concat() // maintains order

    //amb() // ambiguously combining producer
    //groupBy()

    //flatMapConcatMap()
    switchMap()

}

fun startWithOperator() {
    // we have already covered it in TransformingOperators.kt
    startWith()
}

fun zip() {
    // the zip operator waits for all producers to emit values and then accumulates their
    // emissions and apply the function to it specified by the programmer and emits as single emission.

    // Important
    // if producer x emits 10 items, producer y emits 11 items, and producer z emits 8 items, the zip operator
    // will accumulate the first 8 emissions from all the producers and will discard all  remaining emissions from producer x and y.
    // emissions can be stopped by the producer calling onError or onComplete

    val observable1 = Observable.range(1, 10)
    val observable2 = Observable.range(11, 10)
    Observable.zip(observable1, observable2, { producer1, producer2 ->
        producer1 * producer2
    }).subscribe {
        println(it)
    }

}

fun zipWith() {
    // same like zip - but now we can use instance of one observable to call the zip operator

    val observable1 = Observable.range(1, 2)
    val observable2 = listOf("String 1", "String 2").toObservable()
    observable1.zipWith(observable2, { e1: Int, e2: String ->
        "$e2 $e1"
    })
        .subscribe {
            println("Received $it")
        }
}

fun combineLatest() {
    // The combineLatest operator processes and emits the value as soon as it gets an emit from any of its source producers
    // by using the last emitted value for all other source producers.
    // It means if producer one emits 1 and the producer 2 did not emit anything and last value from producer 2 is 0,
    // then we will get the output as = producer 1 = 1  and  producer 2 = 0

    val observable1 = Observable.interval(100, TimeUnit.MILLISECONDS)
    val observable2 = Observable.interval(300, TimeUnit.MILLISECONDS)
    Observable.combineLatest(observable1, observable2,
        { t1, t2 -> "t1: $t1, t2: $t2" })
        .subscribe {
            println("Received $it")
        }
    runBlocking { delay(1100) }
}

fun merge() {
    // It does not accumulate the emissions, it just put them together in the downstream
    // and we will have only single subscriber for listening emissions from multiple producers
    // It doesn't maintain the order specified; rather, it'll start listening to all the provided producers
    // instantly and will fire emissions as soon as they are emitted from the source

    val observable1 = listOf("Kotlin", "Scala", "Groovy").toObservable()
    val observable2 = listOf("Python", "Java", "C++", "C").toObservable()
    Observable.merge(observable1, observable2)
        .subscribe {
            println("Received $it")
        }

    // An example which shows that merge does not keep the things in order
    val observable3 = Observable.interval(500, TimeUnit.MILLISECONDS).map { "Observable 1 $it" }
    val observable4 = Observable.interval(100, TimeUnit.MILLISECONDS).map { "Observable 2 $it" }
    Observable.merge(observable3, observable4)
        .subscribe {
            println("Received $it")
        }
    runBlocking { delay(1500) }

    // Merge only accepts 4 producers but we have another operator called = mergeArray()

    val observable5 = listOf("A", "B", "C").toObservable()
    val observable6 = listOf("D", "E", "F", "G").toObservable()
    val observable7 = listOf("I", "J", "K", "L").toObservable()
    val observable8 = listOf("M", "N", "O", "P").toObservable()
    val observable9 = listOf("Q", "R", "S", "T").toObservable()
    val observable10 = listOf("U", "V", "W", "X").toObservable()
    Observable.mergeArray(
        observable5, observable6, observable7,
        observable8, observable9, observable10
    )
        .subscribe {
            println("Received $it")
        }

    // we also have mergeWith operator

    val observable11 = listOf("Kotlin", "Scala", "Groovy").toObservable()
    val observable12 = listOf("Python", "Java", "C++", "C").toObservable()
    observable11
        .mergeWith(observable12)
        .subscribe {
            println("Received $it")
        }

}

fun concat() {
    // concatenating operators respect the prescribed ordering. Instead of subscribing to all
    // provided producers in one go, it subscribes to the producers one after another; only once,
    // it received onComplete from the previous subscription.

    // take operator is used to take specified amount of emissions - because interval operator emit infinite until max is reached

    val observable1 = Observable.interval(2000, TimeUnit.MILLISECONDS).take(2).map { "Observable 1 =  $it" }//(2)
    val observable2 = Observable.interval(100, TimeUnit.MILLISECONDS).map { "Observable 2 = $it" }
    Observable.concat(observable1, observable2)
        .subscribe {
            println("Received $it")
        }
    runBlocking { delay(5000) }

    // Just like the merge operator, the concat operator also has concatArray and concatWith variants
}

fun amb() {
    // for example , we want to fetch from two different remote or local sources and want to take the first one we got and
    // discard the other one. so in this case we can use amb operator

    val observable1 = Observable.interval(500, TimeUnit.MILLISECONDS).map { "Observable 1 $it" }
    val observable2 = Observable.interval(100, TimeUnit.MILLISECONDS).map { "Observable 2 $it" }
    Observable.amb(listOf(observable1, observable2))
        .subscribe {
            println("Received $it")
        }
    runBlocking { delay(1500) }

    // Just like other combination operators, amb also has ambArray and ambWith operator variants
}

fun groupBy() {

    //  to group up the items based on some custom function
    val observable = Observable.range(1, 30)
    observable.groupBy {
        it % 5
    }.blockingSubscribe {
        println("Key = ${it.key}")
        it.subscribe {
            println("value = $it")
        }
    }

    // more examples to understand it better =  TODO

}

fun flatMapConcatMap() {
    // we have covered it before
    // it uses merge operator underneath
    // it does not maintain order

    Observable.range(1, 10)
        .flatMap {
            val randDelay = Random().nextInt(10)
            Observable.just(it).delay(randDelay.toLong(), TimeUnit.MILLISECONDS)
        }
        .blockingSubscribe {
            println("FlatMap =  $it")
        }

    // concatMap() uses concat operator underneath therefore it maintains order

    Observable.range(1, 10)
        .concatMap {
            val randDelay = Random().nextInt(10)
            Observable.just(it).delay(randDelay.toLong(), TimeUnit.MILLISECONDS)
        }
        .blockingSubscribe {
            println("Concat Map =  $it")
        }

}

fun switchMap() {
    // SwitchMap would emit only the latest observable after a particular delay. It ignores all the previous ones.

    Observable.range(1, 10)
        .switchMap {
            Observable.just(it).delay(5, TimeUnit.MILLISECONDS)
        }
        .blockingSubscribe {
            println("Received $it")
        }

    // the output will be 10 because after a particular delay, it will ignore all and take the latest emitted value

    // Another example to make it clear
    // we get 3,6,9 because they are without delay and
    // then we get 10 because it is last element emitted after a random delay and al previous ones are discarded

    Observable.range(1, 10)
        .switchMap {
            val randDelay = Random().nextInt(10)
            if (it % 3 == 0)
                Observable.just(it)
            else
                Observable.just(it)
                    .delay(randDelay.toLong(), TimeUnit.MILLISECONDS)
        }
        .blockingSubscribe {
            println("Received $it")
        }
}