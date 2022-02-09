package com.daniort.bookapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.daniort.bookapp.data.entities.BookEntity


@Database(entities = [BookEntity::class], version = 2)
abstract class BookDatabase:RoomDatabase() {

    abstract fun booksDAO(): BooksDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: BookDatabase? = null
        fun getDatabase(context: Context): BookDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, BookDatabase::class.java,
                    "book_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
        fun destroyDataBase() {
            INSTANCE = null
        }
    }

}