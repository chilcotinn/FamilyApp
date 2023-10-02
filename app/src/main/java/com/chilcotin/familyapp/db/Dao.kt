package com.chilcotin.familyapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chilcotin.familyapp.entity.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoItem(item: TodoItem)

    @Delete
    suspend fun deleteTodoItem(item: TodoItem)

    @Query("SELECT * FROM TodoList")
    fun getAllTodoItems(): Flow<List<TodoItem>>
}