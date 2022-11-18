package com.amalwin.favdishapplication.model.network

import com.amalwin.favdishapplication.model.entities.RandomDish
import io.reactivex.rxjava3.core.Single
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory

class FavDishesAPIService {
    private val favDishAPI = Retrofit.Builder().baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build().create(FavDishesAPI::class.java)

    fun fetchRandomDishes(
        apiKey: String,
        tags: String,
        number: Int,
        limitLicense: Boolean
    ): Single<RandomDish.Recipes> {
        return favDishAPI.getRandomDishes(apiKey, tags, number, limitLicense)
    }
}