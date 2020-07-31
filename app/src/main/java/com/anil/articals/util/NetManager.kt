package com.anil.articals.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

/**
 * Created by Anil 28/6/20
 */
class NetManager @Inject constructor(private val applicationContext: Context) {

    val isConnectedToInternet: Boolean
        @SuppressLint("MissingPermission")
        get() {
            val conManager =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = conManager.activeNetworkInfo
            return network != null && network.isConnected
        }
}