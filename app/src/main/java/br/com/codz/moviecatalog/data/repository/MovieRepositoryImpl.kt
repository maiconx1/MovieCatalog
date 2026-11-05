package br.com.codz.moviecatalog.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import br.com.codz.moviecatalog.data.local.MovieDatabase
import br.com.codz.moviecatalog.data.local.mapper.toDomain
import br.com.codz.moviecatalog.data.remote.api.TmdbApi
import br.com.codz.moviecatalog.domain.model.Movie
import br.com.codz.moviecatalog.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryImpl
    @Inject
    constructor(
        private val api: TmdbApi,
        private val database: MovieDatabase,
    ) : MovieRepository {
        override fun getPagedMovies(): Flow<PagingData<Movie>> =
            Pager(
                config =
                    PagingConfig(
                        pageSize = 20,
                        initialLoadSize = 20,
                        prefetchDistance = 5,
                        enablePlaceholders = true,
                    ),
                remoteMediator = MovieRemoteMediator(api, database),
                pagingSourceFactory = { database.movieDao().getPagedMovies() },
            ).flow.map { pagingData ->
                pagingData.map { it.toDomain() }
            }

        override suspend fun toggleFavorite(
            movieId: Int,
            isFavorite: Boolean,
        ) {
            database.movieDao().updateFavoriteStatus(movieId, isFavorite)
        }
    }
