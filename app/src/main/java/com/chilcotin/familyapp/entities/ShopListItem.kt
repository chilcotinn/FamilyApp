package com.chilcotin.familyapp.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopListItem(
    val title: String = "",
    val description: String = "",
    val time: String = "",
    val creator: String = "",
) : Parcelable
