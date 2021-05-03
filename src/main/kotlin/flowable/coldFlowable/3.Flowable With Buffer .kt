import helper.DataHolder
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

// Another example of BackPressureStrategy with Buffer
// In the following example emissions will not be missed and consumer can coup up easily
// Any Observable can be converted to flowable with toFlowable Operator

fun main() {

    // An instance of subscriber
    val subscriber: Subscriber<Int> = object : Subscriber<Int> {
        override fun onComplete() {
            println("All Completed")
        }

        override fun onNext(item: Int) {
            println("Next $item")
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
    source.toFlowable(BackpressureStrategy.BUFFER)
        .map {
            DataHolder(it)
        }
        .observeOn(Schedulers.io()).subscribe {
            print("$it ")
            runBlocking { delay(100) }
        }
    runBlocking { delay(300000) }
}

