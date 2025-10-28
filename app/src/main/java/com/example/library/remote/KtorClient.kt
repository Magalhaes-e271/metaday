package com.example.library.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson

object KtorClient {
    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            gson()
        }
    }
}
