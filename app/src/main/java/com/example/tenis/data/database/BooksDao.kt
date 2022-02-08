package com.daniort.bookapp.data.database

import androidx.room.*
import com.daniort.bookapp.data.entities.BookEntity


@Dao
interface BooksDao {

    @Query("SELECT * FROM books_table")
    fun getAllBooks():List<BookEntity>

    @Query("SELECT * FROM books_table WHERE title LIKE '%' || :query || '%'")
    fun getAllBooksByTitle(query:String):List<BookEntity>

    @Insert
    fun insertBook(book:BookEntity)

    @Update
    fun updateBook(book:BookEntity)

    @Delete
    fun deleteBook(book:BookEntity)

}