package br.com.codz.moviecatalog.ui.screens.movielist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.codz.moviecatalog.ui.components.MovieCard
import br.com.codz.moviecatalog.ui.model.MovieUiModel
import br.com.codz.moviecatalog.ui.screens.movielist.viewmodel.MovieListAction
import br.com.codz.moviecatalog.ui.screens.movielist.viewmodel.MovieListState
import br.com.codz.moviecatalog.ui.screens.movielist.viewmodel.MovieListViewModel

@Composable
fun MovieListScreenRoute(viewModel: MovieListViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MovieListScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun MovieListScreen(
    state: MovieListState,
    onAction: (MovieListAction) -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.isOfflineMode) {
        if (state.isOfflineMode) {
            snackBarHostState.showSnackbar(
                message = "No internet connection. Showing local data.",
            )
            onAction(MovieListAction.OnOfflineMessageShown)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            if (state.isLoading && state.movies.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(
                        items = state.movies,
                        key = { movie -> movie.id },
                    ) { movie ->
                        MovieCard(
                            movie = movie,
                            onToggleFavorite = { id, currentStatus ->
                                onAction(MovieListAction.OnToggleFavorite(id, currentStatus))
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MovieListScreenLoadingPreview() {
    MovieListScreen(MovieListState()) { }
}

@Preview
@Composable
fun MovieListScreenListPreview() {
    MovieListScreen(
        MovieListState(
            isLoading = false,
            movies =
                listOf(
                    MovieUiModel(
                        id = 1,
                        title = "Movie Title",
                        overview = "Movie Overview",
                        posterUrl = "https://example.com/poster.jpg",
                        isFavorite = false,
                    ),
                    MovieUiModel(
                        id = 2,
                        title = "Movie Title 2",
                        overview = "Movie Overview 2",
                        posterUrl = "https://example.com/poster.jpg",
                        isFavorite = true,
                    ),
                ),
        ),
    ) { }
}
