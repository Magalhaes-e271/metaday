package com.example.library.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.library.R
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
        setContentView(R.layout.activity_descricao) // 👈 nome do layout XML (criaremos abaixo)



        val usuario = carregarUsuario()
        val imgBedit = findViewById<ImageButton>(R.id.ButtonEdit)


        val usuarioNormalOuNulo = usuario?.type == null || usuario.type == "n"
        if (usuarioNormalOuNulo) {
            imgBedit.visibility = View.GONE
        }




        val meuBotao = findViewById<ImageButton>(R.id.ImgButtonVoltaId)
        meuBotao.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }

        // 🔹 Pegando os dados enviados pela Intent
        val titulo = intent.getStringExtra("titulo")
        val autor = intent.getStringExtra("autor")
        val descricao = intent.getStringExtra("descricao")
        val img = intent.getStringExtra("img")
        val quantia = intent.getStringExtra("quantia")
        val disponivel = intent.getBooleanExtra("disponivel", false)


        // 🔹 Ligando com os elementos da tela
        val imgView = findViewById<ImageView>(R.id.imgDetalhe)
        val txtTitulo = findViewById<TextView>(R.id.txtTituloDetalhe)
        val txtAutor = findViewById<TextView>(R.id.txtAutorDetalhe)
        val txtDescricao = findViewById<TextView>(R.id.txtDescricaoDetalhe)
        val txtDisponivel = findViewById<TextView>(R.id.txtDisponivelDetalhe)

        // 🔹 Preenchendo os dados
        txtTitulo.text = titulo
        txtAutor.text = "Autor: $autor"
        txtDescricao.text = descricao
        txtDisponivel.text = if (disponivel) "Disponível" else "Indisponível"


        val buttonEdit = findViewById<ImageButton>(R.id.ButtonEdit)
        buttonEdit.setOnClickListener {

            val intent = Intent(this, txtAutor::class.java)

            // envia dados do livro via Intent
            intent.putExtra("titulo", titulo)
            intent.putExtra("autor", autor)
            intent.putExtra("descricao", descricao)
            intent.putExtra("img", img)
            intent.putExtra("quantia", quantia)
            intent.putExtra("disponivel", disponivel)

            startActivity(intent)

        }
        txtDisponivel.setTextColor(
            if (txtDisponivel.text == "Disponível") {
                Color.parseColor("#007700")
            } else Color.parseColor("#FF0000")
        )
        // 🔹 Carregando a imagem (com Glide ou drawable local)
        if (img != null) {
            if (img.startsWith("http")) {
                Glide.with(this).load(img).into(imgView)
            } else {
                val resId = resources.getIdentifier(img, "drawable", packageName)
                if (resId != 0) {
                    imgView.setImageResource(resId)
                } else {
                    imgView.setImageResource(android.R.drawable.ic_menu_report_image)
                }

            }

        }


    }

}
