package com.chilcotin.familyapp.adapters

import com.chilcotin.familyapp.entity.ShopListItem

class ShopListAdapter(private val listener: OnItemClickListener) {

    interface OnItemClickListener {
        fun onItemClick(shopListItem: ShopListItem)
    }
}