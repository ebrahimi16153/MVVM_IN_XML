package com.github.ebrahimi16153.mvvminxml.data.repository

import com.github.ebrahimi16153.mvvminxml.data.local.FoodDao
import com.github.ebrahimi16153.mvvminxml.data.model.FoodList
import javax.inject.Inject



class FavRepository @Inject constructor(private val dao:FoodDao) {

    suspend fun addFav(meal:FoodList.Meal) = dao.addMeal(meal = meal)
    suspend fun deleteFav(meal: FoodList.Meal) = dao.deleteMeal(meal = meal)
    suspend fun listOfMeal() = dao.getAllMeal()


}