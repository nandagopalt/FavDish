package com.amalwin.favdishapplication.views.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amalwin.favdishapplication.R
import com.amalwin.favdishapplication.application.FavDishApplication
import com.amalwin.favdishapplication.databinding.FragmentRandomDishBinding
import com.amalwin.favdishapplication.model.entities.FavDish
import com.amalwin.favdishapplication.model.entities.RandomDish
import com.amalwin.favdishapplication.viewmodel.FavDishAddUpdateViewModel
import com.amalwin.favdishapplication.viewmodel.FavDishAddUpdateViewModelFactory
import com.amalwin.favdishapplication.viewmodel.RandomDishViewModel
import com.bumptech.glide.Glide

class RandomDishFragment : Fragment() {

    private var randomDishBinding: FragmentRandomDishBinding? = null
    private val randomDishViewModel: RandomDishViewModel by viewModels()

    private val favDishAddUpdateViewModel: FavDishAddUpdateViewModel by viewModels {
        FavDishAddUpdateViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    private lateinit var randomDish: RandomDish.Recipe
    private var dishType: String = "Other"
    private var dishCategory: String = "Other"
    private var dishIngredients: String = ""
    private var isAddedToFavorites: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        randomDishBinding = FragmentRandomDishBinding.inflate(layoutInflater)
        return randomDishBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isAddedToFavorites = false
        randomDishViewModel.getRandomFavDishes()

        randomDishViewModelObserver()

        randomDishBinding!!.srlFavdishParent.setOnRefreshListener {
            isAddedToFavorites = false
            randomDishViewModel.getRandomFavDishes()
        }

        var isFavorite = false
        randomDishBinding!!.llFavdishContainer.setOnClickListener {
            val favDishID = 0
            val favDishImageSource = "Online"
            if (isAddedToFavorites) {
                Toast.makeText(requireActivity(), "Already added to favorites !", Toast.LENGTH_LONG)
                    .show()
            } else {
                isFavorite = !isFavorite
                if (isFavorite) {
                    randomDishBinding!!.ivFavorite.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireActivity(),
                            R.drawable.ic_favorite_selected
                        )
                    )
                } else {
                    randomDishBinding!!.ivFavorite.setImageDrawable(
                        AppCompatResources.getDrawable(
                            requireActivity(),
                            R.drawable.ic_favorite_unselected
                        )
                    )
                }
                val randomFavDish = FavDish(
                    favDishID,
                    randomDish.image,
                    favDishImageSource,
                    randomDish.title,
                    randomDish.dishTypes[0],
                    dishCategory,
                    dishIngredients,
                    randomDish.readyInMinutes.toString(),
                    randomDish.instructions,
                    isFavorite
                )
                favDishAddUpdateViewModel.insertFavDishDetails(randomFavDish)
                isAddedToFavorites = true
                Toast.makeText(requireActivity(), "Added to favorites !", Toast.LENGTH_LONG)
                    .show()

            }

        }
    }

    fun randomDishViewModelObserver() {
        randomDishViewModel._favDishResponseSuccess.observe(viewLifecycleOwner) { dishes ->
            dishes?.let {
                Log.i("Random Dish:", it.recipes[0].toString())
                if (randomDishBinding!!.srlFavdishParent.isRefreshing) {
                    randomDishBinding!!.srlFavdishParent.isRefreshing = false
                }
                randomDish = it.recipes[0]
                Glide.with(requireActivity())
                    .load(randomDish.image)
                    .placeholder(R.color.secondaryColor)
                    .fitCenter()
                    .into(randomDishBinding!!.ivFavdishImage)
                randomDishBinding!!.tvDishTitle.text =
                    randomDish.title.trim { it <= ' ' }
                dishType = "Other"
                randomDish.dishTypes.let {
                    if (randomDish.dishTypes.isNotEmpty()) {
                        dishType = ""
                        if (randomDish.dishTypes.size > 1) {
                            for (randomDishType in randomDish.dishTypes) {
                                dishType = "$dishType,$randomDishType"
                            }
                        } else {
                            dishType = randomDish.dishTypes[0]
                        }
                    }
                }
                randomDishBinding!!.tvDishTypeValue.text = dishType
                randomDishBinding!!.tvDishCatgeoryValue.text = dishCategory
                for (ingredients in randomDish.extendedIngredients) {
                    if (dishIngredients.isEmpty())
                        dishIngredients = ingredients.original
                    else
                        dishIngredients = "${dishIngredients},\n${ingredients.original}"
                }
                randomDishBinding!!.tvDishIngredientsValue.text = dishIngredients

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    randomDishBinding!!.tvDishDirectionToCookValue.text =
                        Html.fromHtml(randomDish.instructions, Html.FROM_HTML_MODE_COMPACT)
                            .toString()
                else
                    @Suppress("DEPRECATION")
                    randomDishBinding!!.tvDishDirectionToCookValue.text =
                        Html.fromHtml(randomDish.instructions)
                            .toString()

                randomDishBinding!!.ivFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_unselected
                    )
                )

                randomDishBinding!!.tvDishTimeToCookValue.text =
                    resources.getString(
                        R.string.time_for_preparation,
                        randomDish.readyInMinutes.toString()
                    )
            }
        }

        randomDishViewModel._favDishResponseError.observe(viewLifecycleOwner) { error ->
            error?.let {
                Log.i("Random Dish error:", "$error")
                if (randomDishBinding!!.srlFavdishParent.isRefreshing) {
                    randomDishBinding!!.srlFavdishParent.isRefreshing = false
                }
            }
        }

        randomDishViewModel._loadingFavDishLiveData.observe(viewLifecycleOwner) { loading ->
            loading?.let {
                Log.i("Random Dish loading:", "$loading")
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        randomDishBinding = null
    }
}