package com.example.samplekotlin.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val externalId: String,
)
