package com.github.ebrahimi16153.mvvminxml.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ebrahimi16153.mvvminxml.data.model.FoodList
import com.github.ebrahimi16153.mvvminxml.data.repository.FavRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavViewModel @Inject constructor(private val favRepository: FavRepository):ViewModel() {


    val favoriteMeals = MutableLiveData<List<FoodList.Meal>>(emptyList())
     fun getFavList() = viewModelScope.launch {

        favRepository.listOfMeal().collectLatest { itMeals ->
            favoriteMeals.postValue(itMeals)
        }

    }




}