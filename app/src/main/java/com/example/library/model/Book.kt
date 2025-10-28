package com.example.library.model

import java.io.Serializable

data class Book(
    val id: Int? = null,
    val titulo: String? ="",
    val autor: String?="",
    val descricao: String?="",
    val img: String?="",
    val type: String?="",
    val quantia: Int? = null
): Serializable
