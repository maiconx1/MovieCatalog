package br.com.codz.moviecatalog.domain.usecase

import br.com.codz.moviecatalog.domain.model.Movie
import br.com.codz.moviecatalog.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke(): Flow<List<Movie>> {
        return repository.getMovies()
    }
}