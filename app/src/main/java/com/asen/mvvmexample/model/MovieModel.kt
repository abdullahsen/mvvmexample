package com.asen.mvvmexample.model

data class MovieModel(
    val id: Int,
    val title: String,
    val description: String,
    val image: String,
    val releaseDate: String,
    val voteAverage: Double
)