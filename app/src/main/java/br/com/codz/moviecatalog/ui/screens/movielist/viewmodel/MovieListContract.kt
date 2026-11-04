package br.com.codz.moviecatalog.ui.screens.movielist.viewmodel

import br.com.codz.moviecatalog.ui.model.MovieUiModel

data class MovieListState(
    val isLoading: Boolean = true,
    val movies: List<MovieUiModel> = emptyList(),
    val error: String? = null,
    val isOfflineMode: Boolean = false,
    val selectedMovie: MovieUiModel? = null,
)

sealed class MovieListAction {
    data class OnToggleFavorite(
        val movieId: Int,
        val currentStatus: Boolean,
    ) : MovieListAction()

    data object OnRefresh : MovieListAction()

    data object OnOfflineMessageShown : MovieListAction()

    data class OnMovieClicked(
        val movie: MovieUiModel,
    ) : MovieListAction()

    data object OnDismissMovieDetails : MovieListAction()
}
