package com.example.library.remote

import android.util.Log
import com.example.library.model.Book
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class BookService(private val client: HttpClient) {

    private val baseUrl = "http://10.0.2.2:8080/livros" // IP local do backend


    suspend fun listarLivros(): List<Book> {
        return client.get(baseUrl).body()
    }

    suspend fun buscarPorId(id: Int): Book {
        return client.get("$baseUrl/$id").body()
    }

    suspend fun cadastrarLivro(book: Book): Book {
        return client.post("$baseUrl/cadastrar") {
            contentType(ContentType.Application.Json)
            setBody(book)
        }.body()
    }

    suspend fun getLivros(): List<Book> {
        val response = client.get("http://10.0.2.2:8080/livros")

        val jsonText = response.bodyAsText()  // Lê como texto puro
        Log.d("DEBUG_JSON", jsonText)         // <-- Mostra o JSON no Logcat

        return Gson().fromJson(jsonText, Array<Book>::class.java).toList()
    }

    suspend fun atualizarLivro(id: Int, book: Book): Book {
        return client.put("$baseUrl/$id") {
            contentType(ContentType.Application.Json)
            setBody(book)
        }.body()
    }

    suspend fun deletarLivro(id: Int) {
        client.delete("$baseUrl/$id")
    }
}
