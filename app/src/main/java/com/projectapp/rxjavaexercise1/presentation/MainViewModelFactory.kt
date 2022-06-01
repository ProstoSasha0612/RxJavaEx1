package com.projectapp.rxjavaexercise1.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projectapp.rxjavaexercise1.data.GetUrlApiImpl
import com.projectapp.rxjavaexercise1.domain.interfaces.InternetChecker
import com.projectapp.rxjavaexercise1.domain.usecases.GetUrlContentUseCase

class MainViewModelFactory(private val app: App) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val connectivityManager =
            app.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val internetChecker = object : InternetChecker {
            override fun isOnline() = checkInternet(connectivityManager)
        }

        val urlApiService = GetUrlApiImpl
        val getUrlContentUseCase = GetUrlContentUseCase(urlApiService, internetChecker)
        return MainViewModel(getUrlContentUseCase) as T
    }

    private fun checkInternet(connectivityManager: ConnectivityManager): Boolean {
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
