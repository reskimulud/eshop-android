package com.mankart.eshop.profile.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mankart.eshop.core.domain.model.Transaction
import com.mankart.eshop.core.utils.Helpers.timestampToDate
import com.mankart.eshop.profile.databinding.ItemTransactionBinding

class ListTransactionAdapter(
    private val listTransaction: List<Transaction>,
    private val onDetailClickCallback: (transactionId: String) -> Unit
): RecyclerView.Adapter<ListTransactionAdapter.ViewHolder>() {
    inner class ViewHolder(itemTransactionBinding: ItemTransactionBinding): RecyclerView.ViewHolder(itemTransactionBinding.root) {
        val tvTransactionId = itemTransactionBinding.tvTransactionId
        val tvTransactionDate = itemTransactionBinding.tvTransactionDate
        val tvDetail = itemTransactionBinding.tvDetail
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemTransactionBinding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemTransactionBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemTransaction = listTransaction[position]

        holder.apply {
            tvTransactionId.text = itemTransaction.id
            tvTransactionDate.text = itemTransaction.dateCreated.timestampToDate()

            tvDetail.setOnClickListener {
                onDetailClickCallback(itemTransaction.id)
            }
        }
    }

    override fun getItemCount(): Int = listTransaction.size
}