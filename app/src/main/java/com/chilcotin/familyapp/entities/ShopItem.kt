package com.chilcotin.familyapp.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "shop_list")
data class ShopItem(

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "isChecked")
    val isChecked: Boolean = false,

    @ColumnInfo(name = "listId")
    val listId: Int,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) : Parcelable
