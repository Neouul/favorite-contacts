package com.example.favoritecontacts.domain.usecase

import com.example.favoritecontacts.domain.model.Contact
import com.example.favoritecontacts.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow

class SearchContactsUseCase(
    private val repository: ContactRepository
) {
    operator fun invoke(query: String): Flow<List<Contact>> = repository.searchContacts(query)
}
