package com.amalwin.favdishapplication.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.amalwin.favdishapplication.databinding.FragmentDishDetailsBinding
import com.amalwin.favdishapplication.views.activities.MainActivity


class DishDetailsFragment : Fragment() {
    private lateinit var dishDetailsBinding: FragmentDishDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        layoutInflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dishDetailsBinding = FragmentDishDetailsBinding.inflate(layoutInflater)
        return dishDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hideBottomNavView()
        val args: DishDetailsFragmentArgs by navArgs()
        dishDetailsBinding.tvDishDetailsTitle.text = args.let {
            val favDish = args.favDish
            favDish.title
        }
    }
}