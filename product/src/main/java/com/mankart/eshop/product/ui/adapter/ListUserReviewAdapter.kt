package com.mankart.eshop.product.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mankart.eshop.core.domain.model.UserReview
import com.mankart.eshop.product.databinding.ItemReviewBinding

class ListUserReviewAdapter(private val listUserReview: List<UserReview>): RecyclerView.Adapter<ListUserReviewAdapter.ViewHolder>() {
    inner class ViewHolder(binding: ItemReviewBinding): RecyclerView.ViewHolder(binding.root) {
        var userRate = binding.rbUserRate
        var userName = binding.tvUserName
        var userReview = binding.tvUserReview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listUserReview[position]
        Log.e("Adapter", item.toString())

        holder.apply {
            userRate.rating = item.rate.toFloat()
            userName.text = item.user
            userReview.text = item.review
        }
    }

    override fun getItemCount(): Int = listUserReview.size
}