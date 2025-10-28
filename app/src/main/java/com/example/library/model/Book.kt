package com.example.library.model

data class Book(
    val id: Int = 0,
    val titulo: String,
    val autor: String,
    val descricao: String,
    val img: String,
    val type: String,
    val quantia: Int? = null
)
