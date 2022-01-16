package com.example.samplekotlin.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String
)
