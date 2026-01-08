package com.example.favoritecontacts.data.system

import android.content.Context
import android.provider.ContactsContract
import com.example.favoritecontacts.domain.model.Contact

class ContactSystemDataSource(private val context: Context) {

    fun fetchSystemContacts(): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val id = it.getString(idIndex)
                val name = it.getString(nameIndex) ?: ""
                val number = it.getString(numberIndex) ?: ""
                
                // 중복 방지 (여러 번호가 등록된 경우 첫 번째 번호만)
                if (contactList.none { c -> c.id == id }) {
                    contactList.add(
                        Contact(
                            id = id,
                            name = name,
                            phoneNumber = number,
                            initials = getKoreanInitials(name)
                        )
                    )
                }
            }
        }
        return contactList
    }

    private fun getKoreanInitials(text: String): String {
        val CHOSUNG = listOf(
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 
            'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
        )
        
        return text.map { char ->
            if (char in '\uAC00'..'\uD7A3') {
                val code = char.toInt() - 0xAC00
                val chosungIndex = code / (21 * 28)
                CHOSUNG[chosungIndex]
            } else {
                char
            }
        }.joinToString("")
    }
}
