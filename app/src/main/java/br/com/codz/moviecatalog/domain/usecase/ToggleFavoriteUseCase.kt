package br.com.codz.moviecatalog.domain.usecase

import br.com.codz.moviecatalog.domain.repository.MovieRepository
import javax.inject.Inject

class ToggleFavoriteUseCase
    @Inject
    constructor(
        private val repository: MovieRepository,
    ) {
        suspend operator fun invoke(
            movieId: Int,
            currentStatus: Boolean,
        ) {
            repository.toggleFavorite(movieId, !currentStatus)
        }
    }
