package com.amalwin.favdishapplication.views.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amalwin.favdishapplication.R
import com.amalwin.favdishapplication.application.FavDishApplication
import com.amalwin.favdishapplication.databinding.DialogCustomListBinding
import com.amalwin.favdishapplication.databinding.FragmentAllDishesBinding
import com.amalwin.favdishapplication.model.entities.FavDish
import com.amalwin.favdishapplication.utils.Constants
import com.amalwin.favdishapplication.viewmodel.FavDishAddUpdateViewModel
import com.amalwin.favdishapplication.viewmodel.FavDishAddUpdateViewModelFactory
import com.amalwin.favdishapplication.views.activities.AddUpdateDishActivity
import com.amalwin.favdishapplication.views.activities.MainActivity
import com.amalwin.favdishapplication.views.adapters.AllDishListItemsAdapter
import com.amalwin.favdishapplication.views.adapters.CustomListItemAdapter

class AllDishesFragment : Fragment() {

    private lateinit var allDishesBinding: FragmentAllDishesBinding
    private lateinit var allDishListItemAdapter: AllDishListItemsAdapter
    private lateinit var filterListItemDialog: Dialog
    private val favDishAddUpdateViewModel: FavDishAddUpdateViewModel by viewModels {
        FavDishAddUpdateViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        allDishesBinding =
            FragmentAllDishesBinding.inflate(inflater, container, false)
        return allDishesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allDishesBinding.rvAllDish.layoutManager = GridLayoutManager(requireActivity(), 2)
        allDishListItemAdapter =
            AllDishListItemsAdapter({ selectedFavDish: FavDish, operation: String ->
                onListItemSelectionListener(selectedFavDish, operation)
            }, isMoreRequired = true)
        allDishesBinding.rvAllDish.adapter = allDishListItemAdapter

        favDishAddUpdateViewModel.favDishItemsList.observe(viewLifecycleOwner) {
            it.let { dishes ->
                if (dishes.isEmpty()) {
                    allDishesBinding.rvAllDish.visibility = View.GONE
                    allDishesBinding.tvDishYetToAdd.visibility = View.VISIBLE
                } else {
                    allDishesBinding.rvAllDish.visibility = View.VISIBLE
                    allDishesBinding.tvDishYetToAdd.visibility = View.GONE
                    allDishListItemAdapter.reLoadAllDishList(dishes)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    @Suppress("DEPRECATION")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.all_dishes_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }
            R.id.filter_dish -> {
                showFilterListDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onListItemSelectionListener(favDish: FavDish, operation: String) {
        Toast.makeText(requireActivity(), favDish.title, Toast.LENGTH_LONG).show()
        when (operation) {
            "Navigate" -> {
                val action =
                    AllDishesFragmentDirections.actionNavigationAllDishesToDishDetailsFragment(
                        favDish
                    )
                findNavController().navigate(action)
            }
            "Edit" -> {
                Toast.makeText(requireActivity(), "Edit Dish", Toast.LENGTH_LONG).show()
                val intent: Intent = Intent(requireActivity(), AddUpdateDishActivity::class.java)
                intent.putExtra("selected_dish", favDish)
                startActivity(intent)
            }
            "Delete" -> {
                Toast.makeText(requireActivity(), "Delete Dish", Toast.LENGTH_LONG).show()
                val alertDialog = AlertDialog.Builder(requireActivity())
                    .setMessage(resources.getString(R.string.delete_alert_message, favDish.title))
                    .setTitle(resources.getString(R.string.delete_alert_title))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setPositiveButton(
                        "Confirm"
                    ) { dialogInterface, _ ->
                        Toast.makeText(requireActivity(), "Confirm Delete", Toast.LENGTH_LONG)
                            .show()
                        favDishAddUpdateViewModel.deleteFavDishDetails(favDish)
                        dialogInterface.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialogInterface, _ ->
                        Toast.makeText(requireActivity(), "Cancel Delete", Toast.LENGTH_LONG)
                            .show()
                        dialogInterface.dismiss()
                    }
                alertDialog.create().show()
            }
        }
    }

    fun showFilterListDialog() {
        val dishTypes = Constants.dishTypes()
        dishTypes.add(0, "All")
        filterListItemDialog = Dialog(requireActivity())
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        filterListItemDialog.setContentView(binding.root)
        binding.tvDlgCustomTitle.text = resources.getString(R.string.select_title_for_filter)
        binding.rvListDialog.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = CustomListItemAdapter(
            dishTypes,
            "Filter"
        ) { itemSelected: String, String -> onFilterItemClickListener(itemSelected, "Filter") }
        binding.rvListDialog.adapter = adapter
        filterListItemDialog.show()
    }

    fun onFilterItemClickListener(itemSelected: String, operation: String) {
        if (itemSelected.equals("ALL", true)) {
            favDishAddUpdateViewModel.favDishItemsList.observe(viewLifecycleOwner) { dishes ->
                dishes.let {
                    if (dishes.isNotEmpty()) {
                        allDishesBinding.rvAllDish.visibility = View.VISIBLE
                        allDishesBinding.tvDishYetToAdd.visibility = View.GONE
                        allDishListItemAdapter.reLoadAllDishList(dishes)
                    } else {
                        allDishesBinding.rvAllDish.visibility = View.GONE
                        allDishesBinding.tvDishYetToAdd.visibility = View.VISIBLE
                    }
                }
            }
        } else {
            favDishAddUpdateViewModel.filterFavDishDetails(itemSelected)
                .observe(viewLifecycleOwner) { dishes ->
                    if (dishes.isNotEmpty()) {
                        allDishesBinding.rvAllDish.visibility = View.VISIBLE
                        allDishesBinding.tvDishYetToAdd.visibility = View.GONE
                        allDishListItemAdapter.reLoadAllDishList(dishes)
                    } else {
                        allDishesBinding.rvAllDish.visibility = View.GONE
                        allDishesBinding.tvDishYetToAdd.visibility = View.VISIBLE
                    }
                }
        }
        Toast.makeText(requireActivity(), "Selected : $itemSelected", Toast.LENGTH_LONG).show()
        if (filterListItemDialog.isShowing)
            filterListItemDialog.dismiss()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).showBottomNavView()
    }

}
