package com.example.library.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Usuario(
    val id: String? = null,
    val email: String,
    val senha: String
) : Parcelable