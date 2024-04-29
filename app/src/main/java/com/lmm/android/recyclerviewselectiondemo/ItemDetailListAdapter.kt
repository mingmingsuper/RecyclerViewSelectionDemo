package com.lmm.android.recyclerviewselectiondemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView

class ItemDetailListAdapter(private val items: List<String>?) : RecyclerView.Adapter<ItemDetailListViewHolder>() {

    private var selectionTracker: SelectionTracker<String>? = null

//    init {
//        setHasStableIds(true) //设置稳定ID，保证RecyclerView的ViewHolder缓存不会因为Item的变化而失效
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDetailListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.string_item_cell, parent, false)
        return ItemDetailListViewHolder(view, items)
    }

    override fun getItemCount(): Int {
        return items?.size?: 0
    }

    override fun onBindViewHolder(holder: ItemDetailListViewHolder, position: Int) {
        holder.bind(items?.get(position)?: "")
        holder.itemView.isActivated = selectionTracker?.isSelected(items?.get(position)) == true
    }

    fun setSelectionTracker(selectionTracker: SelectionTracker<String>?) {
        this.selectionTracker = selectionTracker
    }

}

class ItemDetailListViewHolder(view: View, private val items: List<String>?) : RecyclerView.ViewHolder(view) {

    private val tvItem: TextView = view.findViewById(R.id.tv_item)

    init {

    }

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<String> {
        return StringItemDetails(adapterPosition, items?.getOrNull(adapterPosition))
    }

    fun bind(item: String) {
        tvItem.text = item
    }
}