package com.amalwin.favdishapplication.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.amalwin.favdishapplication.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {
    @Insert
    suspend fun insertFavDishData(favDish: FavDish)

    @Query("SELECT * FROM fav_dish_table ORDER BY id")
    fun fetchFavDetailsData(): Flow<List<FavDish>>
}