package com.tvchannels.sample.app.core.ext

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

inline fun <T, VH : RecyclerView.ViewHolder> ListAdapter<T, VH>.updateItems(
    crossinline onComplete: () -> Unit={},
    crossinline transform: (T) -> T

) {
    submitList(currentList.map(transform)) { onComplete() }
}