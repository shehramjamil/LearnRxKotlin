package coldFlowable

import helper.DataHolder
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

// A simple example without Flowable where stream is so big and fast that consumer misses a lot of packets in the start
// To overcome this we use Flowable
// run the program to observe the output

fun main() {
    Observable.range(1, 10000000)
        .map {
            DataHolder(it)
        }
        .observeOn(Schedulers.computation())
        .subscribe {
            println("Received $it;\t")
            if (it.id < 100 )  // consumer misses 41 packets due to overflow
            {
                runBlocking { delay(3000) }
            }
            runBlocking { delay(50) }
        }
    runBlocking { delay(300000) }
}