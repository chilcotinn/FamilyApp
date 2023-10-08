package com.chilcotin.familyapp.db

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.chilcotin.familyapp.databinding.TodoItemBinding
import com.chilcotin.familyapp.entity.TodoItem

class TodoAdapter() :
    ListAdapter<TodoItem, TodoAdapter.TodoViewHolder>(ItemComparator()) {

    class TodoViewHolder(private var binding: TodoItemBinding) : ViewHolder(binding.root) {

        fun setData(todoItem: TodoItem) {
            binding.apply {
                tvTitle.text = todoItem.title
                tvTitle.paint.isStrikeThruText = todoItem.isChecked
                tvDescription.text = todoItem.description
                tvTime.text = todoItem.time
                checkBox.isChecked = todoItem.isChecked
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
}