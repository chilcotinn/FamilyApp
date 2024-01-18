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
    val title: String = "",

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "time")
    val time: String = "",

    @ColumnInfo(name = "creator")
    val creator: String = "",

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) : Parcelable
