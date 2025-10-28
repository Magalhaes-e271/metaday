package com.example.library.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.library.R
import com.example.library.model.Usuario
import com.google.gson.Gson

class PerfilActivity : AppCompatActivity() {

    private lateinit var imgPerfil: ImageView
    private lateinit var txtNomePerfil: TextView
    private lateinit var txtEmailPerfil: TextView
    private lateinit var buttonDeslogar: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        // 🔹 Configura padding automático para barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val meuBotao = findViewById<ImageButton>(R.id.ImgButtonVoltarMId)

        meuBotao.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }
        // 🔹 Inicializa as views
        imgPerfil = findViewById(R.id.imgPerfil)
        txtNomePerfil = findViewById(R.id.txtNomePerfil)
        txtEmailPerfil = findViewById(R.id.txtEmailPerfil)
        buttonDeslogar = findViewById(R.id.buttonDeslogar)

        // 🔹 Recupera o usuário salvo
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val usuarioJson = sharedPref.getString("usuario_json", null)

        if (usuarioJson != null) {
            val usuario = Gson().fromJson(usuarioJson, Usuario::class.java)
            exibirInformacoes(usuario)
        } else {
            // Caso não haja usuário logado
            txtNomePerfil.text = "Usuário não logado"
            imgPerfil.alpha = 0.3f // Deixa a imagem "apagada" para simbolizar ausência
        }
        buttonDeslogar.setOnClickListener {
            with(sharedPref.edit()) {
                remove("usuario_json") // remove usuário logado
                apply()
            }
            startActivity(Intent(this, LoginActivity::class.java))
        }


    }

    private fun exibirInformacoes(usuario: Usuario) {
        // 🔹 Define o nome ou e-mail (se quiser, pode mudar)
        txtNomePerfil.text = "Nome: "+usuario.nome ?: usuario.email
        txtEmailPerfil.text = "Email: "+usuario.email
        // 🔹 Aqui você poderia futuramente carregar uma imagem real de perfil (Glide, Picasso, etc.)
        imgPerfil.setImageResource(R.drawable.baseline_account_circle_24)
    }
}
