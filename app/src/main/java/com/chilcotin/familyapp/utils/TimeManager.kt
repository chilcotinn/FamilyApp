package com.chilcotin.familyapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimeManager {
    private const val DEF_TIME_FORMAT = "HH:mm - dd/MM"

    fun getTime(): String {
        val timeFormatter = SimpleDateFormat(DEF_TIME_FORMAT, Locale.getDefault())
        val c = Calendar.getInstance().time
        return timeFormatter.format(c.time)
    }
}