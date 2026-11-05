package br.com.codz.moviecatalog.ui.screens.movielist.viewmodel

import br.com.codz.moviecatalog.ui.model.MovieUiModel

data class MovieListState(
    val selectedMovie: MovieUiModel? = null,
)

sealed class MovieListAction {
    data class OnToggleFavorite(
        val movieId: Int,
        val currentStatus: Boolean,
    ) : MovieListAction()

    data class OnMovieClicked(
        val movie: MovieUiModel,
    ) : MovieListAction()

    data object OnDismissMovieDetails : MovieListAction()
}
