package com.amalwin.favdishapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.amalwin.favdishapplication.model.database.FavDishRepository
import com.amalwin.favdishapplication.model.entities.FavDish

class FavoriteDishViewModel(private val favDishRepository: FavDishRepository) : ViewModel() {
    init {
        Log.i("TAG", "FavoriteDishView Model Instantiated !")
    }

    fun fetchFavoritesDishes(): LiveData<List<FavDish>> =
        favDishRepository.fetchFavorites.asLiveData()


    override fun onCleared() {
        super.onCleared()
    }

}

class FavoriteDishViewModelProvider(private val favDishRepository: FavDishRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteDishViewModel::class.java))
            return FavoriteDishViewModel(favDishRepository) as T
        throw IllegalArgumentException("View not matched !")
    }
}