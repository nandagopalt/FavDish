package com.amalwin.favdishapplication.application

import android.app.Application
import com.amalwin.favdishapplication.model.database.FavDishRepository
import com.amalwin.favdishapplication.model.database.FavDishRoomDatabase

class FavDishApplication : Application() {
    // Instantiate the database and repository using the lazy so that the instances will be created when is needed rather than application gets created.
    private val database by lazy { FavDishRoomDatabase.getDatabase((this@FavDishApplication)) }
    val repository by lazy { FavDishRepository(database.favDishDao()) }
}