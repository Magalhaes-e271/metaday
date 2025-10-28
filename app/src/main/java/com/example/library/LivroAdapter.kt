package com.example.library

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.library.activity.DescricaoActivity
import com.example.library.model.Book

class LivroAdapter(private val livros: List<Book>) :
    RecyclerView.Adapter<LivroAdapter.LivroViewHolder>() {
  var disponivel = false
    class LivroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.ImgView)
        val titulo: TextView = view.findViewById(R.id.txtTitulo)
        val autor: TextView = view.findViewById(R.id.txtAutor)
        val status: TextView = view.findViewById(R.id.txtDisponivel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_livro, parent, false)
        return LivroViewHolder(view)
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        val livro = livros[position]
        if(livro.quantia!! >= 1 ){
            holder.status.text = "Disponível: ${livro.quantia}"
        }else{
            holder.status.text = "Indisponível"
        }
        holder.titulo.text = livro.titulo
        holder.autor.text = livro.autor
      
        holder.status.setTextColor(
            if (livro.quantia!! >= 1 )
                Color.parseColor("#007700")
            else
                Color.parseColor("#FF0000")
        )

        // 🖼️ Carrega imagem
        if (livro.img?.startsWith("http") == true) {
            Glide.with(holder.itemView.context)
                .load(livro.img)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.img)
        } else {
            val resId = holder.itemView.context.resources.getIdentifier(
                livro.img ?: "", "drawable", holder.itemView.context.packageName
            )
            if (resId != 0) {
                holder.img.setImageResource(resId)
            } else {
                holder.img.setImageResource(android.R.drawable.ic_menu_report_image)
            }
        }

        // 🟢 Clique → abre a tela de descrição
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DescricaoActivity::class.java)

            // envia dados do livro via Intent
            intent.putExtra("id", livro.id)
            intent.putExtra("titulo", livro.titulo)
            intent.putExtra("autor", livro.autor)
            intent.putExtra("descricao", livro.descricao)
            intent.putExtra("img", livro.img)
            intent.putExtra("quantia", livro.quantia)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = livros.size
}
