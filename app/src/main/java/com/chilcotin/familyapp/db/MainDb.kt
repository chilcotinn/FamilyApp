package com.chilcotin.familyapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chilcotin.familyapp.entity.TodoItem

@Database(
    entities = [TodoItem::class],
    version = 1,
    exportSchema = true,
)
abstract class MainDb : RoomDatabase() {

    abstract fun getDao(): Dao
}