package com.chilcotin.familyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chilcotin.familyapp.db.MainDb
import com.chilcotin.familyapp.entity.TodoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainDb: MainDb
) : ViewModel() {

    fun insertTodoItem(item: TodoItem) = viewModelScope.launch {
        mainDb.dao.insertTodoItem(item)
    }

    fun deleteTodoItem(item: TodoItem) = viewModelScope.launch {
        mainDb.dao.deleteTodoItem(item)
    }

    fun getAllTodoItem(): Flow<List<TodoItem>> = mainDb.dao.getAllTodoItems()


    class MainViewModelFactory(private val mainDb: MainDb) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(mainDb) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}