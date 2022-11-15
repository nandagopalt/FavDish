package com.amalwin.favdishapplication.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amalwin.favdishapplication.R
import com.amalwin.favdishapplication.databinding.ItemAllDishListBinding
import com.amalwin.favdishapplication.model.entities.FavDish
import com.bumptech.glide.Glide

class AllDishListItemsAdapter(
    private val selectedFavDishListener: (FavDish, String) -> Unit,
    private val isMoreRequired: Boolean = false
) :
    RecyclerView.Adapter<AllDishListItemsAdapter.ViewHolder>() {
    private var allDishList: List<FavDish> = listOf()
    private lateinit var itemAllDishListBinding: ItemAllDishListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        itemAllDishListBinding =
            ItemAllDishListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemAllDishListBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(allDishList.get(position), selectedFavDishListener, isMoreRequired)
    }

    override fun getItemCount(): Int {
        return allDishList.size
    }

    class ViewHolder(private val listItemBinding: ItemAllDishListBinding) :
        RecyclerView.ViewHolder(listItemBinding.root) {

        fun bind(
            favdish: FavDish,
            selectedFavDishListener: (FavDish, String) -> Unit,
            isMoreRequired: Boolean
        ) {
            Glide.with(listItemBinding.ivAllDishImage.context)
                .load(favdish.imagePath)
                .placeholder(
                    ContextCompat.getDrawable(
                        listItemBinding.ivAllDishImage.context,
                        R.drawable.ic_add_photo
                    )
                )
                .into(listItemBinding.ivAllDishImage)

            listItemBinding.tvAllDishTitle.text = favdish.title
            if (isMoreRequired) listItemBinding.ivMore.visibility = View.VISIBLE
            else listItemBinding.ivMore.visibility = View.GONE
            listItemBinding.allDishCardParent.setOnClickListener {
                selectedFavDishListener(favdish, "Navigate")
                /*Toast.makeText(
                    listItemBinding.root.context,
                    listItemBinding.tvAllDishTitle.text,
                    Toast.LENGTH_LONG
                ).show()*/
            }
            listItemBinding.ivMore.setOnClickListener {
                val popUpMenu: PopupMenu =
                    PopupMenu(listItemBinding.ivMore.context, listItemBinding.ivMore)
                popUpMenu.menuInflater.inflate(R.menu.dish_modification_menu, popUpMenu.menu)
                popUpMenu.show()

                popUpMenu.setOnMenuItemClickListener {
                    if (it.itemId == R.id.edit) {
                        selectedFavDishListener(favdish, "Edit")
                        /*Toast.makeText(
                            listItemBinding.ivMore.context,
                            "Edit :: ${favdish.title}",
                            Toast.LENGTH_LONG
                        ).show()*/
                        true
                    } else {
                        selectedFavDishListener(favdish, "Delete")
                        /*Toast.makeText(
                            listItemBinding.ivMore.context,
                            "Delete :: ${favdish.title}",
                            Toast.LENGTH_LONG
                        ).show()*/
                        true
                    }
                }
            }
        }
    }

    fun reLoadAllDishList(allDishList: List<FavDish>) {
        this.allDishList = allDishList
        notifyDataSetChanged()
    }
}