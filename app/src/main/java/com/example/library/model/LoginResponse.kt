package com.example.library.model

data class LoginResponse(
    // O nome da chave deve ser o mesmo que o Spring retorna no JSON (ex: accessToken)
    val accessToken: String,
    val tokenType: String = "Bearer"
)