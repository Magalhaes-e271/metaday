package com.example.library.network

import android.util.Log
import com.example.library.model.Book
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object BookServiceKtor {

    private val client = KtorClient.client
    private const val BASE_URL = "http://10.0.2.2:8181/livros"

    // ✅ Listar todos os livros
    suspend fun listarLivros(): List<Book> = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = client.get(BASE_URL)
            val jsonText = response.bodyAsText()
            Log.d("DEBUG_JSON", "Resposta da API: $jsonText")

            Gson().fromJson(jsonText, Array<Book>::class.java).toList()
        } catch (e: Exception) {
            Log.e("ERRO_API", "Erro ao carregar livros: ${e.message}", e)
            emptyList()
        }
    }

    // ✅ Buscar livro por ID
    suspend fun buscarPorId(id: Int): Book? = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = client.get("$BASE_URL/$id")
            response.body()
        } catch (e: Exception) {
            Log.e("ERRO_API", "Erro ao buscar livro: ${e.message}", e)
            null
        }
    }

    // ✅ Cadastrar novo livro
    suspend fun cadastrarLivro(book: Book): Book? = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = client.post("$BASE_URL/cadastrar") {
                contentType(ContentType.Application.Json)
                setBody(book)
            }

            if (response.status.isSuccess()) {
                response.body()
            } else {
                Log.e("ERRO_API", "Erro ao cadastrar livro: ${response.status}")
                null
            }
        } catch (e: Exception) {
            Log.e("ERRO_API", "Exceção ao cadastrar livro: ${e.message}", e)
            null
        }
    }

    // ✅ Atualizar livro existente
    suspend fun atualizarLivro(id: Int, book: Book): Book? = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = client.put("$BASE_URL/$id") {
                contentType(ContentType.Application.Json)
                setBody(book)
            }

            if (response.status.isSuccess()) {
                response.body()
            } else {
                Log.e("ERRO_API", "Erro ao atualizar livro: ${response.status}")
                null
            }
        } catch (e: Exception) {
            Log.e("ERRO_API", "Exceção ao atualizar livro: ${e.message}", e)
            null
        }
    }

    // ✅ Deletar livro
    suspend fun deletarLivro(id: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = client.delete("$BASE_URL/$id")
            response.status.isSuccess()
        } catch (e: Exception) {
            Log.e("ERRO_API", "Erro ao deletar livro: ${e.message}", e)
            false
        }
    }
    object BookServiceKtor {

        private val client = KtorClient.client

        suspend fun listarLivros(): List<Book> = withContext(Dispatchers.IO) {
            val response: HttpResponse = client.get("http://10.0.2.2:8181/livros")
            Gson().fromJson(response.bodyAsText(), Array<Book>::class.java).toList()
        }

        suspend fun cadastrarLivro(book: Book): Book = withContext(Dispatchers.IO) {
            client.post("http://10.0.2.2:8181/livros/cadastrar") {
                contentType(ContentType.Application.Json)
                setBody(book)
            }.body()
        }

        suspend fun atualizarLivro(id: Int, book: Book): Book = withContext(Dispatchers.IO) {
            client.put("http://10.0.2.2:8181/livros/$id") {
                contentType(ContentType.Application.Json)
                setBody(book)
            }.body()
        }

        suspend fun deletarLivro(id: Int) = withContext(Dispatchers.IO) {
            client.delete("http://10.0.2.2:8181/livros/$id")
        }
    }

}
