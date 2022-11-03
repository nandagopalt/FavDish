package com.amalwin.favdishapplication.viewmodel

import androidx.lifecycle.*
import com.amalwin.favdishapplication.model.database.FavDishRepository
import com.amalwin.favdishapplication.model.entities.FavDish
import kotlinx.coroutines.launch

class FavDishAddUpdateViewModel(private val favDishRepository: FavDishRepository) : ViewModel() {

    fun insertFavDishDetails(favDish: FavDish) =
        viewModelScope.launch { favDishRepository.insertFavDishInformation(favDish) }

    val favDishItemsList: LiveData<List<FavDish>> = favDishRepository.favDetailsList.asLiveData()
}

class FavDishAddUpdateViewModelFactory(private val favDishRepository: FavDishRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavDishAddUpdateViewModel::class.java))
            return FavDishAddUpdateViewModel(favDishRepository) as T
        throw IllegalArgumentException("View not matched !")
    }
}