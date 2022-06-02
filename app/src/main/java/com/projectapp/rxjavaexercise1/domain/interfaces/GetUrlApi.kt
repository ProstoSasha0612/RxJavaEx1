package com.projectapp.rxjavaexercise1.domain.interfaces

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody

interface GetUrlApi {
    fun getUrlContentBody(url:String):Single<ResponseBody>
}
