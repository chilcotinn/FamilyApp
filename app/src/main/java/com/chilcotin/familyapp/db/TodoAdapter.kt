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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            TodoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class TodoViewHolder(private var binding: TodoItemBinding) : ViewHolder(binding.root) {

        fun setData(todoItem: TodoItem) {
            binding.tvTitle.text = todoItem.title
            binding.tvDescription.text = todoItem.description
            binding.tvTime.text = todoItem.time
            binding.checkBox.isChecked = todoItem.isChecked
        }
    }

    class ItemComparator() : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem == newItem
        }
    }
}