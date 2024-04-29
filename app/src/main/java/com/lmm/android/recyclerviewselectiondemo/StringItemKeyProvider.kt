package com.lmm.android.recyclerviewselectiondemo

import androidx.recyclerview.selection.ItemKeyProvider

class StringItemKeyProvider(scope: Int, private val items: List<String>?) : ItemKeyProvider<String>(scope) {
    override fun getKey(position: Int): String? {
        return items?.getOrNull(position)
    }

    override fun getPosition(key: String): Int {
        return items?.indexOf(key) ?: 0
    }
}