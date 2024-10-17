package com.github.ebrahimi16153.mvvminxml.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ebrahimi16153.mvvminxml.util.connection.CheckNetwork
import com.github.ebrahimi16153.mvvminxml.util.connection.CheckNetworkImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val network: CheckNetworkImpl):ViewModel()  {


    init {
        checkNetwork()
    }

    val networkStatus = MutableLiveData(CheckNetwork.Status.Unavailable)

    private fun checkNetwork() = viewModelScope.launch {
        network.observe().collectLatest { itStatus ->
            networkStatus.postValue(itStatus)
        }
    }
}