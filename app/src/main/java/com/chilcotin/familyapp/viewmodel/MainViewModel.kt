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


    fun getAllShareTodoItem(): LiveData<List<ShareTodoItem>> =
        mainDb.getDao().getAllShareTodoItems().asLiveData()

    fun insertShareTodoItem(item: ShareTodoItem) = viewModelScope.launch {
        mainDb.getDao().insertShareTodoItem(item)
    }

    fun updateShareTodoItem(item: ShareTodoItem) = viewModelScope.launch {
        mainDb.getDao().updateShareTodoItem(item)
    }

    fun deleteShareTodoItem(item: ShareTodoItem) = viewModelScope.launch {
        mainDb.getDao().deleteShareTodoItem(item)
        itemEventChannel.send(ItemEvent.ShowUndoDeleteShareTodoItemMessage(item))
    }

    fun onShareTodoItemUndoDeleteClick(item: ShareTodoItem) = viewModelScope.launch {
        mainDb.getDao().insertShareTodoItem(item)
    }

    fun onShareTodoItemSelected(item: ShareTodoItem) = viewModelScope.launch {
        itemEventChannel.send(ItemEvent.NavigateToEditShareTodoItemScreen(item))
    }

    fun onShareTodoItemCheckedChanged(item: ShareTodoItem, isChecked: Boolean) =
        viewModelScope.launch {
            mainDb.getDao().updateShareTodoItem(item.copy(isChecked = isChecked))
        }


    fun getAllShopListItem(): LiveData<List<ShopListItem>> =
        mainDb.getDao().getAllShopListItem().asLiveData()

    fun insertShopListItem(item: ShopListItem) = viewModelScope.launch {
        mainDb.getDao().insertShopListItem(item)
    }

    fun deleteShopListItem(item: ShopListItem) = viewModelScope.launch {
        mainDb.getDao().deleteShopListItem(item)
    }

    fun onShopListItemSelected(item: ShopListItem) = viewModelScope.launch {
        itemEventChannel.send(ItemEvent.NavigateToShopItemsScreen(item))
    }


    fun getAllShopItem(listId: Int): LiveData<List<ShopItem>> =
        mainDb.getDao().getAllShopItem(listId).asLiveData()

    fun insertShopItem(item: ShopItem) = viewModelScope.launch {
        mainDb.getDao().insertShopItem(item)
    }

    fun deleteShopItem(item: ShopItem) = viewModelScope.launch {
        mainDb.getDao().deleteShopItem(item)
    }

    fun onShopItemCheckedChanged(item: ShopItem, isChecked: Boolean) =
        viewModelScope.launch {
            mainDb.getDao().updateShopItem(item.copy(isChecked = isChecked))
        }


    sealed class ItemEvent {
        data class ShowUndoDeleteTodoItemMessage(val todoItem: TodoItem) : ItemEvent()
        data class NavigateToEditTodoItemScreen(val todoItem: TodoItem) : ItemEvent()
        data class ShowUndoDeleteShareTodoItemMessage(val shareTodoItem: ShareTodoItem) : ItemEvent()
        data class NavigateToEditShareTodoItemScreen(val shareTodoItem: ShareTodoItem) : ItemEvent()
        data class NavigateToShopItemsScreen(val shopListItem: ShopListItem) : ItemEvent()
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