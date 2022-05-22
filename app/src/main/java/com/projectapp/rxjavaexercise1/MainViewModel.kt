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
import io.reactivex.rxjava3.functions.BooleanSupplier
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import okhttp3.Response
import okhttp3.ResponseBody
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val urlApiService: GetUrlApiService,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    fun isInternetOnline(): Observable<Boolean> {
        return Observable.fromCallable { isOnline() }.subscribeOn(Schedulers.computation())
//            .repeatWhen{
//                it.delay(1000,TimeUnit.MILLISECONDS)
//            }
    }

    fun getServerResponse(url: String): Single<ResponseBody> {
        return urlApiService.getUrlContent(url)
    }

    fun getUrlContent(urlList: List<String>): Single<List<String>> {
        val res = Observable.fromIterable(urlList)
            .flatMap { url ->
                getServerResponse(url).toObservable()
                    .subscribeOn(Schedulers.io())
            }
            .map {
                Log.d("MYTAG1", it.toString())
                it.string()

            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toList()
        return res
    }


    fun execute(urlList: List<String>) {
        //Is internet active call
        isInternetOnline()
//            .repeatWhen { it.delay(1, TimeUnit.SECONDS) }
//            .repeatUntil { isGetUrlContentWorking }
            .subscribe {
                Log.d("MYTAG", "Internet is active:$it")
            }

        getUrlContent(urlList).subscribe { strList ->
            for (i in strList.indices) {
                Log.d("MYTAG", "res string [$i] is = ${strList[i]}")
            }
//            isGetUrlContentWorking = false
        }

    }

    fun execute2(urlList: List<String>) {
        getUrlContent(urlList)
    }

    private fun isOnline(): Boolean {
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