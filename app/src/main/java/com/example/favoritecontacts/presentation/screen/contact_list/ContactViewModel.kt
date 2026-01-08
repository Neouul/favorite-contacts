package com.example.favoritecontacts.presentation.screen.contact_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.favoritecontacts.domain.model.Contact
import com.example.favoritecontacts.domain.usecase.GetContactsUseCase
import com.example.favoritecontacts.domain.usecase.SearchContactsUseCase
import com.example.favoritecontacts.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class ContactViewModel(
    private val getContactsUseCase: GetContactsUseCase,
    private val searchContactsUseCase: SearchContactsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    
    val state: StateFlow<ContactState> = combine(
        _searchQuery,
        _searchQuery
            .debounce(300)
            .onEach { _isLoading.value = true }
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    getContactsUseCase()
                } else {
                    searchContactsUseCase(query)
                }
            }
            .onEach { _isLoading.value = false },
        _isLoading
    ) { query, contacts, isLoading ->
        ContactState(
            contacts = contacts,
            searchQuery = query,
            isLoading = isLoading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ContactState(isLoading = true)
    )

    fun onAction(action: ContactAction) {
        when (action) {
            is ContactAction.OnSearchQueryChange -> {
                _searchQuery.value = action.query
            }
            is ContactAction.OnToggleFavorite -> {
                toggleFavorite(action.contact)
            }
        }
    }

    private fun toggleFavorite(contact: Contact) {
        viewModelScope.launch {
            toggleFavoriteUseCase(contact)
        }
    }
}
