package com.mankart.eshop.product.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mankart.eshop.core.domain.model.ProductCategory
import com.mankart.eshop.product.databinding.ItemBtnCategoryBinding

class ListCategoryAdapter(
    private val categories: List<ProductCategory>,
    val onItemClickCallback: (categoryId: String, categoryName: String) -> Unit
): RecyclerView.Adapter<ListCategoryAdapter.ViewHolder>() {
    inner class ViewHolder(binding: ItemBtnCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        val root = binding.root
        val tvCategory = binding.tvCategoryName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBtnCategoryBinding = ItemBtnCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBtnCategoryBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categories[position]
        holder.tvCategory.text = item.name

        holder.root.setOnClickListener {
            onItemClickCallback(item.id, item.name)
        }
    }

    override fun getItemCount(): Int = categories.size

}