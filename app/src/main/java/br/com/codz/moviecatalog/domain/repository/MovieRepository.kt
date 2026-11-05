package br.com.codz.moviecatalog.domain.repository

import androidx.paging.PagingData
import br.com.codz.moviecatalog.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPagedMovies(): Flow<PagingData<Movie>>

    suspend fun toggleFavorite(
        movieId: Int,
        isFavorite: Boolean,
    )
}
