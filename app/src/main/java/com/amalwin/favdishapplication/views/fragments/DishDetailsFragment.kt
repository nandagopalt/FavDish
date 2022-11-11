package com.amalwin.favdishapplication.views.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.amalwin.favdishapplication.R
import com.amalwin.favdishapplication.databinding.FragmentDishDetailsBinding
import com.amalwin.favdishapplication.views.activities.MainActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.IOException


class DishDetailsFragment : Fragment() {
    private var dishDetailsBinding: FragmentDishDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        (requireActivity() as MainActivity).hideBottomNavView()
        val args: DishDetailsFragmentArgs by navArgs()
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
                llFavoriteParent.setOnClickListener {
                    Toast.makeText(requireActivity(), "Favorite clicked !", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        dishDetailsBinding = null
    }
}