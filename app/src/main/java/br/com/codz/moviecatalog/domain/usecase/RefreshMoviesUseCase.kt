package br.com.codz.moviecatalog.domain.usecase

import br.com.codz.moviecatalog.domain.repository.MovieRepository
import javax.inject.Inject

class RefreshMoviesUseCase
    @Inject
    constructor(
        private val repository: MovieRepository,
    ) {
        suspend operator fun invoke() {
            repository.refreshMovies()
        }
    }
