package br.com.codz.moviecatalog.ui.model

data class MovieUiModel(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val isFavorite: Boolean,
    val releaseYear: String,
    val formattedRating: String,
)
