package com.projectapp.rxjavaexercise1.domain.logic

import com.projectapp.rxjavaexercise1.domain.interfaces.InternetChecker
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class InternetStatus(private val internetChecker: InternetChecker) {

    fun isInternetOnline(): Observable<Boolean> {
        return Observable.fromCallable { internetChecker.isOnline() }
            .subscribeOn(Schedulers.computation())
            .repeatWhen {
                println("timer works on tread: ${Thread.currentThread().name}")
//                Log.d("MYTAG", "timer works on tread: ${Thread.currentThread().name}")
                it.delay(1000, TimeUnit.MILLISECONDS)
            }
    }
}
