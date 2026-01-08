package com.example.favoritecontacts.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteContactEntity::class], version = 1)
abstract class ContactDatabase : RoomDatabase() {
    abstract val contactDao: ContactDao
}
