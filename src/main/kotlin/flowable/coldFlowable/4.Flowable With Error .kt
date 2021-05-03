import helper.DataHolder
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

// This, again, leads to no backpressure implementation and signals MissingBackpressureException the very
// moment the downstream cannot keep up with the source.

fun main() {

    // An instance of subscriber
    val subscriber: Subscriber<Any> = object : Subscriber<Any> {
        override fun onComplete() {
            println("All Completed")
        }

        override fun onNext(item: Any) {
            println("$item")
        }

        override fun onError(e: Throwable) {
            println("Error Occured ${e.message}")
        }

        override fun onSubscribe(subscription: Subscription) {
            println("New Subscription ")
            subscription.request(10000)
        }
    }

    val source = Observable.range(1, 1000000)
    source.toFlowable(BackpressureStrategy.ERROR)
        .map {
            DataHolder(it)
        }
        .observeOn(Schedulers.io()).subscribe(subscriber)
    runBlocking { delay(300000) }
}

