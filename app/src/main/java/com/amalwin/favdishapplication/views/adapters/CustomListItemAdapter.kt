package com.amalwin.favdishapplication.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amalwin.favdishapplication.databinding.ItemCustomListBinding

class CustomListItemAdapter(
    private val listItems: List<String>,
    private val selection: String,
    private val clickListener: (String, String) -> Unit
) :
    RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {

    private val values =
        listOf("Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemCustomListBinding: ItemCustomListBinding =
            ItemCustomListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemCustomListBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(listItems[position], clickListener)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    inner class ViewHolder(val view: ItemCustomListBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: String, clickListener: (String, String) -> Unit) {
            view.tvItemName.text = item
            view.container.setOnClickListener {
                clickListener(item, selection)
            }
        }
    }
}