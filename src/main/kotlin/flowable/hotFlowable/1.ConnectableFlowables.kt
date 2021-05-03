import io.reactivex.rxjava3.kotlin.toFlowable
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

// Just like ConnectableObservables
// With all the back pressure strategies and operators we used before
// Subscribers receive the data on calling connect()
// See the hotObservables File for comparison

fun main() {
    val connectableFlowable = listOf("String 1","String 2","String 3","String 4", "String 5").toFlowable().publish()
    connectableFlowable.onBackpressureDrop()

    connectableFlowable.
    subscribe {
        println("Subscription 1: $it")
        runBlocking { delay(1000) }
        println("Subscription 1 delay")
    }
    connectableFlowable
        .subscribe({ println("Subscription 2 $it")})


    connectableFlowable.connect()
}