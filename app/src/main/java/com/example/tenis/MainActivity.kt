package com.daniort.bookapp


import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.daniort.bookapp.data.database.BookDatabase
import com.daniort.bookapp.data.entities.BookEntity
import com.daniort.bookapp.data.ui.BookAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    lateinit var room: BookDatabase
    lateinit var librosRecyclerView: RecyclerView
    var listOfBook : List<BookEntity> = emptyList()

    private val responseLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ activityResult ->
        getAllBooks()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(5000)
        setTheme(R.style.Theme_Tenis)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        room = Room.databaseBuilder(this, BookDatabase::class.java, "book_database").build()
        val button = findViewById<FloatingActionButton>(R.id.btnOpenNewPage)
        val seacrh = findViewById<SearchView>(R.id.svBuscar)
        seacrh.setOnQueryTextListener(this)
        button.setOnClickListener {
            responseLauncher.launch(Intent(this, SecondPageActivity::class.java ))
        }
        getAllBooks()
    }

    private fun getAllBooks() {
        CoroutineScope(Dispatchers.IO).launch {
            listOfBook  = room.booksDAO().getAllBooks()
            runOnUiThread {
                if(listOfBook.isNotEmpty()){
                    initRecycler()
                }
            }
        }
    }

    private fun initRecycler() {
        librosRecyclerView = findViewById<RecyclerView>(R.id.rvListaLibros)
        librosRecyclerView.layoutManager = LinearLayoutManager(this)
        librosRecyclerView.adapter = BookAdapter(listOfBook, room)
    }

    fun hidekeyBoard(){
        val roo = findViewById<ConstraintLayout>(R.id.mainContainer)
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(roo.windowToken, 0)
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        hidekeyBoard()
        if (!p0.isNullOrEmpty()){
            buscarPorNombre(p0.lowercase())
        }else{
            getAllBooks()
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    fun buscarPorNombre(query:String){
        CoroutineScope(Dispatchers.IO).launch {
            listOfBook  = room.booksDAO().getAllBooksByTitle(query)
            runOnUiThread {
                println("Hola")
                if(listOfBook.isNotEmpty()){
                    initRecycler()
                }else{
                   showAlert()
                }
            }

        }
    }

    private fun showAlert() {
        Toast.makeText(this, "No se encontró resultados", Toast.LENGTH_LONG).show()
    }

}