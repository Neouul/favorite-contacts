package com.example.favoritecontacts.domain.usecase

import com.example.favoritecontacts.domain.model.Contact
import com.example.favoritecontacts.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow

class GetContactsUseCase(
    private val repository: ContactRepository
) {
    operator fun invoke(): Flow<List<Contact>> = repository.getContacts()
}
