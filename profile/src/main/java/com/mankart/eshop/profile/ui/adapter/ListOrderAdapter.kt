package com.mankart.eshop.profile.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mankart.eshop.core.domain.model.Order
import com.mankart.eshop.core.utils.Helpers.formatIDR
import com.mankart.eshop.profile.databinding.ItemOrderBinding
import timber.log.Timber

class ListOrderAdapter(
    private val listOrder: List<Order>,
    private val onItemClickCallback: (productId: String) -> Unit,
    private val onBtnRateClickCallback: (productId: String, rating: Int, review: String) -> Unit
): RecyclerView.Adapter<ListOrderAdapter.ViewHolder>() {
    private var rating: Int = 0

    inner class ViewHolder(itemOrderBinding: ItemOrderBinding): RecyclerView.ViewHolder(itemOrderBinding.root) {
        val ivProduct = itemOrderBinding.ivProduct
        val tvProductTitle = itemOrderBinding.tvProductTitle
        val tvQuantity = itemOrderBinding.tvQuantity
        val tvSubTotal = itemOrderBinding.tvSubTotal
        val rbYourRating = itemOrderBinding.rbYourRating
        val btnRate = itemOrderBinding.btnRate

        val root = itemOrderBinding.root

        val rbInputUser = itemOrderBinding.rbRatingUserInput
        val etInputReview = itemOrderBinding.etInputReview
        val btnPostReview = itemOrderBinding.btnPostReview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemOrderBinding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemOrderBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemOrder = listOrder[position]

        holder.apply {
            Glide.with(itemView.context)
                .load(itemOrder.image)
                .into(ivProduct)
            tvProductTitle.text = itemOrder.title
            tvQuantity.text = "${itemOrder.quantity}x ${itemOrder.price.formatIDR()}"
            tvSubTotal.text = (itemOrder.price * itemOrder.quantity).formatIDR()

            Timber.d("Rating: ${itemOrder.yourRating}")

            if (itemOrder.yourRating == 0 && itemOrder.yourReview == null) {
                rbYourRating.visibility = ViewGroup.GONE
                btnRate.visibility = ViewGroup.VISIBLE
            } else {
                rbYourRating.visibility = ViewGroup.VISIBLE
                btnRate.visibility = ViewGroup.GONE
                rbYourRating.rating = itemOrder.yourRating.toFloat()
            }

            root.setOnClickListener { onItemClickCallback(itemOrder.id) }
            btnRate.setOnClickListener {
                rbInputUser.visibility = ViewGroup.VISIBLE
                etInputReview.visibility = ViewGroup.VISIBLE
                btnPostReview.visibility = ViewGroup.VISIBLE
            }

            rbInputUser.setOnRatingBarChangeListener { _, rating, _ ->
                this@ListOrderAdapter.rating = rating.toInt()
            }

            btnPostReview.setOnClickListener {
                val review = etInputReview.text.toString()
                if (rating > 0 && review.isNotEmpty()) {
                    onBtnRateClickCallback(itemOrder.id, rating, review)
                }
            }

        }
    }

    override fun getItemCount(): Int = listOrder.size
}