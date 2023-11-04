package com.chilcotin.familyapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.chilcotin.familyapp.databinding.TodoItemBinding
import com.chilcotin.familyapp.entities.TodoItem

class TodoAdapter(private val listener: OnItemClickListener) :
    ListAdapter<TodoItem, TodoAdapter.TodoViewHolder>(ItemComparator()) {

    inner class TodoViewHolder(private var binding: TodoItemBinding) : ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val todoItem = getItem(position)
                        listener.onItemClick(todoItem)
                    }
                }
                checkBox.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val todoItem = getItem(position)
                        listener.onCheckBoxClick(todoItem, checkBox.isChecked)
                    }
                }
            }
        }

        fun setData(todoItem: TodoItem) {
            binding.apply {
                tvTitle.text = todoItem.title
                tvTitle.paint.isStrikeThruText = todoItem.isChecked
                tvDescription.text = todoItem.description
                tvTime.text = todoItem.time
                checkBox.isChecked = todoItem.isChecked

                if (tvDescription.text.isEmpty()) {
                    tvDescription.visibility = View.GONE
                } else {
                    tvDescription.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.setData(currentItem)
    }

    class ItemComparator : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
            oldItem == newItem
    }

    interface OnItemClickListener {
        fun onItemClick(todoItem: TodoItem)
        fun onCheckBoxClick(todoItem: TodoItem, isChecked: Boolean)
    }
}