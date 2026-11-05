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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import br.com.codz.moviecatalog.R
import br.com.codz.moviecatalog.ui.components.MovieCard
import br.com.codz.moviecatalog.ui.model.MovieUiModel
import br.com.codz.moviecatalog.ui.screens.movielist.viewmodel.MovieListAction
import br.com.codz.moviecatalog.ui.screens.movielist.viewmodel.MovieListState
import br.com.codz.moviecatalog.ui.screens.movielist.viewmodel.MovieListViewModel
import br.com.codz.moviecatalog.ui.theme.LocalSpacing
import kotlinx.coroutines.flow.flowOf

@Composable
fun MovieListScreenRoute(viewModel: MovieListViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by viewModel.isConnected.collectAsStateWithLifecycle()

    val pagedMovies = viewModel.pagedMovies.collectAsLazyPagingItems()

    MovieListScreen(
        state = state,
        isOnline = isOnline,
        pagedMovies = pagedMovies,
        onAction = viewModel::onAction,
    )
}

@Composable
fun MovieListScreen(
    state: MovieListState,
    isOnline: Boolean,
    pagedMovies: LazyPagingItems<MovieUiModel>,
    onAction: (MovieListAction) -> Unit,
) {
    val spacing = LocalSpacing.current
    val snackBarHostState = remember { SnackbarHostState() }

    val offlineErrorMessage = stringResource(R.string.error_offline)

    val loadState = pagedMovies.loadState.mediator
    LaunchedEffect(loadState) {
        if (loadState?.refresh is LoadState.Error || loadState?.append is LoadState.Error) {
            snackBarHostState.showSnackbar(
                message = offlineErrorMessage,
            )
        }
    }

    LaunchedEffect(isOnline) {
        if (isOnline) {
            pagedMovies.retry()
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
            if (pagedMovies.loadState.refresh is LoadState.Loading && pagedMovies.itemCount == 0) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                val isRefreshing = pagedMovies.loadState.refresh is LoadState.Loading
                PullToRefreshBox(isRefreshing, { pagedMovies.refresh() }) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(spacing.medium),
                        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                        verticalArrangement = Arrangement.spacedBy(spacing.medium),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        if (!isOnline) {
                            stickyHeader {
                                Text(
                                    text = stringResource(R.string.you_are_offline),
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.error),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onError,
                                )
                            }
                            if (pagedMovies.itemCount == 0) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Text(
                                        text = stringResource(R.string.no_movies_saved),
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineMedium,
                                    )
                                }
                            }
                        }
                        items(
                            count = pagedMovies.itemCount,
                            key = pagedMovies.itemKey { it.id },
                            contentType = pagedMovies.itemContentType { "MovieCard" },
                        ) { index ->
                            pagedMovies[index]?.let { movie ->
                                MovieCard(
                                    movie = movie,
                                    onClick = { onAction(MovieListAction.OnMovieClicked(movie)) },
                                    onToggleFavorite = { id, currentStatus ->
                                        onAction(
                                            MovieListAction.OnToggleFavorite(
                                                id,
                                                currentStatus,
                                            ),
                                        )
                                    },
                                )
                            }
                        }

                        if (pagedMovies.loadState.append is LoadState.Loading) {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(spacing.medium),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
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
                        .padding(horizontal = spacing.large, vertical = spacing.medium),
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(spacing.small))
                Text(
                    text =
                        stringResource(
                            R.string.format_rating_year,
                            movie.releaseYear,
                            movie.formattedRating,
                        ),
                )
                Spacer(modifier = Modifier.height(spacing.medium))
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 24.sp,
                )
                Spacer(modifier = Modifier.height(spacing.extraLarge))
            }
        }
    }
}

@Preview
@Composable
fun MovieListScreenLoadingPreview() {
    MovieListScreen(
        MovieListState(),
        false,
        flowOf(PagingData.empty<MovieUiModel>()).collectAsLazyPagingItems(),
    ) { }
}

@Preview
@Composable
fun MovieListScreenListPreview() {
    MovieListScreen(
        state = MovieListState(),
        pagedMovies = flowOf(PagingData.empty<MovieUiModel>()).collectAsLazyPagingItems(),
        isOnline = true,
    ) { }
}
