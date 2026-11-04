package br.com.codz.moviecatalog.domain.repository

import br.com.codz.moviecatalog.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(): Flow<List<Movie>>

    suspend fun refreshMovies()

    suspend fun toggleFavorite(
        movieId: Int,
        isFavorite: Boolean,
    )
}
