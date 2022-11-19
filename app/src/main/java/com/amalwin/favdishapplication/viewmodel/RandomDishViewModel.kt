package com.amalwin.favdishapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amalwin.favdishapplication.model.entities.RandomDish
import com.amalwin.favdishapplication.model.network.FavDishesAPIService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class RandomDishViewModel : ViewModel() {
    private val favDishesAPIService = FavDishesAPIService()
    private val compositeDisposable = CompositeDisposable()

    private val loadingFavDishLiveData = MutableLiveData<Boolean>()
    val _loadingFavDishLiveData: LiveData<Boolean>
        get() = loadingFavDishLiveData

    private val favDishResponseSuccess = MutableLiveData<RandomDish.Recipes>()
    val _favDishResponseSuccess: LiveData<RandomDish.Recipes>
        get() = favDishResponseSuccess

    private val favDishResponseError = MutableLiveData<Boolean>()
    val _favDishResponseError: LiveData<Boolean>
        get() = favDishResponseError

    fun getRandomFavDishes() {
        loadingFavDishLiveData.value = true

        compositeDisposable.add(
            favDishesAPIService.fetchRandomDishes(
                "bcbf0531cccb4e39b292f98122b51fe4",
                "vegetarian,dessert", 1, true
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RandomDish.Recipes>() {
                    override fun onSuccess(value: RandomDish.Recipes) {
                        loadingFavDishLiveData.value = false
                        favDishResponseSuccess.value = value
                        favDishResponseError.value = false
                    }

                    override fun onError(e: Throwable) {
                        loadingFavDishLiveData.value = false
                        favDishResponseError.value = false
                        e.printStackTrace()
                    }
                })
        )
    }
}