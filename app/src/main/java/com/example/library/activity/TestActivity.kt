package com.example.library.activity

// ⚠️ NOVAS IMPORTAÇÕES NECESSÁRIAS PARA COROUTINES E KTOR
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.library.model.Usuario
import com.example.library.remote.UserService // Seu novo serviço Ktor
// ... outras importações ...

class TestActivity : AppCompatActivity() {

    private val TAG = "API_KTOR_TEST"
    private val userService = UserService() // Instancia o serviço Ktor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ... (código UI) ...

        // 🚨 CHAMA A NOVA FUNÇÃO DE REDE
        callApiComKtor()
    }

    // Função que usa Coroutines para chamar o Ktor de forma segura
    private fun callApiComKtor() {
        // Usa o lifecycleScope para garantir que a requisição só rode enquanto a Activity estiver viva
        lifecycleScope.launch {
            try {
                Log.d(TAG, "Iniciando chamada Ktor...")

                // 📞 CHAMA O SERVIÇO KTOR
                val users: List<Usuario> = userService.getAllUsuarios()

                // Sucesso
                Log.d(TAG, "SUCESSO: Usuários recebidos: ${users.size}")
                // Faça algo com a lista 'users'

            } catch (e: Exception) {
                // Erro (inclui erros de rede, HTTP, e serialização)
                Log.e(TAG, "ERRO NO KTOR: ${e.message}", e)
            }
        }
    }
}