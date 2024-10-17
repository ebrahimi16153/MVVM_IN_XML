package com.github.ebrahimi16153.mvvminxml.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ebrahimi16153.mvvminxml.data.model.FoodList
import com.github.ebrahimi16153.mvvminxml.data.repository.DetailRepository
import com.github.ebrahimi16153.mvvminxml.util.Wrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(private val detailRepository: DetailRepository):ViewModel() {

    val detailFood = MutableLiveData<Wrapper<FoodList.Meal>>(Wrapper.Loading)
    val isFav = MutableLiveData<Boolean>(false)

    fun detailFood (foodID:String) =  viewModelScope.launch {
        try {
            detailRepository.getFoodByID(foodID).collectLatest { itFood ->
                detailFood.postValue(Wrapper.Success(data = itFood))
            }
        }catch (e:UnknownHostException){
            Wrapper.Error(message = e.message.toString())
        }catch (e:Exception){
            Wrapper.Error(message = e.message.toString())
        }


    }

    fun addFav(meal: FoodList.Meal) = viewModelScope.launch {
        detailRepository.addFav(meal)
    }

    fun deleteFav(meal: FoodList.Meal) = viewModelScope.launch {
        detailRepository.deleteFav(meal)
    }

    fun isFav(mealId:String) = viewModelScope.launch { detailRepository.isFav(mealId).collectLatest { isFav.postValue(it) } }



}