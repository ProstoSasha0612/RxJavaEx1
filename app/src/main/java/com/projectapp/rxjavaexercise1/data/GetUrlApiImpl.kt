package com.projectapp.rxjavaexercise1.data

import com.projectapp.rxjavaexercise1.domain.interfaces.GetUrlApi
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.create

object GetUrlApiImpl : GetUrlApi {

    init {
        initRetrofit()
    }

    private var getUrlApiService: GetUrlApiService? = null

    private fun initRetrofit() {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            //без базового url что-то никак не хочет работать, поэтому это костылик
            .baseUrl("https://api.thecatapi/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        getUrlApiService = retrofit.create()
    }

    override fun getUrlContentBody(url: String): Single<ResponseBody> {
        return getUrlApiService?.getUrlContent(url)
            ?: throw Exception("getUrlApiService value is NULL!")
    }
}
