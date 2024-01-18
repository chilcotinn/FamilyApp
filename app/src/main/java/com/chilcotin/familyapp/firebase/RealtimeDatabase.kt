package com.chilcotin.familyapp.firebase

import com.chilcotin.familyapp.entities.ShareTodoItem
import com.google.firebase.database.DatabaseReference

object RealtimeDatabase {

    fun insertShareTodoItem(item: ShareTodoItem, rootPath: DatabaseReference) {
        rootPath.child(item.title).setValue(
            ShareTodoItem(
                item.title,
                item.description,
                item.time,
                item.checked,
                item.creator,
            )
        )
    }

    fun deleteShareTodoItem(item: ShareTodoItem, rootPath: DatabaseReference) {
        rootPath.child(item.title).removeValue()
    }
}