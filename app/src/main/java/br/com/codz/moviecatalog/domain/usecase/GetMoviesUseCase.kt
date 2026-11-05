package br.com.codz.moviecatalog.domain.usecase

import androidx.paging.PagingData
import br.com.codz.moviecatalog.domain.model.Movie
import br.com.codz.moviecatalog.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase
    @Inject
    constructor(
        private val repository: MovieRepository,
    ) {
        operator fun invoke(): Flow<PagingData<Movie>> = repository.getPagedMovies()
    }
