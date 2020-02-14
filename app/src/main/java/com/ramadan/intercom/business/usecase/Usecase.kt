package com.ramadan.intercom.business.usecase
import io.reactivex.Flowable

interface UseCase<P,R> {
    fun execute(param:P): Flowable<R>
}