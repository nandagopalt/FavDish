package com.amalwin.favdishapplication.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amalwin.favdishapplication.R
import com.amalwin.favdishapplication.databinding.ItemAllDishListBinding
import com.amalwin.favdishapplication.model.entities.FavDish
import com.bumptech.glide.Glide

class AllDishListItemsAdapter(private val selectedFavDishListener: (FavDish) -> Unit) :
    RecyclerView.Adapter<AllDishListItemsAdapter.ViewHolder>() {
    private var allDishList: List<FavDish> = listOf()
    private lateinit var itemAllDishListBinding: ItemAllDishListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        itemAllDishListBinding =
            ItemAllDishListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemAllDishListBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(allDishList.get(position), selectedFavDishListener)
    }

    override fun getItemCount(): Int {
        return allDishList.size
    }

    class ViewHolder(private val listItemBinding: ItemAllDishListBinding) :
        RecyclerView.ViewHolder(listItemBinding.root) {

        fun bind(favdish: FavDish, selectedFavDishListener: (FavDish) -> Unit) {
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
            listItemBinding.allDishCardParent.setOnClickListener {
                selectedFavDishListener(favdish)
                /*Toast.makeText(
                    listItemBinding.root.context,
                    listItemBinding.tvAllDishTitle.text,
                    Toast.LENGTH_LONG
                ).show()*/
            }
        }
    }

    fun reLoadAllDishList(allDishList: List<FavDish>) {
        this.allDishList = allDishList
        notifyDataSetChanged()
    }
}