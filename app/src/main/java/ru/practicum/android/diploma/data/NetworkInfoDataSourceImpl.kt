package ru.practicum.android.diploma.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkInfoDataSourceImpl(private val context: Context) : NetworkInfoDataSource {

    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun isConnected(): Boolean {
        val network = cm.activeNetwork ?: return false
        return cm.getNetworkCapabilities(network)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }
}
