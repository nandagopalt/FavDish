package com.amalwin.favdishapplication.viewmodel

import androidx.lifecycle.*
import com.amalwin.favdishapplication.model.database.FavDishRepository
import com.amalwin.favdishapplication.model.entities.FavDish
import kotlinx.coroutines.launch

class FavDishAddUpdateViewModel(private val favDishRepository: FavDishRepository) : ViewModel() {

    fun insertFavDishDetails(favDish: FavDish) =
        viewModelScope.launch { favDishRepository.insertFavDishInformation(favDish) }

    val favDishItemsList: LiveData<List<FavDish>> = favDishRepository.favDetailsList.asLiveData()

    fun updateFavDishDetails(favDish: FavDish?) = viewModelScope.launch {
        favDishRepository.updateFavDishInformation(favDish)
    }

    fun deleteFavDishDetails(favDish: FavDish) = viewModelScope.launch {
        favDishRepository.deleteFavDishInformation(favDish)
    }

    fun filterFavDishDetails(filter: String): LiveData<List<FavDish>> =
        favDishRepository.fetchFilteredFavDishInformation(filter).asLiveData()

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