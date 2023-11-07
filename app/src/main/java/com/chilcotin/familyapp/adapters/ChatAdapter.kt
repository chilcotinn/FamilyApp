package com.chilcotin.familyapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.chilcotin.familyapp.databinding.ChatItemBinding
import com.chilcotin.familyapp.entities.ChatItem
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter : ListAdapter<ChatItem, ChatAdapter.ChatViewHolder>(ItemComparator()) {

    inner class ChatViewHolder(private val binding: ChatItemBinding) : ViewHolder(binding.root) {

        fun setData(chatItem: ChatItem) {
            binding.apply {
                imAvatar.setImageURI(FirebaseAuth.getInstance().currentUser?.photoUrl)
                tvCreator.text = chatItem.creator
                tvTime.text = chatItem.time
                tvMessage.text = chatItem.message
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ChatViewHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ChatViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.setData(currentItem)
    }

    class ItemComparator : DiffUtil.ItemCallback<ChatItem>() {
        override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean =
            oldItem == newItem
    }
}