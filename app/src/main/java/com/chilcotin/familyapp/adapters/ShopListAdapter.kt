package com.chilcotin.familyapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.ShopListItemBinding
import com.chilcotin.familyapp.entities.ShopListItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ShopListAdapter(private val listener: OnItemClickListener) :
    ListAdapter<ShopListItem, ShopListAdapter.ShopListViewHolder>(ItemComparator()) {

    inner class ShopListViewHolder(private val binding: ShopListItemBinding) :
        ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val shopListItem = getItem(position)
                        listener.onItemClick(shopListItem)
                    }
                }
            }
        }

        fun setData(shopListItem: ShopListItem) {
            binding.apply {
                tvTitle.text = shopListItem.title
                tvDescription.text = shopListItem.description
                tvTime.text = shopListItem.time
                tvCreator.text = shopListItem.creator

                if (tvDescription.text.isEmpty()) {
                    tvDescription.visibility = View.GONE
                }

                itemView.setOnClickListener {
                    listener.onItemClick(shopListItem)
                }
                ibDelete.setOnClickListener {
                    listener.deleteItem(
                        shopListItem,
                        Firebase.database.getReference(itemView.context.getString(R.string.root_path_shop_list))
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        val binding = ShopListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.setData(currentItem)
    }

    class ItemComparator : DiffUtil.ItemCallback<ShopListItem>() {
        override fun areItemsTheSame(oldItem: ShopListItem, newItem: ShopListItem): Boolean =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: ShopListItem, newItem: ShopListItem): Boolean =
            oldItem == newItem
    }

    interface OnItemClickListener {
        fun onItemClick(shopListItem: ShopListItem)
        fun deleteItem(shopListItem: ShopListItem, rootPath: DatabaseReference)
    }
}