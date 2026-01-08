package com.example.favoritecontacts.data.system

import android.content.Context
import android.provider.ContactsContract
import com.example.favoritecontacts.domain.model.Contact

class ContactSystemDataSource(private val context: Context) {

    fun fetchSystemContacts(): List<Contact> {
        val contactList = mutableListOf<Contact>()
        
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " ASC"
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            if (idIndex == -1 || nameIndex == -1 || numberIndex == -1) return emptyList()

            while (it.moveToNext()) {
                val id = it.getString(idIndex)
                val name = it.getString(nameIndex) ?: ""
                val number = it.getString(numberIndex) ?: ""
                
                // 중복 방지
                if (contactList.none { c -> c.id == id }) {
                    contactList.add(
                        Contact(
                            id = id,
                            name = name,
                            phoneNumber = number,
                            initials = getSearchInitials(name)
                        )
                    )
                }
            }
        }
        return contactList
    }

    private fun getSearchInitials(text: String): String {
        val CHOSUNG = listOf(
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 
            'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
        )
        
        val initials = StringBuilder()
        var lastCharWasSpace = true

        text.forEach { char ->
            if (char in '\uAC00'..'\uD7A3') {
                val code = char.toInt() - 0xAC00
                val chosungIndex = code / (21 * 28)
                initials.append(CHOSUNG[chosungIndex])
                lastCharWasSpace = false
            } else if (char.isLetter()) {
                if (lastCharWasSpace) {
                    initials.append(char)
                }
                lastCharWasSpace = false
            } else if (char.isWhitespace()) {
                lastCharWasSpace = true
            } else {
                initials.append(char)
                lastCharWasSpace = false
            }
        }
        return initials.toString()
    }
}
