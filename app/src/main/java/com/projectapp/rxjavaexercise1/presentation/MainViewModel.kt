package com.projectapp.rxjavaexercise1.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.projectapp.rxjavaexercise1.domain.usecases.GetUrlContentUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel(
    private val getUrlContentUseCase: GetUrlContentUseCase
) : ViewModel() {

    fun getContent(urlList: List<String>) {
        getUrlContentUseCase(urlList)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                Log.d("MYTAG", "onSubscribe current thread: ${Thread.currentThread().name}")
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { responseList, error ->
                println(" MYTAG current thread when subscribe: ${Thread.currentThread().name}")
                Log.d("MYTAG", "response list size=${responseList.size}")
                Log.d("MYTAG", "current thread: ${Thread.currentThread().name}")
                responseList.forEach {
                    Log.d("MYTAG", "response list value is:${it}")
                }
            }
    }

    fun getContent2(urlList: List<String>): Single<List<String>> {

       return getUrlContentUseCase(urlList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}