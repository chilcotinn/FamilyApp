package com.chilcotin.familyapp.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "shop_list_item")
data class ShopListItem(

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "time")
    val time: String = "",

    @ColumnInfo(name = "creator")
    val creator: String = "",

    @ColumnInfo(name = "allItemsCounter")
    val allItemsCounter: Int = 0,

    @ColumnInfo(name = "checkedItemsCounter")
    val checkedItemsCounter: Int = 0,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) : Parcelable
