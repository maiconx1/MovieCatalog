package br.com.codz.moviecatalog.ui.mapper

import br.com.codz.moviecatalog.domain.model.Movie
import br.com.codz.moviecatalog.ui.model.MovieUiModel

fun Movie.toUiModel(): MovieUiModel =
    MovieUiModel(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterUrl = this.posterUrl,
        isFavorite = this.isFavorite,
    )

fun List<Movie>.toUiModelList(): List<MovieUiModel> = this.map { it.toUiModel() }
