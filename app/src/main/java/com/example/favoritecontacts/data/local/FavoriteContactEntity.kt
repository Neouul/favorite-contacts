package com.example.favoritecontacts.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_contacts")
data class FavoriteContactEntity(
    @PrimaryKey val contactId: String,
    val name: String,
    val phoneNumber: String
)
