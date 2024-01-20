package com.chilcotin.familyapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chilcotin.familyapp.entities.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM todo_list")
    fun getAllTodoItems(): Flow<List<TodoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoItem(item: TodoItem)

    @Update
    suspend fun updateTodoItem(item: TodoItem)

    @Delete
    suspend fun deleteTodoItem(item: TodoItem)
}