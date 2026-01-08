package com.example.favoritecontacts.domain.repository

import com.example.favoritecontacts.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getContacts(): Flow<List<Contact>>
    suspend fun toggleFavorite(contact: Contact)
    fun searchContacts(query: String): Flow<List<Contact>>
}
