package com.github.ebrahimi16153.mvvminxml.util

sealed class Wrapper<out T> {

    data object Idle:Wrapper<Nothing>()
    data object Loading :Wrapper<Nothing>()
    data class Success<T>(val data:T):Wrapper<T>()
    data class Error(val message:String):Wrapper<Nothing>()

}