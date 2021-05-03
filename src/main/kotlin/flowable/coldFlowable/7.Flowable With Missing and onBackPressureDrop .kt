import helper.DataHolder
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription


// Back Pressure Missing helps us using the previous strategies but with some more options
// Same like Drop strategy and come with configuration option like finding the missing items and doing something

fun main() {

    // An instance of subscriber
    val subscriber: Subscriber<Any> = object : Subscriber<Any> {
        override fun onComplete() {
            println("All Completed")
        }

        override fun onNext(item: Any) {
            println("$item")
            runBlocking { delay(50) }
        }

        override fun onError(e: Throwable) {
            println("Error Occured ${e.message}")
        }

        override fun onSubscribe(subscription: Subscription) {
            println("New Subscription ")
            subscription.request(10000)
        }
    }


    val source = Observable.range(1, 10000)
    source
        .toFlowable(BackpressureStrategy.MISSING)
        .onBackpressureDrop { println("Dropped $it")}  // Here we can find the dropped packets // Amazing
        .map {
            DataHolder(it)
        }
        .observeOn(Schedulers.io()).subscribe(subscriber)
    runBlocking { delay(300000) }
}

