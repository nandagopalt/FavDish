package com.amalwin.favdishapplication.views.fragments

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.amalwin.favdishapplication.R
import com.amalwin.favdishapplication.application.FavDishApplication
import com.amalwin.favdishapplication.databinding.FragmentDishDetailsBinding
import com.amalwin.favdishapplication.viewmodel.FavDishAddUpdateViewModel
import com.amalwin.favdishapplication.viewmodel.FavDishAddUpdateViewModelFactory
import com.amalwin.favdishapplication.views.activities.MainActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.IOException


class DishDetailsFragment : Fragment() {
    private var dishDetailsBinding: FragmentDishDetailsBinding? = null
    private val favDishAddUpdateViewModel: FavDishAddUpdateViewModel by viewModels {
        FavDishAddUpdateViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dishDetailsBinding = FragmentDishDetailsBinding.inflate(layoutInflater)
        return dishDetailsBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DishDetailsFragmentArgs by navArgs()
        if (requireActivity() is MainActivity)
            (requireActivity() as MainActivity).hideBottomNavView()
        args.favDish.let {
            dishDetailsBinding?.apply {
                try {
                    Glide.with(ivDishImage.context)
                        .load(it.imagePath)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource.let {
                                    Palette.from(it!!.toBitmap()).generate { palette ->
                                        palette.let {
                                            val color = it!!.vibrantSwatch?.rgb ?: 0
                                            dishDetailsBinding?.flDishDetailsParent?.setBackgroundColor(
                                                color
                                            )
                                        }
                                    }
                                }
                                return false
                            }
                        })
                        .centerCrop()
                        .into(ivDishImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                tvDishTitle.text = it.title.trim { it <= ' ' }
                tvDishTypeValue.text = it.type
                tvDishCategoryValue.text = it.category
                tvDishIngredientsValue.text = it.ingredients
                tvDishDirectionsToCookValue.text = it.instructions
                tvTimeForDishPreparation.text =
                    resources.getString(R.string.time_for_preparation, it.cookingTime)
                if (args.favDish.isFavDish) {
                    dishDetailsBinding
                        ?.ivFavorite
                        ?.setImageDrawable(
                            AppCompatResources.getDrawable(
                                requireActivity(),
                                R.drawable.ic_favorite_selected
                            )
                        )
                } else {
                    dishDetailsBinding
                        ?.ivFavorite
                        ?.setImageDrawable(
                            ContextCompat.getDrawable(
                                requireActivity(),
                                R.drawable.ic_favorite_unselected
                            )
                        )
                }

                llFavoriteParent.setOnClickListener {
                    args.favDish.isFavDish = !args.favDish.isFavDish
                    favDishAddUpdateViewModel.updateFavDishDetails(args.favDish)
                    if (args.favDish.isFavDish) {
                        dishDetailsBinding
                            ?.ivFavorite
                            ?.setImageDrawable(
                                AppCompatResources.getDrawable(
                                    requireActivity(),
                                    R.drawable.ic_favorite_selected
                                )
                            )
                        Toast.makeText(
                            requireActivity(),
                            "Dish added favorite !",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    } else {
                        dishDetailsBinding
                            ?.ivFavorite
                            ?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireActivity(),
                                    R.drawable.ic_favorite_unselected
                                )
                            )
                        Toast.makeText(
                            requireActivity(),
                            "Dish removed favorite !",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dish_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when(item.itemId) {
             R.id.share -> {
                Toast.makeText(requireActivity(), "Share button clicked !", Toast.LENGTH_LONG)
                    .show()
                 return true

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        dishDetailsBinding = null
    }
}