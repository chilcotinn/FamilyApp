package com.chilcotin.familyapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.ShareTodoItemBinding
import com.chilcotin.familyapp.entities.ShareTodoItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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
                        val oldShareTodoItem = getItem(position)
                        val newShareTodoItem =
                            oldShareTodoItem.copy(checked = !oldShareTodoItem.checked)
                        listener.onCheckedBoxClick(
                            newShareTodoItem,
                            Firebase
                                .database
                                .getReference(itemView.context.getString(R.string.root_path_share_todo))
                        )
                    }
                }
            }
        }

        fun setData(shareTodoItem: ShareTodoItem) {
            binding.apply {
                tvTitle.text = shareTodoItem.title
                tvTitle.paint.isStrikeThruText = shareTodoItem.checked
                tvDescription.text = shareTodoItem.description
                tvTime.text = shareTodoItem.time
                checkBox.isChecked = shareTodoItem.checked
                tvCreator.text = shareTodoItem.creator

                if (tvDescription.text.isEmpty()) {
                    tvDescription.visibility = View.GONE
                } else {
                    tvDescription.visibility = View.VISIBLE
                }
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
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: ShareTodoItem, newItem: ShareTodoItem): Boolean =
            oldItem == newItem
    }

    interface OnItemClickListener {
        fun onItemClick(shareTodoItem: ShareTodoItem)
        fun onCheckedBoxClick(shareTodoItem: ShareTodoItem, rootPath: DatabaseReference)
    }
}