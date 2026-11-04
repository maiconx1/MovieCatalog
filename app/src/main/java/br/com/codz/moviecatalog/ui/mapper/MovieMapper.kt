package br.com.codz.moviecatalog.ui.mapper

import br.com.codz.moviecatalog.domain.model.Movie
import br.com.codz.moviecatalog.ui.model.MovieUiModel
import java.util.Locale

fun Movie.toUiModel(): MovieUiModel {
    val year =
        if (this.releaseDate.length >= 4) {
            this.releaseDate.substring(0, 4)
        } else {
            "N/A"
        }

    val rating = String.format(Locale.US, "%.1f", this.voteAverage)

    return MovieUiModel(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterUrl = this.posterUrl,
        isFavorite = this.isFavorite,
        releaseYear = year,
        formattedRating = rating,
    )
}

fun List<Movie>.toUiModelList(): List<MovieUiModel> = this.map { it.toUiModel() }
