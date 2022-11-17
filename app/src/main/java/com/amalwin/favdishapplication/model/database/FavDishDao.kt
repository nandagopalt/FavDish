package com.amalwin.favdishapplication.model.database

import androidx.room.*
import com.amalwin.favdishapplication.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {
    @Insert
    suspend fun insertFavDishData(favDish: FavDish)

    @Query("SELECT * FROM fav_dish_table ORDER BY id")
    fun fetchFavDetailsData(): Flow<List<FavDish>>

    @Update
    suspend fun updateFavDetailsData(favDish: FavDish?)

    @Query("SELECT * FROM fav_dish_table WHERE is_fav_dish = 1")
    fun fetchFavorites(): Flow<List<FavDish>>

    @Delete
    suspend fun deleteFavDetailsData(favDish: FavDish)

    @Query("SELECT * FROM fav_dish_table WHERE type = :filter order by id")
    fun fetchFilteredFavDishData(filter: String): Flow<List<FavDish>>
}