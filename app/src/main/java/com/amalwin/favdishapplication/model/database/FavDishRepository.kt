package com.amalwin.favdishapplication.model.database

import androidx.annotation.WorkerThread
import com.amalwin.favdishapplication.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

class FavDishRepository(private val favDishDao: FavDishDao) {

    @WorkerThread
    suspend fun insertFavDishInformation(favDish: FavDish) {
        favDishDao.insertFavDishData(favDish)
    }

    val favDetailsList: Flow<List<FavDish>> = favDishDao.fetchFavDetailsData()
}