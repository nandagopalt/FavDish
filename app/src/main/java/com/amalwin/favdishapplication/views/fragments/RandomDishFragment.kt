package com.amalwin.favdishapplication.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amalwin.favdishapplication.databinding.FragmentRandomDishBinding

class RandomDishFragment : Fragment() {

    private var randomDishBinding: FragmentRandomDishBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        randomDishBinding = FragmentRandomDishBinding.inflate(layoutInflater)
        return randomDishBinding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        randomDishBinding = null
    }
}