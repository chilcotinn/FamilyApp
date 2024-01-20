package com.chilcotin.familyapp.firebase

import com.chilcotin.familyapp.entities.ShareTodoItem
import com.chilcotin.familyapp.entities.ShopItem
import com.chilcotin.familyapp.entities.ShopListItem
import com.google.firebase.database.DatabaseReference

object RealtimeDatabase {

    fun insertShareTodoItem(item: ShareTodoItem, rootPath: DatabaseReference) {
        rootPath.child(item.title).setValue(
            ShareTodoItem(
                item.title,
                item.description,
                item.time,
                item.checked,
                item.creator
            )
        )
    }

    fun deleteShareTodoItem(item: ShareTodoItem, rootPath: DatabaseReference) {
        rootPath.child(item.title).removeValue()
    }

    fun insertShopListItem(item: ShopListItem, rootPath: DatabaseReference) {
        rootPath.child(item.title).setValue(
            ShopListItem(
                item.title,
                item.description,
                item.time,
                item.creator
            )
        )
    }

    fun deleteShopListItem(item: ShopListItem, rootPath: DatabaseReference) {
        rootPath.child(item.title).removeValue()
    }

    fun insertShopItem(item: ShopItem, rootPath: DatabaseReference) {
        rootPath.child(item.listTitle).child(item.title).setValue(
            ShopItem(
                item.title,
                item.checked,
                item.listTitle
            )
        )
    }

    fun deleteShopItem(item: ShopItem, rootPath: DatabaseReference) {
        rootPath.child(item.listTitle).child(item.title).removeValue()
    }
}