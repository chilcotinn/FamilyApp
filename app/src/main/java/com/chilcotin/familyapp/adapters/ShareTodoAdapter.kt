package com.chilcotin.familyapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.chilcotin.familyapp.databinding.ShareTodoItemBinding
import com.chilcotin.familyapp.entity.ShareTodoItem

class ShareTodoAdapter(private val listener: OnItemClickListener) :
    ListAdapter<ShareTodoItem, ShareTodoAdapter.ShareTodoViewHolder>(ItemComparator()) {

    inner class ShareTodoViewHolder(private val binding: ShareTodoItemBinding) :
        ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val shareTodoItem = getItem(position)
                        listener.onItemClick(shareTodoItem)
                    }
                }
                checkBox.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val shareTodoItem = getItem(position)
                        listener.onCheckedBoxClick(shareTodoItem, checkBox.isChecked)
                    }
                }
            }
        }

        fun setData(shareTodoItem: ShareTodoItem) {
            binding.apply {
                tvTitle.text = shareTodoItem.title
                tvTitle.paint.isStrikeThruText = shareTodoItem.isChecked
                tvDescription.text = shareTodoItem.description
                tvTime.text = shareTodoItem.time
                checkBox.isChecked = shareTodoItem.isChecked
                tvCreator.text = shareTodoItem.creator
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareTodoViewHolder {
        val binding = ShareTodoItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ShareTodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShareTodoViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.setData(currentItem)
    }

    class ItemComparator : DiffUtil.ItemCallback<ShareTodoItem>() {
        override fun areItemsTheSame(oldItem: ShareTodoItem, newItem: ShareTodoItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ShareTodoItem, newItem: ShareTodoItem): Boolean =
            oldItem == newItem
    }

    interface OnItemClickListener {
        fun onItemClick(shareTodoItem: ShareTodoItem)
        fun onCheckedBoxClick(shareTodoItem: ShareTodoItem, isChecked: Boolean)
    }
}