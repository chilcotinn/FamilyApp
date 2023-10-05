package com.chilcotin.familyapp.db

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.entity.TodoItem

class TodoAdapter() :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var dataSetTodo = emptyList<TodoItem>()

    class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView
        val description: TextView
        val time: TextView
        val checkBox: CheckBox

        init {
            title = view.findViewById(R.id.tvTitle)
            description = view.findViewById(R.id.tvDescription)
            time = view.findViewById(R.id.tvTime)
            checkBox = view.findViewById(R.id.checkBox)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.title.text = dataSetTodo[position].title
        holder.description.text = dataSetTodo[position].description
        holder.time.text = dataSetTodo[position].time
        holder.checkBox.isChecked = dataSetTodo[position].isChecked
    }

    override fun getItemCount() = dataSetTodo.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<TodoItem>) {
        dataSetTodo = list
        notifyDataSetChanged()
    }

    fun getTodoItem(position: Int): TodoItem {
        return dataSetTodo[position]
    }
}