package com.mankart.eshop.product.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mankart.eshop.core.domain.model.Product
import com.mankart.eshop.core.utils.DiffUtilCallback
import com.mankart.eshop.product.databinding.ItemProductBinding

class ListProductAdapter: PagingDataAdapter<Product, ListProductAdapter.ViewHolder>(DiffUtilCallback()) {
    inner class ViewHolder(binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root) {
        var image = binding.ivImage
        var title = binding.tvTitle
        var price = binding.tvPrice
        var rating = binding.rbRating
        var tvRating = binding.tvRating
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)

        holder.apply {
            Glide.with(itemView.context)
                .load(product?.image)
                .into(image)
            title.text = product?.title
            price.text = product?.price.toString()
            rating.rating = product?.rating?.toFloat() ?: 0f
            tvRating.text = product?.rating.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemProductBinding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemProductBinding)
    }
}