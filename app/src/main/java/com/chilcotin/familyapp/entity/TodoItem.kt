package com.chilcotin.familyapp.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TodoList")
data class TodoItem(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @NonNull
    @ColumnInfo(name = "title")
    val title: String,

    @NonNull
    @ColumnInfo(name = "description")
    val description: String,

    @NonNull
    @ColumnInfo(name = "isChecked")
    val isChecked: Boolean,
)