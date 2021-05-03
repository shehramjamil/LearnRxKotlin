package flowable

import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

// Previously we apply back pressure strategies at the downstream
// 1. by dropping the packets while consumer is busy
// 2. by buffering them
// But we can also hold packets while emission
// For applying strategies at source we need some operators to slow down the source

fun main() {
    bufferOperator()
    windowOperator()
    throttleOperator()
}


fun bufferOperator() {
    // Buffer Operator
    // Unlike the onBackPressureBuffer() operator, which buffers emissions until the
    //consumer consumes, the buffer() operator will gather emissions as a batch and will emit
    //them as a list or any other collection type.

    val flowable = Flowable.range(1, 111)
    flowable.buffer(10)
        .subscribe { println(it) }

    // We can also use skip parameter to skip values at the end of each emission
    // One scenario is when skip parameter is bigger than count so it works properly
    // for the following example, 15-10 = 5, so our program will skip 5 items after 10 and start from 15 and repeat

    val flowable2 = Flowable.range(1, 111)
    flowable2.buffer(10, 15)//(1)
        .subscribe { println("Subscription 1 $it") }

    // Other scenario is when skip parameter is less than count then we will have repetitions
    // It will repeat the items from the every 7th position of the previous list
    flowable2.buffer(15, 7)//(2)
        .subscribe { println("Subscription 2 $it") }


    // Buffer can store values using a time slow as shown in following example
    // buffer is amazing, we can emit after storing data for a specified interval
    val flowable3 = Flowable.interval(100, TimeUnit.MILLISECONDS)
    flowable3.buffer(1, TimeUnit.SECONDS)
        .subscribe { println("Subscription 3 $it") }
    runBlocking { delay(5000) }


    // we can also put another flowable inside buffer
    // both flowable will emit values by combining their time intervals
    // so in that case we will get a list of 4 everytime = 300 + 100 = 400 [1 item per 100 ms]
    val boundaryFlowable = Flowable.interval(300, TimeUnit.MILLISECONDS)
    val flowable4 = Flowable.interval(100, TimeUnit.MILLISECONDS)
    flowable4.buffer(boundaryFlowable)
        .subscribe { println("Subscription 4 $it") }
    runBlocking { delay(5000) }

}

fun windowOperator() {
    // The window() operator works almost the same, except that, instead of buffering items in a
    // Collection object, it buffers items in another producer.
    // window operator also has same other functionalities like buffer

    val flowable = Flowable.range(1, 111)//(1)
    flowable.window(10)
        .subscribe { it ->
            it.subscribe {
                println("$it ")
            }
        }
}

fun throttleOperator() {
    // to omit items
    // this code will omit items every 200 ms
    val flowable = Flowable.interval(100, TimeUnit.MILLISECONDS)
    flowable.throttleFirst(200, TimeUnit.MILLISECONDS)
        .subscribe { println(it) }
    runBlocking { delay(1000) }

    //we have throttleLatest and throttleLast operator as well
    // will be discussed later

}