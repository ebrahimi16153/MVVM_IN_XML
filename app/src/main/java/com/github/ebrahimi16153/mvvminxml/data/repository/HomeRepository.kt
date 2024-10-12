package com.github.ebrahimi16153.mvvminxml.data.repository

import com.github.ebrahimi16153.mvvminxml.data.remote.ApiService
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException
import javax.inject.Inject


class HomeRepository @Inject constructor(private val apiService: ApiService) {


    suspend fun getRandomFood() = flow {

        try {
            val response = apiService.getRandomFood()

            when (response.code()) {
                200 -> {
                    if (response.body()!!.meals!!.isNotEmpty()) {
                        emit(response.body()?.meals?.get(0) ?: throw Exception("404"))
                    } else {
                        throw Exception("404")
                    }
                }

                in 300..399 -> {
                    throw Exception("Error CODE 300")
                }

                in 400..499 -> {
                    throw Exception("Error CODE 400")
                }

                in 500..599 -> {
                    throw Exception("Error CODE 500")
                }

                else -> {
                    throw Exception("Unknown Error")
                }
            }

        } catch (e:UnknownHostException){
            throw UnknownHostException("Internet ERROR")
        }
        catch (e: Exception) {
            e.stackTrace
            throw Exception(e.message.toString())
        }

    }


    suspend fun getCategory() = flow {

        try {
            val response = apiService.getCategories()

            when (response.code()) {
                200 -> {
                    if (response.body()!!.categories.isNotEmpty()) {
                        emit(response.body()?.categories?.toMutableList())
                    } else {
                        throw Exception("Empty Category")
                    }
                }

                in 300..399 -> {
                    throw Exception("Error CODE 300")
                }

                in 400..499 -> {
                    throw Exception("Error CODE 400")
                }

                in 500..599 -> {
                    throw Exception("Error CODE 500")
                }

                else -> {
                    throw Exception("Unknown Error")
                }
            }


        } catch (e:UnknownHostException){
            throw UnknownHostException("Internet ERROR")
        }
        catch (e: Exception) {
            throw Exception(e.message.toString())
        }

    }


    suspend fun getFoodByFirstLetter(letter: String) = flow {

        try {
            val response = apiService.getListOfFood(letter)

            when (response.code()) {
                200 -> {
                    if (response.body()!!.meals!!.isNotEmpty()) {
                        emit(response.body()?.meals?.toMutableList())
                    } else {
                        throw Exception("404")
                    }
                }

                in 300..399 -> {
                    throw Exception("Error CODE 300")
                }

                in 400..499 -> {
                    throw Exception("Error CODE 400")
                }

                in 500..599 -> {
                    throw Exception("Error CODE 500")
                }

                else -> {
                    throw Exception("Unknown Error")
                }
            }

        }catch (e:UnknownHostException){
            throw UnknownHostException("Internet ERROR")
        }
        catch (e: Exception) {
            throw Exception(e.message.toString())
        }

    }


    suspend fun getFoodsByCategory(cat: String) = flow {


        try {
            val response = apiService.getByCategory(categoryName = cat)
            when (response.code()) {

                in 200..299 -> {
                    emit(response.body()?.meals?.toMutableList())
                }

                in 300..399 -> {
                    throw Exception("ERROR CODE 300")
                }

                in 400..499 -> {
                    throw Exception("ERROR CODE 400")
                }

                in 500..599 -> {
                    throw Exception("ERROR CODE 500")
                }

                else -> {
                    throw Exception("ERROR CODE :0")
                }

            }
        }catch (e:UnknownHostException){
            throw UnknownHostException("Internet ERROR")
        }
        catch (e:Exception){
            throw Exception(e.message)
        }


    }



    suspend fun searchFoods(searchQuery:String) = flow{
        try {
            val response = apiService.searchQuery(searchQuery = searchQuery)
            when (response.code()) {

                in 200..299 -> {
                    emit(response.body()?.meals?.toMutableList())
                }

                in 300..399 -> {
                    throw Exception("ERROR CODE 300")
                }

                in 400..499 -> {
                    throw Exception("ERROR CODE 400")
                }

                in 500..599 -> {
                    throw Exception("ERROR CODE 500")
                }

                else -> {
                    throw Exception("ERROR CODE :0")
                }

            }
        }catch (e:UnknownHostException){
            throw UnknownHostException("Internet ERROR")
        }
        catch (e:Exception){
            throw Exception(e.message)
        }
    }


}

