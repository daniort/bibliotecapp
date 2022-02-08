package com.daniort.bookapp.data.ui



import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daniort.bookapp.EditPageActivity
import com.daniort.bookapp.R
import com.daniort.bookapp.data.database.BookDatabase
import com.daniort.bookapp.data.entities.BookEntity
import com.daniort.bookapp.data.entities.toBookModel
import com.daniort.bookapp.data.models.toBundle


class BookAdapter(val tenis:List<BookEntity>, val room: BookDatabase): RecyclerView.Adapter<BookAdapter.BookHolder>() {

    interface OnListItemClick {
        fun onClick(view: View?, position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder  {
        val layaoutParaInflar = LayoutInflater.from(parent.context)
        return BookHolder(layaoutParaInflar.inflate(R.layout.item_view, parent, false))
    }
    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        holder.render(tenis[position])
    }

    override fun getItemCount(): Int = tenis.size

    class BookHolder(val view: View): RecyclerView.ViewHolder(view)  {

        val nombre = view.findViewById<TextView>(R.id.tvBookName)
        val autor = view.findViewById<TextView>(R.id.tvBookAuth)
        val editorial = view.findViewById<TextView>(R.id.tvBookEditorial)
        val ano = view.findViewById<TextView>(R.id.tvBookYear)
        val avatar = view.findViewById<ImageView>(R.id.ivItem)

        fun render(book :BookEntity){
            nombre.text = book.title
            autor.text = book.auth
            editorial.text  = book.edit
            ano.text = book.year
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap( view.context.contentResolver, Uri.parse(book.picture))
                val avatar = view.findViewById<ImageView>(R.id.ivItem)
                avatar.setImageBitmap(bitmap)
            } catch (e: Exception) { }

            view.setOnClickListener {
                val myIntent = Intent(view.context, EditPageActivity::class.java)
                myIntent.putExtras(book.toBookModel().toBundle())
                view.context.startActivity(myIntent)
            }
        }
    }


}


