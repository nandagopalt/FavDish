package com.amalwin.favdishapplication.model.network

import com.amalwin.favdishapplication.model.entities.RandomDish
import io.reactivex.rxjava3.core.Single
import retrofit.http.GET
import retrofit.http.Query

interface FavDishesAPI {
    @GET("/recipes/random")
    fun getRandomDishes(
        @Query("apiKey") apiKey: String,
        @Query("tags") tags: String,
        @Query("number") number: Int,
        @Query("limitLicense") limitLicense: Boolean
    ): Single<RandomDish.Recipes>
}