package com.example.library.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.library.R
import com.example.library.model.Book
import com.example.library.model.Usuario
import com.google.gson.Gson

class DescricaoActivity : AppCompatActivity() {

    private fun carregarUsuario(): Usuario? {
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val usuarioJson = sharedPref.getString("usuario_json", null)
        return Gson().fromJson(usuarioJson, Usuario::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_descricao)

        val usuario = carregarUsuario()
        val imgBedit = findViewById<ImageButton>(R.id.ButtonEdit)

        // 🔹 Verifica se o usuário é normal e oculta botão de edição
        val usuarioNormalOuNulo = usuario?.type == null || usuario.type == "n"
        if (usuarioNormalOuNulo) {
            imgBedit.visibility = View.GONE
        }

        // 🔹 Botão de voltar
        val btnVoltar = findViewById<ImageButton>(R.id.ImgButtonVoltaId)
        btnVoltar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // 🔹 Recebe o livro pela intent
        val livro = intent.getSerializableExtra("livro") as? Book
        if (livro == null) {
            Toast.makeText(this, "Erro: livro não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 🔹 Referências da tela
        val imgView = findViewById<ImageView>(R.id.imgDetalhe)
        val txtTitulo = findViewById<TextView>(R.id.txtTituloDetalhe)
        val txtAutor = findViewById<TextView>(R.id.txtAutorDetalhe)
        val txtDescricao = findViewById<TextView>(R.id.txtDescricaoDetalhe)
        val txtDisponivel = findViewById<TextView>(R.id.txtDisponivelDetalhe)

        // 🔹 Preenche campos com dados do livro
        txtTitulo.text = livro.titulo
        txtAutor.text = "Autor: ${livro.autor}"
        txtDescricao.text = livro.descricao

        // Considera disponível se a quantia > 0
        val disponivel = (livro.quantia ?: 0) > 0
        txtDisponivel.text = if (disponivel) "Disponível" else "Indisponível"
        txtDisponivel.setTextColor(
            if (disponivel) Color.parseColor("#007700")
            else Color.parseColor("#FF0000")
        )

        // 🔹 Carrega imagem (link ou drawable)
        val img = livro.img
        if (!img.isNullOrBlank()) {
            if (img.startsWith("http")) {
                Glide.with(this).load(img).into(imgView)
            } else {
                val resId = resources.getIdentifier(img, "drawable", packageName)
                if (resId != 0) imgView.setImageResource(resId)
                else imgView.setImageResource(android.R.drawable.ic_menu_report_image)
            }
        } else {
            imgView.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        // 🔹 Abre tela de edição se o usuário for admin
        imgBedit.setOnClickListener {
            val intent = Intent(this, AtualizarLivroActivity::class.java)
            intent.putExtra("livro", livro)
            startActivity(intent)
        }
    }
}
