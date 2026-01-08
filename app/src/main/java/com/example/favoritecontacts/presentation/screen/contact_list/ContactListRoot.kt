package com.example.favoritecontacts.presentation.screen.contact_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ContactListRoot(
    viewModel: ContactViewModel
) {
    val state by viewModel.state.collectAsState()
    
    ContactListScreen(
        state = state,
        onAction = viewModel::onAction
    )
}
