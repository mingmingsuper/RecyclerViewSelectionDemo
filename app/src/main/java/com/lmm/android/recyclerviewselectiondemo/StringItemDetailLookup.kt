package com.lmm.android.recyclerviewselectiondemo

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class StringItemDetailLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<String>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            val viewHolder = recyclerView.getChildViewHolder(view)
            if (viewHolder is ItemDetailListViewHolder) {
                return viewHolder.getItemDetails()
            }
        }
        return null
    }


}

class StringItemDetails(private val position: Int, private val item: String?) :
    ItemDetailsLookup.ItemDetails<String>() {

    override fun getPosition(): Int {
        return position
    }

    override fun getSelectionKey(): String? {
        return item
    }

    /**
     * 加上这个方法返回true则可以做到打击选择，第一个项目不用长按选中了，如果去了这个方法，第一个选中的项目则需要长按才会选中
     *
     * @param e
     * @return
     */
    override fun inSelectionHotspot(e: MotionEvent): Boolean {
        return true
    }
}