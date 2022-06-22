package com.projectapp.rxjavaexercise1.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projectapp.rxjavaexercise1.domain.usecases.GetUrlContentUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val getUrlContentUseCase: GetUrlContentUseCase
) : ViewModel() {

    private val disposableBag = CompositeDisposable()
    private val _urlsAnswerList = MutableLiveData<List<String>>()
    val urlsAnswerList: LiveData<List<String>> get() = _urlsAnswerList

    fun getContent(urlList: List<String>) {

        val disposable = getUrlContentUseCase(urlList)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                Log.d("MYTAG", "onSubscribe current thread: ${Thread.currentThread().name}")
            }
            .subscribeOn(Schedulers.io())
            .subscribe { responseList, error ->
                println(" MYTAG current thread when subscribe: ${Thread.currentThread().name}")
                Log.d("MYTAG", "response list size=${responseList.size}")
                Log.d("MYTAG", "current thread: ${Thread.currentThread().name}")
                responseList.forEach {
                    Log.d("RESPONSETAG", "response list value is:${it}")
                }

                _urlsAnswerList.value = responseList
            }

        disposableBag.add(disposable)
    }

    override fun onCleared() {
        disposableBag.clear()
        super.onCleared()
    }

}
