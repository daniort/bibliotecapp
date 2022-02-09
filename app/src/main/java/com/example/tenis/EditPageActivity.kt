package com.daniort.bookapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.daniort.bookapp.data.database.BookDatabase
import com.daniort.bookapp.data.models.BookModel
import com.daniort.bookapp.data.models.toBookEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPageActivity : AppCompatActivity() {

        lateinit var room: BookDatabase
        private var fotoTomada = "";

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_edit_page)
            room = Room.databaseBuilder(this, BookDatabase::class.java, "book_database").build()
            val myBundle:Bundle = intent.extras as Bundle
            val bookParametro = convertBundleToEntity(myBundle)
            rellenarInputs(bookParametro)
            val btnDelete = findViewById<Button>(R.id.btnDelete)
            val btnUpdate = findViewById<Button>(R.id.btnUpdate)
            val btnPhoto = findViewById<ImageButton>(R.id.btnsPhoto)
            btnPhoto.setOnClickListener { getContent.launch("image/*") }
            btnDelete.setOnClickListener { showAlert(bookParametro) }
            btnUpdate.setOnClickListener { processToUpdateBook() }
        }

        private fun processToUpdateBook() {
            val titulo = findViewById<EditText>(R.id.etTitleBookEdit)
            val autor = findViewById<EditText>(R.id.etAutorBookEdit)
            val editorial = findViewById<EditText>(R.id.etEditorialBookEdit)
            val ano = findViewById<EditText>(R.id.etYearBookEdit)
            val pages = findViewById<EditText>(R.id.etPagesBookEdit)
            val precio = findViewById<EditText>(R.id.etPriceBookEdit)
            val categoria = findViewById<EditText>(R.id.etCategoryBookEdit)
            if( fotoTomada.isNotEmpty() ){
                if(titulo.text.isNotEmpty()){
                    if(autor.text.isNotEmpty()){
                        if(editorial.text.isNotEmpty()){
                            if(ano.text.isNotEmpty() && pages.text.isNotEmpty() && precio.text.isNotEmpty() && categoria.text.isNotEmpty() ){

                                val item = BookModel(
                                    title = titulo.text.toString(),
                                    auth = autor.text.toString(),
                                    edit = editorial.text.toString(),
                                    year = ano.text.toString(),
                                    category = categoria.text.toString(),
                                    pages = Integer.parseInt(pages.text.toString()),
                                    price = Integer.parseInt(precio.text.toString()),
                                    picture = fotoTomada  )
                                updateBook(item)
                                finish()

                            }
                        }
                    }
                }
            }else{
                Toast.makeText(this, "Selecciona una Imagen", Toast.LENGTH_LONG).show()
            }
    }

        private fun rellenarInputs(book: BookModel) {
            val titulo = findViewById<EditText>(R.id.etTitleBookEdit)
            val autor = findViewById<EditText>(R.id.etAutorBookEdit)
            val editorial = findViewById<EditText>(R.id.etEditorialBookEdit)
            val ano = findViewById<EditText>(R.id.etYearBookEdit)
            val paginas = findViewById<EditText>(R.id.etPagesBookEdit)
            val precio = findViewById<EditText>(R.id.etPriceBookEdit)
            val categoria = findViewById<EditText>(R.id.etCategoryBookEdit)
            val avatar = findViewById<ImageView>(R.id.ivFotoSelect)

            titulo.setText(book.title)
            autor.setText(book.auth)
            editorial.setText(book.edit)
            ano.setText(book.year)
            paginas.setText(book.pages.toString())
            precio.setText(book.price.toString())
            categoria.setText(book.category)

            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.parse(book.picture))
                avatar.setImageBitmap(bitmap)
            } catch (e: Exception) { }
        }

        private fun convertBundleToEntity(bun: Bundle): BookModel {
            return BookModel(
                uid = bun.getInt("uidKey"),
                title = bun.getString("titleKey")!!,
                auth = bun.getString("authKey")!!,
                edit = bun.getString("editKey")!!,
                year = bun.getString("yearKey")!!,
                pages = bun.getInt("pagesKey")!!,
                picture = bun.getString("pictureKey")!!,
                        price = bun.getInt("priceKey"),
                category = bun.getString("categoryKey")!!,

            )
        }

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                fotoTomada = uri.toString();
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                val avatar = findViewById<ImageView>(R.id.ivFotoSelect)
                avatar.setImageBitmap(bitmap)
        }

        fun showAlert(book: BookModel){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Alert")
            builder.setMessage("¿Está seguro que desea borrar el libro?")
            builder.setPositiveButton("Aceptar"){ dialog, which -> deleteBook(book) }
            builder.setNegativeButton("Cancelar"){ dialog, which -> }
            builder.show()
        }

        private fun deleteBook(book: BookModel){
            CoroutineScope(Dispatchers.IO).launch {
                room.booksDAO().deleteBook(book.toBookEntity())
            }
            finish()
        }
        private fun updateBook(book: BookModel){
            CoroutineScope(Dispatchers.IO).launch {
                room.booksDAO().updateBook(book.toBookEntity())
            }
        }

    }
