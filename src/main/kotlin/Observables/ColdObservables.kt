import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.toObservable
import java.util.concurrent.Callable
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

// different ways to make an observable
// Cold Observables
// They need subscriptions to begin emission

fun main() {

    lateinit var disposable: Disposable

    // An instance of observer
    val observer: Observer<Any> = object : Observer<Any> {
        override fun onSubscribe(d: Disposable?) {
            println("New Subscription")
            disposable = d!!
        }

        override fun onNext(t: Any?) {
            println(t.toString())
        }

        override fun onError(e: Throwable?) {
        }

        override fun onComplete() {
            println("Completed")
            // Dispose the resources anywhere
            disposable.dispose()
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////

    val observable1: Observable<List<Any>> =
        Observable.just(listOf(arrayListOf("2", "3", "4")), arrayListOf("Shehram", "Jamil"))
    observable1.subscribe(observer)

    ///////////////////////////////////////////////////////////////////////////////////

    val observable2: Observable<Any> = listOf("2", "3", "4").toObservable()
    observable2.subscribe(observer)

    ///////////////////////////////////////////////////////////////////////////////////

    val observable3 = Observable.create<String> {
        it.onNext("hello")
        it.onNext("How Are You")
        it.onError(Exception("Error"))
        it.onComplete()
    }
    observable3.subscribe(observer)

    ///////////////////////////////////////////////////////////////////////////////////


    val list = listOf(1, 2, 3, 4, 5)
    val observable4 = Observable.fromIterable(list)
    observable4.subscribe(observer)

    ///////////////////////////////////////////////////////////////////////////////////

    // interesting
    val future = object : Future<String> {
        override fun cancel(mayInterruptIfRunning: Boolean): Boolean = false

        override fun isCancelled(): Boolean = false

        override fun isDone(): Boolean = true

        override fun get(): String = "Future String"

        override fun get(timeout: Long, unit: TimeUnit): String = "Future String"
    }
    val observable5 = Observable.fromFuture(future)
    observable5.subscribe(observer)


    ///////////////////////////////////////////////////////////////////////////////////

    val callable = Callable { "From Callable" }

    val observable6: Observable<String> = Observable.fromCallable(callable)
    observable6.subscribe(observer)

    ///////////////////////////////////////////////////////////////////////////////////

    val observable7 = Observable.range(1, 5)
    observable7.subscribe(observer)

    ///////////////////////////////////////////////////////////////////////////////////

    val observable8: Observable<Long> = Observable.interval(300, TimeUnit.MILLISECONDS)
    observable8.subscribe(observer)

    ///////////////////////////////////////////////////////////////////////////////////

    Observable.timer(400, TimeUnit.MILLISECONDS).subscribe(observer)

    ///////////////////////////////////////////////////////////////////////////////////

    Observable.empty<Int>().subscribe(observer)

    ///////////////////////////////////////////////////////////////////////////////////


}


