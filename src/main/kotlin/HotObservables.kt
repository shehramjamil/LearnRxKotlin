import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.toObservable
import io.reactivex.rxjava3.subjects.AsyncSubject
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

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

    // Connectable Observables - Pushing all the observables after calling connect
    // React to one single push - [Do not receive after calling connect]
    // multicasting mechanism

    val connectableObservable = (1..10).toObservable().publish()
    connectableObservable.subscribe {
        println("Subscription 1 = $it")
    }
    connectableObservable.map {
        it.plus(1)
    }.subscribe {
        println("Subscription 2 = $it")
    }
    connectableObservable.connect()
    // To make the subscriptions work after connect() method, consult the book [Hint : add delays and interval]


    ///////////////////////////////////////////////////////////////////////////////////

    // AsyncSubjects emits only last value of source observable
    val observable1 = Observable.just(1, 2, 3, 4)
    val asyncSubject1 = AsyncSubject.create<Int>()
    observable1.subscribe(asyncSubject1)
    asyncSubject1.subscribe(observer)


    // We can also do it without observable
    val asyncSubject2 = AsyncSubject.create<Int>()
    asyncSubject2.onNext(1)
    asyncSubject2.onNext(2)
    asyncSubject2.onNext(3)
    asyncSubject2.onComplete()  // Async only emits on calling onComplete
    asyncSubject2.subscribe(observer)


    ///////////////////////////////////////////////////////////////////////////////////

    // BehaviourSubject - It takes the last item emitted before subscription and items emitted after the subscription
    // mix of Async and Publish Subjects

    val subject = BehaviorSubject.create<Int>()
    subject.onNext(1)
    subject.onNext(2)
    subject.onNext(3)
    subject.onNext(4)
    subject.subscribe({
        println("S1 Received $it")
    }, {
        it.printStackTrace()
    }, {
        println("S1 Complete")
    })
    subject.onNext(5)
    subject.onNext(6)
    subject.subscribe({
        println("S2 Received $it")
    }, {
        it.printStackTrace()
    }, {
        println("S2 Complete")
    })
    subject.onComplete()


    ///////////////////////////////////////////////////////////////////////////////////

    // ReplaySubject - More Like Cold Observables

    val replaySubject = ReplaySubject.create<String>()
    replaySubject.onNext("Replay 1")
    replaySubject.onNext("Replay 2")
    replaySubject.onNext("Replay 3")
    replaySubject.onNext("Replay 4")
    replaySubject.subscribe(observer)
    replaySubject.onNext("Replay 5")
    replaySubject.onNext("Replay 6")
    replaySubject.subscribe(observer)
    replaySubject.onComplete()

    ///////////////////////////////////////////////////////////////////////////////////

    // PublishSubject emits to an observer only those items that are emitted by
    // the Observable sources subsequent to the time of the subscription.[after subscription]
    // Subjects are just like a TV Channel broadcasting a Film from a CD/DVD[Observable] recording
    // Subjects have the properties of both Observables and Observers

    val publishSubject = PublishSubject.create<Long>()
    publishSubject.onNext(1)
    publishSubject.onNext(2)
    publishSubject.subscribe { println("Subscription 1 Received $it") }
    publishSubject.onNext(3)
    publishSubject.onNext(4)
    publishSubject.subscribe { println("Subscription 2 Received $it") }
    publishSubject.onNext(5)
    publishSubject.onNext(6)

    runBlocking { delay(1100)}




}




