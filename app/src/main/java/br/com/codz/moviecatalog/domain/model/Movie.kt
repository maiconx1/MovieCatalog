package br.com.codz.moviecatalog.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val isFavorite: Boolean,
)
