package observables

import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable

fun main() {
    singleObservable()
    mayBeObservable()
    completableObservable()
}

fun singleObservable() {
    val singleObserver = object : SingleObserver<Int> {
        override fun onSubscribe(d: Disposable?) {
        }
        override fun onSuccess(t: Int?) {
            println(t)
        }
        override fun onError(e: Throwable?) {
        }

    }
    // Single is used when the Observable has to emit only one value like a response from a network call.
    Single.create<Int> {
        it.onSuccess(1)
    }.subscribe(singleObserver)
}

fun mayBeObservable() {
    // Maybe is used when the Observable has to emit a value or no value.
    val mayBeObserver = object : MaybeObserver<Int> {
        override fun onSubscribe(d: Disposable?) {
        }

        override fun onSuccess(t: Int?) {
            println(t)
        }

        override fun onError(e: Throwable?) {
        }

        override fun onComplete() {
            println("Operation Done")
        }

    }
    Maybe.create<Int> {
        it.onSuccess(1)
    }.subscribe(mayBeObserver)

}

fun completableObservable() {
    val completableObserver = object : CompletableObserver {
        override fun onSubscribe(d: Disposable?) {
        }
        override fun onError(e: Throwable?) {
        }
        override fun onComplete() {
            println("Operation Done")
        }
    }
    // Completable is used when the Observable has to do some task without emitting a value.
    Completable.create {
        it.onComplete()
    }.subscribe(completableObserver)

}