package com.github.ebrahimi16153.mvvminxml.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import javax.inject.Inject

class CheckConnection @Inject constructor(private val cm: ConnectivityManager) :
    LiveData<Boolean>() {



        private val networkCallback  = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                this@CheckConnection.postValue(true)
                super.onAvailable(network)
            }

            override fun onLost(network: Network) {
                postValue(false)
                super.onLost(network)
            }

        }

    override fun onActive() {
        val request = NetworkRequest.Builder()
        cm.registerNetworkCallback(request.build(), networkCallback)
        super.onActive()
    }

    override fun onInactive() {
        cm.unregisterNetworkCallback(networkCallback)
        super.onInactive()
    }


}