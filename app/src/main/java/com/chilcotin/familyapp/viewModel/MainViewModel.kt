package com.chilcotin.familyapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.chilcotin.familyapp.db.MainDb
import com.chilcotin.familyapp.entity.TodoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainDb: MainDb
) : ViewModel() {

    fun insertTodoItem(item: TodoItem) = viewModelScope.launch {
        mainDb.getDao().insertTodoItem(item)
    }

    fun updateTodoItem(item: TodoItem) = viewModelScope.launch {
        mainDb.getDao().updateTodoItem(item)
    }

    fun deleteTodoItem(item: TodoItem) = viewModelScope.launch {
        mainDb.getDao().deleteTodoItem(item)
    }

    fun getAllTodoItem(): LiveData<List<TodoItem>> = mainDb.getDao().getAllTodoItems().asLiveData()

    fun onItemSelected(todoItem: TodoItem) {}

    fun onItemCheckedChanged(todoItem: TodoItem, isChecked: Boolean) = viewModelScope.launch {
        mainDb.getDao().updateTodoItem(todoItem.copy(isChecked = isChecked))
    }

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