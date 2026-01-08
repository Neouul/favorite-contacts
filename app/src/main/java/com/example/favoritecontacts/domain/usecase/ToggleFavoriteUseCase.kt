package com.example.favoritecontacts.domain.usecase

import com.example.favoritecontacts.domain.model.Contact
import com.example.favoritecontacts.domain.repository.ContactRepository

class ToggleFavoriteUseCase(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(contact: Contact) {
        repository.toggleFavorite(contact)
    }
}
