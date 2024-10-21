package com.github.ebrahimi16153.mvvminxml.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ebrahimi16153.mvvminxml.data.model.Categories
import com.github.ebrahimi16153.mvvminxml.data.model.FoodList
import com.github.ebrahimi16153.mvvminxml.data.repository.HomeRepository
import com.github.ebrahimi16153.mvvminxml.util.Wrapper
import com.github.ebrahimi16153.mvvminxml.util.connection.CheckNetwork
import com.github.ebrahimi16153.mvvminxml.util.connection.CheckNetworkImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository) : ViewModel() {

        init {
            getRandomFood()
            getCategory()
            getFoodByFirstLetter("A")
        }

    val randomFood = MutableLiveData<Wrapper<FoodList.Meal?>>(Wrapper.Loading)
    val categories = MutableLiveData<Wrapper<MutableList<Categories.Category>>>(Wrapper.Loading)
    val foods = MutableLiveData<Wrapper<MutableList<FoodList.Meal>>>(Wrapper.Loading)
    val errorStatus = MutableLiveData<Boolean>()


    fun getRandomFood() = viewModelScope.launch {

        try {
            homeRepository.getRandomFood().collectLatest { itFood ->

                randomFood.postValue(Wrapper.Success(data = itFood))
                errorStatus.postValue(false)
            }
        } catch (e: Exception) {
            randomFood.postValue(Wrapper.Error(message = e.message.toString()))
            errorStatus.postValue(true)
        }
    }


    @OptIn(FlowPreview::class)
    fun searchFoods(searchQuery:String) = viewModelScope.launch {
        try {
            homeRepository.searchFoods(searchQuery = searchQuery).debounce(300).collect{ itFoods ->
                foods.postValue(Wrapper.Success(data = itFoods!!))
                errorStatus.postValue(false)
            }
        }catch (e:Exception){
            foods.postValue(Wrapper.Error("search ERROR : "+e.message.toString()))
            errorStatus.postValue(true)
        }
    }


    fun getCategory() = viewModelScope.launch {
        try {
            homeRepository.getCategory().collectLatest { itCategory ->

                categories.postValue(Wrapper.Success(data = itCategory!!))
                errorStatus.postValue(false)
            }
        } catch (e: Exception) {
            categories.postValue(Wrapper.Error(message = e.message.toString()))
            errorStatus.postValue(true)
        }
    }


    fun getFoodByFirstLetter(letter: String) = viewModelScope.launch {

        try {
            homeRepository.getFoodByFirstLetter(letter = letter).collectLatest { itFoods ->

                foods.postValue(Wrapper.Success(data = itFoods!!))
                errorStatus.postValue(false)

            }
        } catch (e: Exception) {
            foods.postValue(Wrapper.Error(message = e.message.toString()))
            errorStatus.postValue(true)
        }


    }


    fun getFoodsByCategory(cat: String) = viewModelScope.launch {
        foods.postValue(Wrapper.Loading)
        try {
            homeRepository.getFoodsByCategory(cat = cat).collect { itFoods ->
                foods.postValue(Wrapper.Success(data = itFoods!!))
                errorStatus.postValue(false)
            }
        } catch (e: Exception) {
            e.stackTrace
            foods.postValue(Wrapper.Error(message = e.message.toString()))
            errorStatus.postValue(true)
        }
    }


}