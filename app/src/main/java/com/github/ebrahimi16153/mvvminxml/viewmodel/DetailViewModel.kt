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



}