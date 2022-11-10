package com.amalwin.favdishapplication.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.amalwin.favdishapplication.R
import com.amalwin.favdishapplication.databinding.FragmentDishDetailsBinding
import com.amalwin.favdishapplication.views.activities.MainActivity
import com.bumptech.glide.Glide
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