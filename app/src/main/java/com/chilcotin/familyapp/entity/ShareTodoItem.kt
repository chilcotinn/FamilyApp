package com.chilcotin.familyapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "share_todo_list")
data class ShareTodoItem(

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "time")
    val time: String = "",

    @ColumnInfo(name = "isChecked")
    val isChecked: Boolean = false,

    @ColumnInfo(name = "creator")
    val creator: String = "",

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) : Parcelable