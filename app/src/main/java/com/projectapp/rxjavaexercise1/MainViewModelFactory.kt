package com.projectapp.rxjavaexercise1

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(private val app: App) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val urlApiService = app.getUrlApiService
        val connectivityManager =
            app.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return MainViewModel(urlApiService, connectivityManager = connectivityManager) as T
    }
}