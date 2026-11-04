package br.com.codz.moviecatalog.ui.screens.movielist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.codz.moviecatalog.domain.usecase.GetMoviesUseCase
import br.com.codz.moviecatalog.domain.usecase.RefreshMoviesUseCase
import br.com.codz.moviecatalog.domain.usecase.ToggleFavoriteUseCase
import br.com.codz.moviecatalog.ui.mapper.toUiModelList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
    ) : ViewModel() {
        private val _state = MutableStateFlow(MovieListState())
        val state: StateFlow<MovieListState> = _state.asStateFlow()

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
            }
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
    }
