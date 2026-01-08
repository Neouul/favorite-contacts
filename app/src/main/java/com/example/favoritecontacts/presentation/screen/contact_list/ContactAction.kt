package com.example.favoritecontacts.presentation.screen.contact_list

import com.example.favoritecontacts.domain.model.Contact

sealed interface ContactAction {
    data class OnSearchQueryChange(val query: String) : ContactAction
    data class OnToggleFavorite(val contact: Contact) : ContactAction
}
