package com.chilcotin.familyapp.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.TodoItemBinding
import com.chilcotin.familyapp.entity.TodoItem

class TodoAdapter() :
    ListAdapter<TodoItem, TodoAdapter.TodoViewHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = TodoItemBinding.bind(view)

        fun setData(todoItem: TodoItem) =
            with(binding) {
                tvTitle.text = todoItem.title
                tvDescription.text = todoItem.description
                tvTime.text = todoItem.time
                checkBox.isChecked = todoItem.isChecked
            }

        companion object {
            fun create(parent: ViewGroup): TodoViewHolder {
                return TodoViewHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.todo_item, parent, false)
                )
            }
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