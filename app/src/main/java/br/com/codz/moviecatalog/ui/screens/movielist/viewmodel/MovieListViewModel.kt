package br.com.codz.moviecatalog.ui.screens.movielist.viewmodel

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.codz.moviecatalog.domain.usecase.GetMoviesUseCase
import br.com.codz.moviecatalog.domain.usecase.RefreshMoviesUseCase
import br.com.codz.moviecatalog.domain.usecase.ToggleFavoriteUseCase
import br.com.codz.moviecatalog.ui.mapper.toUiModelList
import br.com.codz.moviecatalog.ui.model.MovieUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel
    @Inject
    constructor(
        private val getMoviesUseCase: GetMoviesUseCase,
        private val refreshMoviesUseCase: RefreshMoviesUseCase,
        private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
        private val connectivityManager: ConnectivityManager,
    ) : ViewModel() {
        private val _state = MutableStateFlow(MovieListState())
        val state: StateFlow<MovieListState> = _state.asStateFlow()

        val isConnected: StateFlow<Boolean> =
            callbackFlow {
                trySend(getCurrentConnectivityState())

                val callback =
                    object : ConnectivityManager.NetworkCallback() {
                        override fun onAvailable(network: Network) {
                            trySend(true)
                        }

                        override fun onLost(network: Network) {
                            trySend(false)
                        }

                        override fun onCapabilitiesChanged(
                            network: Network,
                            networkCapabilities: NetworkCapabilities,
                        ) {
                            val hasInternet =
                                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            trySend(hasInternet)
                            if (hasInternet) {
                                refreshMoviesFromNetwork()
                            }
                        }
                    }

                connectivityManager.registerDefaultNetworkCallback(callback)
                awaitClose {
                    connectivityManager.unregisterNetworkCallback(callback)
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = getCurrentConnectivityState(),
            )

        init {
            observeMoviesFromDatabase()
            refreshMoviesFromNetwork()
        }

        fun onAction(action: MovieListAction) {
            when (action) {
                is MovieListAction.OnOfflineMessageShown -> clearOfflineMessage()
                is MovieListAction.OnRefresh -> refreshMoviesFromNetwork()
                is MovieListAction.OnToggleFavorite ->
                    toggleFavorite(
                        action.movieId,
                        action.currentStatus,
                    )

                MovieListAction.OnDismissMovieDetails -> dismissMovieDetails()
                is MovieListAction.OnMovieClicked -> selectMovie(action.movie)
            }
        }

        private fun getCurrentConnectivityState(): Boolean {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

        private fun observeMoviesFromDatabase() {
            viewModelScope.launch {
                getMoviesUseCase()
                    .catch { e ->
                        _state.update {
                            it.copy(error = e.message, isLoading = false)
                        }
                    }.collect { cachedMovies ->
                        _state.update {
                            it.copy(
                                movies = cachedMovies.toUiModelList(),
                                isLoading = cachedMovies.isEmpty() && it.isLoading,
                            )
                        }
                    }
            }
        }

        private fun refreshMoviesFromNetwork() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true, error = null, isOfflineMode = false) }
                try {
                    refreshMoviesUseCase() // Repository inserts movies into database and the new data is automatically emitted
                    _state.update { it.copy(isLoading = false) }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(isLoading = false, isOfflineMode = true)
                    }
                }
            }
        }

        private fun toggleFavorite(
            movieId: Int,
            currentStatus: Boolean,
        ) {
            viewModelScope.launch {
                toggleFavoriteUseCase(movieId, currentStatus)
            }
        }

        private fun clearOfflineMessage() {
            _state.update { it.copy(isOfflineMode = false) }
        }

        private fun dismissMovieDetails() {
            _state.update {
                it.copy(selectedMovie = null)
            }
        }

        private fun selectMovie(movie: MovieUiModel) {
            _state.update {
                it.copy(selectedMovie = movie)
            }
        }
    }
