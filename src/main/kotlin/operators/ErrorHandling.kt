package operators

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy

fun main() {
    //errorExample()

    // We use error operators to handle the bugs smoothly

    //onErrorReturn()

    // both onError and onComplete are terminal operators,
    // so the downstream stops listening to that upstream as soon as it receives any of them.
    // To deal with this we have onErrorResumeNext() operator

    //onErrorResumeNext()
    //retry()
}

fun errorExample() {
    // How to deal with that exceptions
    Observable.just(1, 2, 3, 4, 5)
        .map { it / (3 - it) }
        .subscribe {
            println("Received $it")
        }
}

fun onErrorReturn() {
    // returns a default value on Error
    Observable.just(1, 2, 3, 4, 5)
        .map { it / (3 - it) }
        .onErrorReturn { -1 }
        .subscribe {
            println("Received $it")
        }
}

fun onErrorResumeNext() {
    Observable.just(1, 2, 3, 4, 5)
        .map { it / (3 - it) }
        //.onErrorResumeNext() // will deal with it later =  TODO
        .onErrorResumeNext {
            Observable.just(11, 12, 13, 14, 15)
        }
        .subscribe {
            println("Received $it")
        }
}

fun retry() {
    // effective in http requests in android
    Observable.just(1, 2, 3, 4, 5)
        .map { it / (3 - it) }
        .retry(3)
        .subscribeBy(
            onNext = { println("Received $it") },
            onError = { println("Error") }
        )

    // another way to use retry operator
    var retryCount = 0
    Observable.just(1, 2, 3, 4, 5)
        .map { it / (3 - it) }
        .retry { _, _ ->
            (++retryCount) <3
        }
        .subscribeBy(
            onNext = { println("Received $it") },
            onError = { println("Error") }
        )

}