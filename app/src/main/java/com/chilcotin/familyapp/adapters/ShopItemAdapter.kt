package com.chilcotin.familyapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.chilcotin.familyapp.databinding.ShopItemBinding
import com.chilcotin.familyapp.entities.ShopItem

class ShopItemAdapter(private val listener: OnItemClickListener) :
    ListAdapter<ShopItem, ShopItemAdapter.ShopItemViewHolder>(ItemComparator()) {

    inner class ShopItemViewHolder(private val binding: ShopItemBinding) :
        ViewHolder(binding.root) {

        init {
            binding.apply {
                checkBox.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val shopItem = getItem(position)
                        listener.onCheckedBoxClick(shopItem, checkBox.isChecked)
                    }
                }
            }
        }

        fun setData(shopItem: ShopItem) {
            binding.apply {
                tvTitle.text = shopItem.title
                checkBox.isChecked = shopItem.isChecked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val binding = ShopItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.setData(currentItem)
    }

    class ItemComparator : DiffUtil.ItemCallback<ShopItem>() {
        override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean =
            oldItem == newItem
    }

    interface OnItemClickListener {
        fun onCheckedBoxClick(shopItem: ShopItem, isChecked: Boolean)
    }
}