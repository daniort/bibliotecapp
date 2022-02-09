package com.daniort.bookapp.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daniort.bookapp.data.models.BookModel

//*Agregar los campos de Precio y Categoría
// (Ejemplo:Novela, Terror, Educativo) al capturar un libro.


@Entity(tableName = "books_table")
data class BookEntity(
    @PrimaryKey(autoGenerate = true )
    @ColumnInfo(name= "id") val uid:Int = 0,
    @ColumnInfo(name= "title") val title:String,
    @ColumnInfo(name= "auth") val auth:String,
    @ColumnInfo(name= "edit") val edit:String,
    @ColumnInfo(name= "year") val year:String,
    @ColumnInfo(name= "pages") val pages:Int,
    @ColumnInfo(name= "price") val price:Int,
    @ColumnInfo(name= "category") val category:String,
    @ColumnInfo(name= "picture") val picture:String,
)

fun BookEntity.toBookModel(): BookModel {
    return BookModel(uid = uid, title = title, auth=auth,
        category = category, price = price,
        edit=edit, year=year, pages=pages, picture=picture)
}





