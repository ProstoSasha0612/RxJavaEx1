package com.projectapp.rxjavaexercise1.retrofit

import io.reactivex.rxjava3.core.Single
import kotlinx.serialization.json.JsonElement
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface GetUrlApiService {
    @GET
    fun getUrlContent(@Url urlString: String): Single<ResponseBody>
}