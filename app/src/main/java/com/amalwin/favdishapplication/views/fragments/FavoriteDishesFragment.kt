package com.amalwin.favdishapplication.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.amalwin.favdishapplication.application.FavDishApplication
import com.amalwin.favdishapplication.databinding.FragmentFavoriteDishesBinding
import com.amalwin.favdishapplication.model.entities.FavDish
import com.amalwin.favdishapplication.viewmodel.FavoriteDishViewModel
import com.amalwin.favdishapplication.viewmodel.FavoriteDishViewModelProvider
import com.amalwin.favdishapplication.views.activities.MainActivity
import com.amalwin.favdishapplication.views.adapters.AllDishListItemsAdapter

class FavoriteDishesFragment : Fragment() {
    private var favoriteDishBinding: FragmentFavoriteDishesBinding? = null
    private val favoriteDishViewModel: FavoriteDishViewModel by viewModels {
        FavoriteDishViewModelProvider((requireActivity().application as FavDishApplication).repository)
    }
    private lateinit var favoriteListAdapter: AllDishListItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favoriteDishBinding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)
        return favoriteDishBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteDishViewModel.fetchFavoritesDishes().observe(viewLifecycleOwner) { favDishes ->
            favDishes.let {
                if (favDishes.isNotEmpty()) {
                    favoriteDishBinding!!.rvFavorites.apply {
                        visibility = View.VISIBLE
                        favoriteDishBinding!!.tvNoFavoritesAdded.visibility = View.GONE
                        layoutManager =
                            GridLayoutManager(requireActivity(), 2)
                        favoriteListAdapter = AllDishListItemsAdapter({ selectedFavDish: FavDish, operation :String ->
                            onItemClickListener(selectedFavDish, operation)
                        }, isMoreRequired = false)
                        favoriteListAdapter.reLoadAllDishList(favDishes)
                        adapter = favoriteListAdapter
                        /*for (item in it) {
                            Log.i("Favorite Dish", "Favorite Item : ${item.id} :: ${item.title}")
                        }*/
                    }
                } else {
                    favoriteDishBinding!!.rvFavorites.visibility = View.GONE
                    favoriteDishBinding!!.tvNoFavoritesAdded.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            ((requireActivity() as MainActivity).showBottomNavView())
        }
    }

    fun onItemClickListener(favDish: FavDish, operation: String) {
        Toast.makeText(
            requireActivity(),
            "Clicked Item : ${favDish.id} :: ${favDish.title}",
            Toast.LENGTH_LONG
        ).show()
        findNavController().navigate(
            FavoriteDishesFragmentDirections.actionNavigationFavoriteDishesToDishDetailsFragment(
                favDish
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteDishBinding = null
    }
}