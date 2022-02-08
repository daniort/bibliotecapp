package com.daniort.bookapp.data.models

import android.os.Bundle
import com.daniort.bookapp.data.entities.BookEntity

data class BookModel (
    val uid:Int = 0,
    val title:String,
    val auth:String,
    val edit:String,
    val year:String,
    val picture:String
)

fun BookModel.toBundle(): Bundle {
    var bolsa: Bundle = Bundle()
    bolsa.putInt("uidKey", uid )
    bolsa.putString("titleKey", title )
    bolsa.putString("authKey", auth )
    bolsa.putString("editKey", edit )
    bolsa.putString("yearKey", year )
    bolsa.putString("pictureKey", picture )
    return bolsa
}

fun BookModel.toBookEntity(): BookEntity {
    return BookEntity(
        uid = uid, title= title, auth = auth, edit= edit, year= year, picture=picture
    )
}
