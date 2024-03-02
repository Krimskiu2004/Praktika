package com.example.scheldan

data class Post(
    var id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    var amountlike: Int,
    var sharecount: Int,
    val link: String,
)