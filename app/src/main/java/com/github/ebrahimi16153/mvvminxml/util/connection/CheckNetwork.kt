package com.github.ebrahimi16153.mvvminxml.util.connection

import kotlinx.coroutines.flow.Flow

interface CheckNetwork {

    fun observe(): Flow<Status>

    enum class Status {
        Available, Unavailable, Losing, Lost;


    }

}