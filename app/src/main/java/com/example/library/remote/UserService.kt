package com.example.library.remote

import com.example.library.model.LoginRequest
import com.example.library.model.Usuario
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson

class UserService {

    private val client = HttpClient(Android) { // <-- engine Android
        install(ContentNegotiation) {
            gson()
        }
    }

    private val BASE_URL = "http://10.0.2.2:8181/usuarios"

    suspend fun login(request: LoginRequest): Usuario {
        return client.post("$BASE_URL/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun cadastrar(usuario: Usuario): Usuario {
        return client.post("$BASE_URL/cadastrar") {
            contentType(ContentType.Application.Json)
            setBody(usuario)
        }.body()
    }
}
