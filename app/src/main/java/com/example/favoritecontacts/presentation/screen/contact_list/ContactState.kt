package com.example.favoritecontacts.presentation.screen.contact_list

import com.example.favoritecontacts.domain.model.Contact

data class ContactState(
    val isLoading: Boolean = false,
    val contacts: List<Contact> = emptyList(),
    val searchQuery: String = "",
    val selectedTabIndex: Int = 0,
    val errorMessage: String? = null
)
