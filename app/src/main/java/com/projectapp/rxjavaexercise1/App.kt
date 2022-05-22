package com.projectapp.rxjavaexercise1

import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.projectapp.rxjavaexercise1.retrofit.GetUrlApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class App : Application() {

    lateinit var getUrlApiService: GetUrlApiService

    override fun onCreate() {
        super.onCreate()
        initRetrofit()
    }

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
}