package com.example.favoritecontacts.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM favorite_contacts")
    fun getAllFavorites(): Flow<List<FavoriteContactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(contact: FavoriteContactEntity)

    @Delete
    suspend fun deleteFavorite(contact: FavoriteContactEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_contacts WHERE contactId = :contactId)")
    fun isFavorite(contactId: String): Flow<Boolean>
}
