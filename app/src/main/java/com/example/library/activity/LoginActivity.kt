package com.example.library.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.library.R
import com.example.library.databinding.ActivityLoginBinding
import com.example.library.model.LoginRequest
import com.example.library.model.Usuario
import com.example.library.remote.UserService
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val userService = UserService()
    private var direcaoResgatada: String? = null
    private var origemResgatada: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val usuarioJson = sharedPref.getString("usuario_json", null)
        val usuario = Gson().fromJson(usuarioJson, Usuario::class.java)

        if(usuario != null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        direcaoResgatada = intent.getStringExtra("direcao")
        origemResgatada = intent.getStringExtra("origem")
        // Se nulo ou vazio, volta para MainActivity
        if (direcaoResgatada.isNullOrBlank()) {
            binding.ImgButtonVoltarId.visibility = View.GONE

            direcaoResgatada = "MainActivity"
        }else{
            binding.ButtonVisitante.visibility = View.GONE
        }

        binding.ButtonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val senha = binding.editTextSenha.text.toString().trim()


            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "E-mail inválido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val loginRequest = LoginRequest(email, senha)
                    val usuario = userService.login(loginRequest)

                    // Protege contra retorno nulo
                    if (usuario == null) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Usuário inválido ou erro no servidor.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@launch
                    }

                    // Salva usuário
                    val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("usuario_json", Gson().toJson(usuario))
                        apply()
                    }

                    Toast.makeText(
                        this@LoginActivity,
                        "Bem-vindo, ${usuario.nome ?: "Usuário"}!",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Tenta abrir a Activity de destino
                    val classeDestino = try {
                        // 👇 Verifica se existe a classe antes de tentar abrir
                        Class.forName("com.example.library.activity.$direcaoResgatada")
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                        MainActivity::class.java // fallback seguro
                    }

                    val intent = Intent(this@LoginActivity, classeDestino)
                    startActivity(intent)
                    finish()

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@LoginActivity,
                        "Erro ao efetuar login: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.ImgButtonVoltarId.setOnClickListener{
            val classeOrigem= try {
                // 👇 Verifica se existe a classe antes de tentar abrir
                Class.forName("com.example.library.activity.$origemResgatada")
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                MainActivity::class.java // fallback seguro
            }
            startActivity(Intent(this@LoginActivity, classeOrigem))

        }
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

        binding.ButtonVisitante.setOnClickListener {

            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }


        binding.textLinkCadastro.setOnClickListener {
            startActivity(Intent(this@LoginActivity, CadastroActivity::class.java))
            finish()
        }
    }
}
