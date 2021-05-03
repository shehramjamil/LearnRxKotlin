import helper.DataHolder
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription


// Back Pressure Missing helps us using the previous strategies but with some more options
// Same like Latest strategy and come with no configuration option
// Drops all but the latest item emitted by the current Flowable if
// the downstream is not ready to receive new items and emits this latest item when the downstream becomes ready.

fun main() {

    // An instance of subscriber
    val subscriber: Subscriber<Any> = object : Subscriber<Any> {
        override fun onComplete() {
            println("All Completed")
        }

        override fun onNext(item: Any) {
            println("Consumer with $item")
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
        .onBackpressureLatest() // no configuration provided for this
        .map {
            DataHolder(it)
        }
        .observeOn(Schedulers.io()).subscribe(subscriber)
    runBlocking { delay(300000) }
}

