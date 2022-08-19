package com.mankart.eshop.product.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mankart.eshop.core.domain.model.Product
import com.mankart.eshop.core.utils.DiffUtilCallback
import com.mankart.eshop.core.utils.Helpers.formatIDR
import com.mankart.eshop.product.R
import com.mankart.eshop.product.databinding.ItemProductBinding

class ListProductAdapter(val onItemCallback: (productId: String) -> Unit): PagingDataAdapter<Product, ListProductAdapter.ViewHolder>(DiffUtilCallback()) {
    inner class ViewHolder(binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root) {
        var root = binding.root
        var image = binding.ivImage
        var title = binding.tvTitle
        var price = binding.tvPrice
        var rating = binding.rbRating
        var tvRating = binding.tvRating

        val itemResources: Resources = itemView.resources
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)

        holder.apply {
            Glide.with(itemView.context)
                .load(product?.image)
                .into(image)
            title.text = product?.title
            price.text = product?.price?.formatIDR()
            rating.rating = product?.rating?.toFloat() ?: 0f
            tvRating.text = product?.rating.toString()

            image.contentDescription = itemResources.getString(R.string.product_image_of, product?.title)

            root.setOnClickListener {
                product?.let { p -> onItemCallback(p.id) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemProductBinding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemProductBinding)
    }
}