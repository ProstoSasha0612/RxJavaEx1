package com.projectapp.rxjavaexercise1.domain.usecases

import com.projectapp.rxjavaexercise1.domain.interfaces.GetUrlApi
import com.projectapp.rxjavaexercise1.domain.interfaces.InternetChecker
import com.projectapp.rxjavaexercise1.domain.logic.InternetStatus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody

class GetUrlContentUseCase(
    private val getUrlApi: GetUrlApi,
    internetChecker: InternetChecker
) {

    private val internetStatus = InternetStatus(internetChecker = internetChecker)

    operator fun invoke(urlList: List<String>): Single<List<String>> {
        var dis: Disposable? = null
        return getUrlContentList(urlList)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                dis = internetStatus.isInternetOnline().subscribeOn(Schedulers.computation())
                    .doOnEach { notification ->
//                        Log.d(
//                            "MYTAG",
//                            "thread for internet checking is: ${Thread.currentThread().name}"
//                        )
//                        Log.d("MYTAG", "Internet is active:$it")
                        if (notification.value == false) {
                            throw Exception("Your internet is offline!!!")
                        }
                    }
                    .subscribe { isInternetOnline ->
//                        Log.d("MYTAG", "Internet is active:$it")
                        if (!isInternetOnline) {
                            throw Exception("Your internet is offline!!!")
                        }
                    }
            }
            .doFinally {
//                Log.d("MYTAG", "doFinally: isInternetOnline disposed")
                dis?.dispose()
            }
    }

    private fun getUrlContentList(urlList: List<String>): Single<List<String>> {
        val res = Observable.fromIterable(urlList)
            .flatMap { url ->
                getServerResponse(url).toObservable()
                    .subscribeOn(Schedulers.io())
            }
            .map {
//                Log.d(
//                    "MYTAG",
//                    "thread for mapping is: ${Thread.currentThread().name}"
//                )
//                Log.d("MYTAG1", it.toString())
                it.string()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toList()
        return res
    }

    private fun getServerResponse(url: String): Single<ResponseBody> {
        return getUrlApi.getUrlContentBody(url)
    }
}