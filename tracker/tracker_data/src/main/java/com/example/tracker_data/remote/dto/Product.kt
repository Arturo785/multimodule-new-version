package com.example.tracker_data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// Data transfer objects that receive the data from the remote api

data class Product(
    @field:Json(name = "image_front_thumb_url")
    val imageFrontThumbUrl: String?,
    val nutriments: Nutriments,
    @field:Json(name = "product_name")
    val productName: String?
)