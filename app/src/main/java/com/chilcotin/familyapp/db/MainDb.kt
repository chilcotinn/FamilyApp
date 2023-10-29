package com.chilcotin.familyapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chilcotin.familyapp.entities.ShareTodoItem
import com.chilcotin.familyapp.entities.ShopItem
import com.chilcotin.familyapp.entities.ShopListItem
import com.chilcotin.familyapp.entities.TodoItem

@Database(
    entities = [
        TodoItem::class,
        ShareTodoItem::class,
        ShopListItem::class,
        ShopItem::class
    ],
    version = 1,
    exportSchema = true,
)
abstract class MainDb : RoomDatabase() {

    abstract fun getDao(): Dao
}