package com.projectapp.rxjavaexercise1.data

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface GetUrlApiService {
    @GET
    fun getUrlContent(@Url urlString: String): Single<ResponseBody>
}