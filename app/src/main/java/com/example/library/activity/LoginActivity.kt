package com.example.library.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.library.R
import com.example.library.model.LoginRequest
import com.example.library.model.LoginResponse
import com.example.library.databinding.ActivityLoginBinding // Gerado pelo View Binding
import com.example.library.remote.UserService // Seu serviço Ktor
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    // 1. Variável para o View Binding
    private lateinit var binding: ActivityLoginBinding

    // 2. Instância do Serviço de Rede Ktor (ou Retrofit)
    private val userService = UserService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 3. Inicializa o View Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonLogin = findViewById<Button>(R.id.ButtonLogin)
        // 4. Configura o Listener do Botão
        buttonLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email = binding.editTextEmail.text.toString()
        val senha = binding.editTextSenha.text.toString()

        // 5. Validação básica (campos vazios)
        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha o email e a senha.", Toast.LENGTH_SHORT).show()
            return
        }

        // 6. Mostra o indicador de progresso e desabilita o botão
        setLoadingState(true)

        // 7. Chama o Ktor (ou Retrofit) usando Coroutines
        lifecycleScope.launch {
            try {
                // Monta o objeto de requisição
                val request = LoginRequest(email, senha)

                // ⚠️ Se você está usando Retrofit, a chamada seria diferente aqui!
                val response: LoginResponse = userService.login(request) // Método do Ktor

                // SUCESSO!
                handleSuccessfulLogin(response)

            } catch (e: Exception) {
                // ERRO (Rede, 401 Unauthorized, ou JSON)
                Log.e("LOGIN_API", "Erro no login: ${e.message}", e)
                Toast.makeText(this@LoginActivity, "Login falhou. Credenciais inválidas.", Toast.LENGTH_LONG).show()
            } finally {
                // 8. Oculta o indicador de progresso
                setLoadingState(false)
            }
        }
    }

    private fun handleSuccessfulLogin(response: LoginResponse) {
        // SALVAR O TOKEN: Você deve salvar o response.accessToken em SharedPreferences
        // (Isso é crucial para manter o usuário logado e autenticar futuras chamadas)
        Log.d("LOGIN_SUCCESS", "Token recebido: ${response.accessToken}")

        // REDIRECIONAR PARA A TELA PRINCIPAL (ex: HomeActivity)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finaliza a LoginActivity para que o usuário não volte para ela
    }

    // Função auxiliar para gerenciar o estado de carregamento
    private fun setLoadingState(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.buttonLogin.isEnabled = !isLoading
        binding.editTextEmail.isEnabled = !isLoading
        binding.editTextSenha.isEnabled = !isLoading
    }
}