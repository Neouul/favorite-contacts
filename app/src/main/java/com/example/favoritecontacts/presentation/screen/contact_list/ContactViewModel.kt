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

    private val _state = MutableStateFlow(ContactState(isLoading = true))
    val state: StateFlow<ContactState> = _state.asStateFlow()

    init {
        val queryFlow = _state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(300)

        val tabIndexFlow = _state
            .map { it.selectedTabIndex }
            .distinctUntilChanged()

        combine(queryFlow, tabIndexFlow) { query, tabIndex ->
            query to tabIndex
        }
            .onEach { _state.update { it.copy(isLoading = true) } }
            .flatMapLatest { (query, tabIndex) ->
                val contactsFlow = if (query.isBlank()) {
                    getContactsUseCase()
                } else {
                    searchContactsUseCase(query)
                }
                
                contactsFlow.map { contacts ->
                    if (tabIndex == 1) {
                        contacts.filter { it.isFavorite }
                    } else {
                        contacts
                    }
                }
            }
            .onEach { contacts ->
                _state.update { it.copy(contacts = contacts, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ContactAction) {
        when (action) {
            is ContactAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchQuery = action.query) }
            }
            is ContactAction.OnToggleFavorite -> {
                toggleFavorite(action.contact)
            }
            is ContactAction.OnTabClick -> {
                _state.update { it.copy(selectedTabIndex = action.index) }
            }
        }
    }

    private fun toggleFavorite(contact: Contact) {
        viewModelScope.launch {
            toggleFavoriteUseCase(contact)
        }
    }
}
