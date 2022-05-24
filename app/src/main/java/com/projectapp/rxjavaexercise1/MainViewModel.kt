package com.projectapp.rxjavaexercise1

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.projectapp.rxjavaexercise1.retrofit.GetUrlApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BooleanSupplier
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import okhttp3.Response
import okhttp3.ResponseBody
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.math.E

class MainViewModel(
    private val urlApiService: GetUrlApiService, // move to data layer + create interface and impl
    private val connectivityManager: ConnectivityManager // it needs in viewmodel, is wil be here in constructor
) : ViewModel() {

    private fun isInternetOnline(): Observable<Boolean> {
        return Observable.fromCallable { isOnline() }
            .subscribeOn(Schedulers.computation())
            .repeatWhen {
                it.delay(1000, TimeUnit.MILLISECONDS)
            }
    }

    private fun getServerResponse(url: String): Single<ResponseBody> { // move to domain
        return urlApiService.getUrlContent(url)
    }

    private fun getUrlContent(urlList: List<String>): Single<List<String>> { // move to domain private
        val res = Observable.fromIterable(urlList)
            .flatMap { url ->
                getServerResponse(url).toObservable()
                    .subscribeOn(Schedulers.io())
            }
            .map {
                Log.d(
                    "MYTAG",
                    "thread for mapping is: ${Thread.currentThread().name}"
                )
                Log.d("MYTAG1", it.toString())
                it.string()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toList()
        return res
    }

    fun execute(urlList: List<String>): Single<List<String>> { // move to domain public execute and subscribe in viewmodel
        var dis: Disposable? = null
        return getUrlContent(urlList)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                dis = isInternetOnline().subscribeOn(Schedulers.computation())
                    .doOnEach {
                        Log.d(
                            "MYTAG",
                            "thread for internet checking is: ${Thread.currentThread().name}"
                        )
                        Log.d("MYTAG", "Internet is active:$it")
                        if (!it.value) {
                            throw Exception("Your internet is offline!!!")
                        }
                    }

                    .subscribe {
                        Log.d("MYTAG", "Internet is active:$it")
                        if (!it) {
                            throw Exception("Your internet is offline!!!")
                        }
                    }
            }
            //try also doAfterSuccess
                //change it to doOnTerminate or finally
            .doOnSuccess {
                Log.d("MYTAG", "doOnSuccess: isInternetOnline disposed")
                dis?.dispose()
            }

    }


    private fun isOnline(): Boolean { // move to domain private, method acquires connecivity manager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}