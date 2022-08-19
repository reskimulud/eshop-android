package com.mankart.eshop.favorite.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mankart.eshop.core.domain.model.Product
import com.mankart.eshop.core.utils.Helpers.formatIDR
import com.mankart.eshop.favorite.R
import com.mankart.eshop.favorite.databinding.ItemFavoriteProductBinding

class ListFavoriteProductAdapter(
    private val listProduct: List<Product>,
    private val onItemClickCallback: (productId: String) -> Unit,
    private val onFavBtnClickCallback: (productId: String) -> Unit
): RecyclerView.Adapter<ListFavoriteProductAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemFavoriteProductBinding): RecyclerView.ViewHolder(binding.root) {
        val ivProductImage = binding.ivProductImage
        val tvProductTitle = binding.tvProductTitle
        val tvProductPrice = binding.tvProductPrice
        val rbProductRating = binding.rbProductRating
        val root = binding.root
        val btnFavProduct = binding.btnFavProduct
        val itemResource: Resources = itemView.resources

        lateinit var productId: String
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemFavoriteProductBinding = ItemFavoriteProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemFavoriteProductBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = listProduct[position]
        holder.productId = product.id

        holder.apply {
            Glide.with(itemView.context)
                .load(product.image)
                .into(ivProductImage)
            tvProductTitle.text = product.title
            tvProductPrice.text = product.price.formatIDR()
            rbProductRating.rating = product.rating.toFloat()

            ivProductImage.contentDescription = itemResource.getString(R.string.product_image_of, product.title)

            root.setOnClickListener { onItemClickCallback(product.id) }
            btnFavProduct.setOnClickListener { onFavBtnClickCallback(product.id) }
        }
    }

    override fun getItemCount(): Int = listProduct.size

}