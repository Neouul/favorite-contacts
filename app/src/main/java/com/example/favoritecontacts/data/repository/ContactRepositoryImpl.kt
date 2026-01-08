package com.example.favoritecontacts.data.repository

import com.example.favoritecontacts.data.local.ContactDao
import com.example.favoritecontacts.data.mapper.toEntity
import com.example.favoritecontacts.data.system.ContactSystemDataSource
import com.example.favoritecontacts.domain.model.Contact
import com.example.favoritecontacts.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ContactRepositoryImpl(
    private val systemDataSource: ContactSystemDataSource,
    private val contactDao: ContactDao
) : ContactRepository {

    override fun getContacts(): Flow<List<Contact>> {
        val systemContacts = flow { emit(systemDataSource.fetchSystemContacts()) }
        val favoriteContacts = contactDao.getAllFavorites()

        return combine(systemContacts, favoriteContacts) { system, favorites ->
            val favoriteIds = favorites.map { it.contactId }.toSet()
            system.map { contact ->
                contact.copy(isFavorite = contact.id in favoriteIds)
            }
        }
    }

    override suspend fun toggleFavorite(contact: Contact) {
        if (contact.isFavorite) {
            contactDao.deleteFavorite(contact.toEntity())
        } else {
            contactDao.insertFavorite(contact.toEntity())
        }
    }

    override fun searchContacts(query: String): Flow<List<Contact>> {
        return getContacts().map { contacts ->
            contacts.filter { contact ->
                contact.name.contains(query, ignoreCase = true) || 
                contact.initials.contains(query, ignoreCase = true)
            }
        }
    }
}
