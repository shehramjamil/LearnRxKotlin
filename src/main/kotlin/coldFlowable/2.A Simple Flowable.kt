package coldFlowable

import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

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

fun main() {

    // Using Flowable with BackPressure Buffer
    // Buffer is unbound but OutOfMemory error could occur. [it is not un limited no doubt]

    val flowable1 = Flowable.create<Int>({
    for (i in 1..100)
        it.onNext(i)
    it.onComplete()
    }, BackpressureStrategy.BUFFER)
    flowable1.subscribe(subscriber)

}