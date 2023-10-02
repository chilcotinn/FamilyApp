package com.chilcotin.familyapp.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [],
    version = 1,
    exportSchema = true,
)
abstract class MainDb : RoomDatabase() {
    abstract val dao: Dao
}