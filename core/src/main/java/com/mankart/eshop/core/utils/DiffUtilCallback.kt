package com.mankart.eshop.core.utils

import androidx.recyclerview.widget.DiffUtil
import com.mankart.eshop.core.domain.model.Product

class DiffUtilCallback: DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return when {
            oldItem.id != newItem.id -> false
            oldItem.title != newItem.title -> false
            oldItem.image != newItem.image -> false
            oldItem.description != newItem.description -> false
            oldItem.price != newItem.price -> false
            oldItem.rating != newItem.rating -> false
            oldItem.countRate != newItem.countRate -> false
            else -> true
        }
    }
}