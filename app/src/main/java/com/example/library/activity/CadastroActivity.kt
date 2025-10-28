package com.example.library.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.library.R
import com.example.library.databinding.ActivityCadastroBinding
import com.example.library.model.Usuario
import com.example.library.remote.UserService
import com.google.gson.Gson
import kotlinx.coroutines.launch

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private val userService = UserService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var senhaVisivel = false

        binding.ButtonOlhar.setOnClickListener {
            senhaVisivel = !senhaVisivel

            if (senhaVisivel) {
                binding.ButtonOlhar.setImageResource(R.drawable.olho)
                // mostra
                binding.editTextSenha.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.ButtonOlhar.setImageResource(R.drawable.olho_fechado)
                // esconde
                binding.editTextSenha.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            // mantém o cursor no final do texto
            binding.editTextSenha.setSelection(binding.editTextSenha.text?.length ?: 0)
        }


        binding.ButtonLogon.setOnClickListener {
            val nome = binding.editTextNome.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val senha = binding.editTextSenha.text.toString().trim()

            // Validação completa
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuario = Usuario(
                nome = nome,
                email = email,
                senha = senha,
                type = "n")

            // Chamada suspend dentro de coroutine
            lifecycleScope.launch {
                try {
                    val cadastroSucesso = userService.cadastrar(usuario)

                    if (cadastroSucesso != null) {
                        // Salva apenas os dados seguros (não salvar senha!)
                        val usuarioSeguro = Usuario(
                            nome = cadastroSucesso.nome,
                            email = cadastroSucesso.email,
                            senha = "" // remove a senha

                        )

                        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("usuario_json", Gson().toJson(usuarioSeguro))
                            apply()
                        }

                        Toast.makeText(this@CadastroActivity, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@CadastroActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@CadastroActivity, "Falha no cadastro. Tente novamente.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@CadastroActivity, "Erro ao conectar com servidor: ${e.message}", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }
    }
}
