package com.github.ebrahimi16153.mvvminxml.util.connection

import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


class CheckNetworkImpl @Inject constructor( private val cm: ConnectivityManager) : CheckNetwork {


    override fun observe(): Flow<CheckNetwork.Status> {

        return callbackFlow {

            val callback = object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {
                        send(CheckNetwork.Status.Available)
                    }

                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch {
                        send(CheckNetwork.Status.Losing)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {

                        send(CheckNetwork.Status.Lost)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {
                        send(CheckNetwork.Status.Unavailable)
                    }
                }
            }
            //
            cm.registerDefaultNetworkCallback(callback)
            awaitClose {
                cm.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()

    }
}