@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.codz.moviecatalog.ui.screens.movielist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val isOnline by viewModel.isConnected.collectAsStateWithLifecycle()

    MovieListScreen(
        state = state,
        isOnline = isOnline,
        onAction = viewModel::onAction,
    )
}

@Composable
fun MovieListScreen(
    state: MovieListState,
    isOnline: Boolean,
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
                PullToRefreshBox(state.isLoading, { onAction(MovieListAction.OnRefresh) }) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        if (!isOnline) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Text(
                                    text = "You are offline",
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.error),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onError,
                                )
                            }
                            if (state.movies.isEmpty()) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Text(
                                        text = "No movies saved locally",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineMedium,
                                    )
                                }
                            }
                        }
                        items(
                            items = state.movies,
                            key = { movie -> movie.id },
                        ) { movie ->
                            MovieCard(
                                movie = movie,
                                onClick = { onAction(MovieListAction.OnMovieClicked(movie)) },
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

    state.selectedMovie?.let { movie ->
        ModalBottomSheet(
            onDismissRequest = {
                onAction(MovieListAction.OnDismissMovieDetails)
            },
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${movie.releaseYear} • ⭐ ${movie.formattedRating}",
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 24.sp,
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview
@Composable
fun MovieListScreenLoadingPreview() {
    MovieListScreen(MovieListState(), false) { }
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
                        releaseYear = "1999",
                        formattedRating = "4.9",
                    ),
                    MovieUiModel(
                        id = 2,
                        title = "Movie Title 2",
                        overview = "Movie Overview 2",
                        posterUrl = "https://example.com/poster.jpg",
                        isFavorite = true,
                        releaseYear = "1998",
                        formattedRating = "3.8",
                    ),
                ),
        ),
        isOnline = true,
    ) { }
}
