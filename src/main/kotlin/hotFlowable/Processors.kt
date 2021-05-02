package hotFlowable

import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.kotlin.toFlowable
import io.reactivex.rxjava3.processors.AsyncProcessor
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.processors.PublishProcessor


// Processors are the counterparts for Subjects in Flowable. Every type of Subject has its
// counterpart as processor with backpressure support.

fun main() {

    ///////////////////////////////////////////////////////////////////////////////////

    // PublishProcessor
    val flowable = Flowable.create<String>({
        it.onNext("Shehram")
        it.onNext("Jamil")
    },BackpressureStrategy.BUFFER)
    val processor = PublishProcessor.create<String>()
    flowable.subscribe(processor)

    processor.subscribe { println("Subscription 1: $it") }
    processor.subscribe { println("Subscription 2 $it") }
    processor.onNext("String 6")
    processor.onNext("String 7")

    // It only emits 6 and 7 and skip all before that
    // If I put the [flowable.subscribe(processor)] in the end, It will meit everything
    // because it will considers everything as a new emission

    ///////////////////////////////////////////////////////////////////////////////////

    // AsyncProcessor
    val flowable2 = listOf("String 1","String 2","String 3", "String 4","String 5").toFlowable()
    val processor2 = AsyncProcessor.create<String>()
    processor2.subscribe{
        println("Async Processor $it")
    }
    flowable2.subscribe(processor2)


    ///////////////////////////////////////////////////////////////////////////////////

    // BehaviourProcessor
    // Be careful about the position of subscribing processor to flowable.

    val flowable3 = Flowable.create<Int>(
        {it.onNext(1)
        it.onNext(2)}
    ,BackpressureStrategy.BUFFER)

    val processor3 = BehaviorProcessor.create<Int>()
    flowable3.subscribe(processor3)
    processor3.onNext(22)
    processor3.subscribe{
        println("Behavioural Processor $it")
    }
    processor3.onNext(3)
    processor3.onNext(4)
    processor3.onNext(5)


}