package com.chilcotin.familyapp.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chilcotin.familyapp.entity.TodoItem

class SharedViewModel : ViewModel() {
    private val _todoItem: MutableLiveData<TodoItem> by lazy {
        MutableLiveData<TodoItem>()
    }
    val todoItem: LiveData<TodoItem> = _todoItem

    fun setTodoItem(todoItem: TodoItem) {
        _todoItem.value = todoItem
    }
}