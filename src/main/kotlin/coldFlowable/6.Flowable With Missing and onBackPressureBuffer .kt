import helper.DataHolder
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription


// Back Pressure Missing helps us using the previous strategies but with some more options
// Same like Buffer Strategy but with configuration option like setting buffer size and many more

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

    val source = Observable.range(1, 100)

    source
        .toFlowable(BackpressureStrategy.MISSING)
        .onBackpressureBuffer()  // Here buffer size can be set -
        .map {
            DataHolder(it)
        }
        .observeOn(Schedulers.io()).subscribe(subscriber)
    runBlocking { delay(300000) }
}

