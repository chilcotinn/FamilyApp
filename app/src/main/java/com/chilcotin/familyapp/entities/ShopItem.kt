package com.chilcotin.familyapp.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopItem(
    val title: String = "",
    val checked: Boolean = false,
    val listTitle: String = "",
) : Parcelable
