package com.mankart.eshop.cart.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mankart.eshop.cart.R
import com.mankart.eshop.cart.databinding.ItemCartProductBinding
import com.mankart.eshop.core.domain.model.CartItem
import com.mankart.eshop.core.utils.Helpers.formatIDR

class ListCartAdapter(
    private val listCart: List<CartItem>,
    private val onItemClickCallback: (itemId: String) -> Unit,
    private val onBtnIncreaseClickCallback: (itemId: String, qty: Int) -> Unit,
    private val onBtnDecreaseClickCallback: (itemId: String, qty: Int) -> Unit
): RecyclerView.Adapter<ListCartAdapter.ViewHolder>() {
    inner class ViewHolder(itemCartProductBinding: ItemCartProductBinding): RecyclerView.ViewHolder(itemCartProductBinding.root) {
        val ivProductImage = itemCartProductBinding.ivProductImage
        val tvProductTitle = itemCartProductBinding.tvProductTitle
        val tvProductPrice = itemCartProductBinding.tvProductPrice
        val tvSubTotal = itemCartProductBinding.tvSubTotal
        val tvQty = itemCartProductBinding.tvQty
        val ibIncrease = itemCartProductBinding.ibIncrease
        val ibDecrease = itemCartProductBinding.ibDecrease
        val root = itemCartProductBinding.root
        val itemResources: Resources = itemView.context.resources

        lateinit var itemId: String
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemCartProductBinding = ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemCartProductBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = listCart[position]

        holder.apply {
            Glide.with(itemView.context)
                .load(cartItem.image)
                .into(ivProductImage)

            tvProductTitle.text = cartItem.title
            tvProductPrice.text = StringBuilder("@").append(cartItem.price.formatIDR())
            tvQty.text = cartItem.quantity.toString()
            tvSubTotal.text = (cartItem.price * cartItem.quantity).formatIDR()

            ivProductImage.contentDescription = itemResources.getString(R.string.product_image_of, cartItem.title)

            root.setOnClickListener {
                onItemClickCallback(cartItem.productId)
            }
            ibIncrease.setOnClickListener {
                onBtnIncreaseClickCallback(cartItem.id, cartItem.quantity + 1)
            }
            ibDecrease.setOnClickListener {
                onBtnDecreaseClickCallback(cartItem.id, cartItem.quantity - 1)
            }

            itemId = cartItem.id
        }
    }

    override fun getItemCount(): Int = listCart.size
}