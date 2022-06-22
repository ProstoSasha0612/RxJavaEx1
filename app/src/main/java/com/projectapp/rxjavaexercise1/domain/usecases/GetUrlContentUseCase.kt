package com.projectapp.rxjavaexercise1.domain.usecases

import android.util.Log
import com.projectapp.rxjavaexercise1.domain.interfaces.GetUrlApi
import com.projectapp.rxjavaexercise1.domain.interfaces.InternetChecker
import com.projectapp.rxjavaexercise1.domain.logic.InternetStatus
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.concurrent.TimeUnit

class GetUrlContentUseCase(
    private val getUrlApi: GetUrlApi,
    internetChecker: InternetChecker
) {

    private val internetStatus = InternetStatus(internetChecker = internetChecker)

    operator fun invoke(urlList: List<String>): Single<List<String>> {

        val internetOnObservable = internetStatus.isInternetOnline()
        val requestAnswerObservable = getUrlContentList(urlList).toObservable()


        val observable = Observable.zip(
            internetOnObservable, requestAnswerObservable
        ) { internetOn, requestAnswer ->
            Log.d("MYTAG", "Zipper function callled !!!!!!!")
            Log.d("MYTAG", "Zipper function works on thread: ${Thread.currentThread().name}")

            Log.d("MYTAG1", "internet observable = $internetOn")
            Log.d("MYTAG1", "response single = $requestAnswer")
            Log.d("MYTAG1", "response single list size = ${requestAnswer.size}")
            if (!internetOn) {
                throw Exception("MYTAG your internet connection is offline!!!!!!!")
            }
            requestAnswer
        }
            .subscribeOn(Schedulers.io())

        return Single.fromObservable(observable)
    }


    private fun getUrlContentList(urlList: List<String>): Single<List<String>> {

        val res = Observable.fromIterable(urlList)
            .flatMap { url ->
                getServerResponse(url).toObservable()
            }
            .map {
                Log.d(
                    "MYTAG",
                    "thread for mapping is: ${Thread.currentThread().name}"
                )
                it.string()
            }
            .doOnError {
                println("MYTAG Erorr was occurred: ${it.message} ")
            }
            .subscribeOn(Schedulers.io())
            .toList()
        return res
    }

    private fun getServerResponse(url: String): Single<ResponseBody> {
        return getUrlApi.getUrlContentBody(url)
            .observeOn(Schedulers.computation())
            .doOnSuccess {
                Log.d("MYTAG", "getServerResponse works on thread: ${Thread.currentThread().name}")
            }
            .onErrorReturn {
                println("In request[$url] error occurred: ${it.cause}")
                val errorResult = "In request[$url] error occurred: ${it.cause}".toResponseBody()
                errorResult
            }
    }

}
