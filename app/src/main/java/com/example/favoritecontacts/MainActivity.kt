package com.example.favoritecontacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.favoritecontacts.ui.theme.FavoriteContactsTheme

import androidx.room.Room
import com.example.favoritecontacts.data.local.ContactDatabase
import com.example.favoritecontacts.data.repository.ContactRepositoryImpl
import com.example.favoritecontacts.data.system.ContactSystemDataSource
import com.example.favoritecontacts.domain.usecase.GetContactsUseCase
import com.example.favoritecontacts.domain.usecase.SearchContactsUseCase
import com.example.favoritecontacts.domain.usecase.ToggleFavoriteUseCase
import com.example.favoritecontacts.presentation.screen.contact_list.ContactViewModel
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.favoritecontacts.presentation.screen.contact_list.ContactListRoot
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment

class MainActivity : ComponentActivity() {
    
    private var hasContactPermission by mutableStateOf(false)
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasContactPermission = isGranted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        checkPermission()

        // 의존성 수동 주입
        val database = Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "contact_db"
        ).build()
        
        val systemDataSource = ContactSystemDataSource(applicationContext)
        val repository = ContactRepositoryImpl(systemDataSource, database.contactDao)
        
        val viewModel = ContactViewModel(
            getContactsUseCase = GetContactsUseCase(repository),
            searchContactsUseCase = SearchContactsUseCase(repository),
            toggleFavoriteUseCase = ToggleFavoriteUseCase(repository)
        )

        enableEdgeToEdge()
        setContent {
            FavoriteContactsTheme {
                if (hasContactPermission) {
                    ContactListRoot(viewModel = viewModel)
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Button(onClick = { requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS) }) {
                            Text("연락처 권한 필요")
                        }
                    }
                }
            }
        }
    }
    
    private fun checkPermission() {
        hasContactPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        
        if (!hasContactPermission) {
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FavoriteContactsTheme {
        Greeting("Android")
    }
}