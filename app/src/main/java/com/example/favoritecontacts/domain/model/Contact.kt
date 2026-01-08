package com.example.favoritecontacts.domain.model

data class Contact(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val isFavorite: Boolean = false,
    val initials: String = "" // 초성 검색을 위한 필드
)
