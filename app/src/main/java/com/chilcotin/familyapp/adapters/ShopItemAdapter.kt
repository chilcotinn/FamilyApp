package com.chilcotin.familyapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.ShopItemBinding
import com.chilcotin.familyapp.entities.ShopItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ShopItemAdapter(private val listener: OnItemClickListener) :
    ListAdapter<ShopItem, ShopItemAdapter.ShopItemViewHolder>(ItemComparator()) {

    inner class ShopItemViewHolder(private val binding: ShopItemBinding) :
        ViewHolder(binding.root) {

        init {
            binding.apply {
                checkBox.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val oldShopItem = getItem(position)
                        val newShopItem = oldShopItem.copy(checked = !oldShopItem.checked)
                        listener.onCheckedBoxClick(
                            newShopItem,
                            Firebase.database.getReference(itemView.context.getString(R.string.root_path_shop_list))
                                .child(newShopItem.listTitle)
                        )
                    }
                }
            }
        }

        fun setData(shopItem: ShopItem) {
            binding.apply {
                tvTitle.text = shopItem.title
                tvTitle.paint.isStrikeThruText = shopItem.checked
                checkBox.isChecked = shopItem.checked
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
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean =
            oldItem == newItem
    }

    interface OnItemClickListener {
        fun onCheckedBoxClick(shopItem: ShopItem, rootPath: DatabaseReference)
    }
}