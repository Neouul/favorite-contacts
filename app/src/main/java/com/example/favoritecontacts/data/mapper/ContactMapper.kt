package com.example.favoritecontacts.data.mapper

import com.example.favoritecontacts.data.local.FavoriteContactEntity
import com.example.favoritecontacts.domain.model.Contact

fun FavoriteContactEntity.toDomainModel(): Contact {
    return Contact(
        id = contactId,
        name = name,
        phoneNumber = phoneNumber,
        isFavorite = true
    )
}

fun Contact.toEntity(): FavoriteContactEntity {
    return FavoriteContactEntity(
        contactId = id,
        name = name,
        phoneNumber = phoneNumber
    )
}
