package com.example.library.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.library.LivroAdapter
import com.example.library.databinding.ActivityMainBinding
import com.example.library.model.Usuario
import com.example.library.network.BookServiceKtor
import com.example.library.network.KtorClient
import com.example.library.remote.BookService
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val bookService = BookService(KtorClient.client) // ✅ usa Ktor

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.buttonAdd.setOnClickListener {
            intent.putExtra("origem", "MainActivity")
            startActivity(intent)
        }
        // Carrega usuário logado
        val usuario = carregarUsuario()

        // Configura botão de perfil
        binding.ImgButtonPerfilId.setOnClickListener {
            if (usuario == null) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("direcao", "PerfilActivity")
                intent.putExtra("origem", "MainActivity")
                startActivity(intent)
            } else {
                startActivity(Intent(this, PerfilActivity::class.java))
            }
        }

        // Busca livros do backend
        carregarLivros()

        val usuarioNormalOuNulo = usuario?.type == null || usuario.type == "n"
        if (usuarioNormalOuNulo) {
            binding.buttonAdd.visibility = View.GONE
        }

    }

    private fun carregarUsuario(): Usuario? {
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val usuarioJson = sharedPref.getString("usuario_json", null)
        return Gson().fromJson(usuarioJson, Usuario::class.java)
    }

    private fun carregarLivros() {
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val livros = BookServiceKtor.listarLivros()
                binding.recyclerView.adapter = LivroAdapter(livros)
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Erro ao carregar livros: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }

    }
}
