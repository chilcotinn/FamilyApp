package com.chilcotin.familyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.chilcotin.familyapp.db.MainDb
import com.chilcotin.familyapp.entities.ShareTodoItem
import com.chilcotin.familyapp.entities.ShopItem
import com.chilcotin.familyapp.entities.ShopListItem
import com.chilcotin.familyapp.entities.TodoItem
import com.chilcotin.familyapp.firebase.RealtimeDatabase
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainDb: MainDb
) : ViewModel() {

    private val itemEventChannel = Channel<ItemEvent>()
    val itemEvent = itemEventChannel.receiveAsFlow()

    fun getAllTodoItem(): LiveData<List<TodoItem>> = mainDb.getDao().getAllTodoItems().asLiveData()

    fun insertTodoItem(item: TodoItem) = viewModelScope.launch {
        mainDb.getDao().insertTodoItem(item)
    }

    fun updateTodoItem(item: TodoItem) = viewModelScope.launch {
        mainDb.getDao().updateTodoItem(item)
    }

    fun deleteTodoItem(item: TodoItem) = viewModelScope.launch {
        mainDb.getDao().deleteTodoItem(item)
        itemEventChannel.send(ItemEvent.ShowUndoDeleteTodoItemMessage(item))
    }

    fun onTodoItemUndoDeleteClick(item: TodoItem) = viewModelScope.launch {
        mainDb.getDao().insertTodoItem(item)
    }

    fun onTodoItemSelected(item: TodoItem) = viewModelScope.launch {
        itemEventChannel.send(ItemEvent.NavigateToEditTodoItemScreen(item))
    }

    fun onTodoItemCheckedChanged(item: TodoItem, isChecked: Boolean) = viewModelScope.launch {
        mainDb.getDao().updateTodoItem(item.copy(isChecked = isChecked))
    }


    fun getAllShareTodoItem() {}

    fun insertShareTodoItem(item: ShareTodoItem, rootPath: DatabaseReference) =
        viewModelScope.launch {
            RealtimeDatabase.insertShareTodoItem(item, rootPath)
        }

    fun updateShareTodoItem(item: ShareTodoItem, rootPath: DatabaseReference) =
        viewModelScope.launch {
            RealtimeDatabase.insertShareTodoItem(item, rootPath)
        }

    fun deleteShareTodoItem(item: ShareTodoItem, rootPath: DatabaseReference) =
        viewModelScope.launch {
            RealtimeDatabase.deleteShareTodoItem(item, rootPath)
            itemEventChannel.send(ItemEvent.ShowUndoDeleteShareTodoItemMessage(item))
        }

    fun onShareTodoItemUndoDeleteClick(item: ShareTodoItem, rootPath: DatabaseReference) =
        viewModelScope.launch {
            RealtimeDatabase.insertShareTodoItem(item, rootPath)
        }

    fun onShareTodoItemSelected(item: ShareTodoItem) = viewModelScope.launch {
        itemEventChannel.send(ItemEvent.NavigateToEditShareTodoItemScreen(item))
    }

    fun onShareTodoItemCheckedChanged(item: ShareTodoItem, rootPath: DatabaseReference) =
        viewModelScope.launch {
            RealtimeDatabase.insertShareTodoItem(item, rootPath)
        }


    fun getAllShopListItem() {}

    fun insertShopListItem(item: ShopListItem, rootPath: DatabaseReference) =
        viewModelScope.launch {
            RealtimeDatabase.insertShopListItem(item, rootPath)
        }

    fun deleteShopListItem(item: ShopListItem, rootPath: DatabaseReference) =
        viewModelScope.launch {
            RealtimeDatabase.deleteShopListItem(item, rootPath)
        }

    fun onShopListItemSelected(item: ShopListItem) = viewModelScope.launch {
        itemEventChannel.send(ItemEvent.NavigateToShopItemsScreen(item))
    }


    fun getAllShopItem() {}

    fun insertShopItem(item: ShopItem, rootPath: DatabaseReference) = viewModelScope.launch {
        RealtimeDatabase.insertShopItem(item, rootPath)
    }

    fun deleteShopItem(item: ShopItem, rootPath: DatabaseReference) = viewModelScope.launch {
        RealtimeDatabase.deleteShopItem(item, rootPath)
        itemEventChannel.send(ItemEvent.ShowUndoDeleteShopItemMessage(item))
    }

    fun onShopItemCheckedChanged(item: ShopItem, rootPath: DatabaseReference) =
        viewModelScope.launch {
            RealtimeDatabase.insertShopItem(item, rootPath)
        }

    fun onShopItemUndoDeleteClick(item: ShopItem, rootPath: DatabaseReference) =
        viewModelScope.launch {
            RealtimeDatabase.insertShopItem(item, rootPath)
        }


    sealed class ItemEvent {
        data class ShowUndoDeleteTodoItemMessage(val todoItem: TodoItem) : ItemEvent()
        data class NavigateToEditTodoItemScreen(val todoItem: TodoItem) : ItemEvent()
        data class ShowUndoDeleteShareTodoItemMessage(val shareTodoItem: ShareTodoItem) : ItemEvent()
        data class NavigateToEditShareTodoItemScreen(val shareTodoItem: ShareTodoItem) : ItemEvent()
        data class NavigateToShopItemsScreen(val shopListItem: ShopListItem) : ItemEvent()
        data class ShowUndoDeleteShopItemMessage(val shopItem: ShopItem) : ItemEvent()
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