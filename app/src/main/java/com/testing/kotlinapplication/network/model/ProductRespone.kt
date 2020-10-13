package com.testing.kotlinapplication.network.model

data class ProductRespone(
    val current_page: Int,
    val `data`: List<Data>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val next_page_url: Int,
    val path: String,
    val per_page: Int,
    val prev_page_url: Int,
    val to: Int,
    val total: Int
)