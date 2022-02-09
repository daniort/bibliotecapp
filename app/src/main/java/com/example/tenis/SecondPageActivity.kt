package com.daniort.bookapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.daniort.bookapp.data.database.BookDatabase
import com.daniort.bookapp.data.entities.BookEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class   SecondPageActivity : AppCompatActivity() {

    lateinit var room: BookDatabase
    private var fotoTomada = "";
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_page)
        room = Room.databaseBuilder(this, BookDatabase::class.java, "book_database").build()

        val btnAddNew = findViewById<Button>(R.id.btnAddNewBook)
        val btnPhoto = findViewById<ImageButton>(R.id.btnsPhoto)
        btnAddNew.setOnClickListener { processToAddNewBook() }
        btnPhoto.setOnClickListener { getContent.launch("image/*") }
    }

    private fun processToAddNewBook() {
        val titulo = findViewById<EditText>(R.id.etTitleBook)
        val autor = findViewById<EditText>(R.id.etAutorBook)
        val editorial = findViewById<EditText>(R.id.etEditorialBook)
        val ano = findViewById<EditText>(R.id.etYearBook)
        val pages = findViewById<EditText>(R.id.etPagesBook)
        if( fotoTomada.isNotEmpty() ){
            if(titulo.text.isNotEmpty()){
                if(autor.text.isNotEmpty()){
                    if(editorial.text.isNotEmpty()){
                        if(editorial.text.isNotEmpty()){
                            val item = BookEntity(
                                title = titulo.text.toString(),
                                auth = autor.text.toString(),
                                edit = editorial.text.toString(),
                                year = ano.text.toString(),
                                pages = Integer.parseInt(pages.text.toString()),
                                picture = fotoTomada  )
                            saveNewTenis(item)
                            finish()
                        }
                    }
                }
            }
        }else{
            Toast.makeText(this, "Selecciona una Imagen", Toast.LENGTH_LONG).show()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        fotoTomada = uri.toString();
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        val avatar = findViewById<ImageView>(R.id.ivFotoSelect)
        avatar.setImageBitmap(bitmap)
    }

    private fun saveNewTenis(tenis:BookEntity){
        CoroutineScope(Dispatchers.IO).launch {
            room.booksDAO().insertBook( tenis)
        }
    }
}