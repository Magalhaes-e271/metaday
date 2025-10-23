package com.example.library.remote

import com.example.library.model.LoginRequest
import com.example.library.model.LoginResponse
import com.example.library.model.Usuario
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json // Necessário para configuração

class UserService {

    // Cliente Ktor configurado para requisições
    private val client = HttpClient(Android) {
        // Configuração para processar JSON
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    // O URL base para sua API Spring
    private val BASE_URL = "http://10.0.2.2:8080"

    // Funções do Ktor são suspensas e devem ser chamadas em Coroutines
    suspend fun getAllUsuarios(): List<Usuario> {
        // Ktor constrói a URL: "http://10.0.2.2:8080/usuarios"
        val response = client.get("$BASE_URL/usuarios")

        // Converte a resposta JSON diretamente para List<Usuario>
        return response.body()
    }

    suspend fun login(request: LoginRequest): LoginResponse {
        val response = client.post("$BASE_URL$LOGIN_PATH") {

            // 1. Define o corpo da requisição com o objeto LoginRequest
            setBody(request)

            // 2. Informa que o conteúdo enviado é JSON
            contentType(ContentType.Application.Json)
        }

    // Exemplo de uma chamada POST (Login)
    /*
    suspend fun login(request: LoginRequest): LoginResponse {
        val response = client.post("$BASE_URL/auth/login") {
            setBody(request)
        }
        return response.body()
    }
    */
}