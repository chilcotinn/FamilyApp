package com.chilcotin.familyapp.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareTodoItem(

    val title: String = "",

    val description: String = "",

    val time: String = "",

    val checked: Boolean = false,

    val creator: String = "",
) : Parcelable