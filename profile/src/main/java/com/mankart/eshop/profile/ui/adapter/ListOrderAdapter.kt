package com.mankart.eshop.profile.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mankart.eshop.core.domain.model.Order
import com.mankart.eshop.core.utils.Helpers.formatIDR
import com.mankart.eshop.profile.databinding.ItemOrderBinding

class ListOrderAdapter(
    private val listOrder: List<Order>,
    private val onItemClickCallback: (productId: String) -> Unit,
    private val onBtnRateClickCallback: (productId: String) -> Unit
): RecyclerView.Adapter<ListOrderAdapter.ViewHolder>() {
    inner class ViewHolder(itemOrderBinding: ItemOrderBinding): RecyclerView.ViewHolder(itemOrderBinding.root) {
        val ivProduct = itemOrderBinding.ivProduct
        val tvProductTitle = itemOrderBinding.tvProductTitle
        val tvQuantity = itemOrderBinding.tvQuantity
        val tvSubTotal = itemOrderBinding.tvSubTotal
        val rbYourRating = itemOrderBinding.rbYourRating
        val btnRate = itemOrderBinding.btnRate

        val root = itemOrderBinding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemOrderBinding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemOrderBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemOrder = listOrder[position]

        holder.apply {
            Glide.with(itemView.context)
                .load(itemOrder.image)
                .into(ivProduct)
            tvProductTitle.text = itemOrder.title
            tvQuantity.text = "${itemOrder.quantity}x ${itemOrder.price.formatIDR()}"
            tvSubTotal.text = (itemOrder.price * itemOrder.quantity).formatIDR()

            Log.e("Adapter", "Rating: ${itemOrder.yourRating}")

            if (itemOrder.yourRating == 0 && itemOrder.yourReview == null) {
                rbYourRating.visibility = ViewGroup.GONE
                btnRate.visibility = ViewGroup.VISIBLE
            } else {
                rbYourRating.visibility = ViewGroup.VISIBLE
                btnRate.visibility = ViewGroup.GONE
                rbYourRating.rating = itemOrder.yourRating.toFloat()
            }

            root.setOnClickListener { onItemClickCallback(itemOrder.id) }
            btnRate.setOnClickListener { onBtnRateClickCallback(itemOrder.id) }
        }
    }

    override fun getItemCount(): Int = listOrder.size
}