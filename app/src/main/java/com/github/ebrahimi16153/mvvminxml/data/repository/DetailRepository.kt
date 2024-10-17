package com.github.ebrahimi16153.mvvminxml.data.repository

import com.github.ebrahimi16153.mvvminxml.data.local.FoodDao
import com.github.ebrahimi16153.mvvminxml.data.model.FoodList
import com.github.ebrahimi16153.mvvminxml.data.remote.ApiService
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException
import javax.inject.Inject


class DetailRepository @Inject constructor(
    private val apiService: ApiService,
    private val foodDao: FoodDao
) {

    ///////////////////////getFoodByID////////////////////////////////
    suspend fun getFoodByID(foodID: String) = flow {
        try {
            val response = apiService.getDetailOfFood(foodId = foodID.toInt())

            when (response.code()) {
                in 200..299 -> {
                    emit(response.body()!!.meals?.get(0)!!)
                }

                in 300..399 -> {
                    throw Exception("ERROR CODE 300")
                }

                in 400..499 -> {
                    throw Exception("ERROR CODE400")
                }

                in 500..599 -> {
                    throw Exception("ERROR CODE 500")
                }

                else -> {
                    throw Exception("Unknown ERROR")
                }
            }
        } catch (e: UnknownHostException) {
            throw Exception("InternetError")
        } catch (e: Exception) {
            throw e
        }
    }

    //DB
    //add FavMeals
    suspend fun addFav(meal: FoodList.Meal) = foodDao.addMeal(meal)

    //remove Fav
    suspend fun deleteFav(meal: FoodList.Meal) = foodDao.deleteMeal(meal)

    //isFave
    suspend fun isFav(mealID:String) = foodDao.foodExists(id = mealID)


}