package com.projectapp.rxjavaexercise1.domain.logic

import android.util.Log
import com.projectapp.rxjavaexercise1.domain.interfaces.InternetChecker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Error
import java.util.concurrent.TimeUnit

class InternetStatus(private val internetChecker: InternetChecker) {

    fun isInternetOnline(): Observable<Boolean> {
        return Observable.fromCallable { internetChecker.isOnline() }
            .doOnNext {
                Log.d("MYTAG", "internet is active = $it")
                if(!it) throw Exception("MYTAG your internet connection is offline!!!!!!!")
            }
            .repeatWhen {
                println("timer works on tread: ${Thread.currentThread().name}")
                Log.d("MYTAG", "timer works on tread: ${Thread.currentThread().name}")
                it.delay(200, TimeUnit.MILLISECONDS)
            }
            .subscribeOn(Schedulers.computation())
    }

}
