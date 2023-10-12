package com.chilcotin.familyapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chilcotin.familyapp.entity.ShareTodoItem
import com.chilcotin.familyapp.entity.TodoItem
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


    @Query("SELECT * FROM share_todo_list")
    fun getAllShareTodoItems(): Flow<List<ShareTodoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShareTodoItem(item: ShareTodoItem)

    @Update
    suspend fun updateShareTodoItem(item: ShareTodoItem)

    @Delete
    suspend fun deleteShareTodoItem(item: ShareTodoItem)
}