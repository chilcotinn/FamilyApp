package com.chilcotin.familyapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object InputManager {
    fun showSoftKeyboard(view: View, context: Context) {
        if (view.requestFocus()) {
            val imm = context.getSystemService(InputMethodManager::class.java)
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}