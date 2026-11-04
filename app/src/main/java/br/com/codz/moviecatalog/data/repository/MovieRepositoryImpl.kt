package br.com.codz.moviecatalog.data.repository

import br.com.codz.moviecatalog.data.local.dao.MovieDao
import br.com.codz.moviecatalog.data.local.mapper.toDomainList
import br.com.codz.moviecatalog.data.remote.api.TmdbApi
import br.com.codz.moviecatalog.data.remote.mapper.toEntityList
import br.com.codz.moviecatalog.domain.model.Movie
import br.com.codz.moviecatalog.domain.repository.MovieRepository
import coil3.network.HttpException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

class MovieRepositoryImpl
    @Inject
    constructor(
        private val api: TmdbApi,
        private val dao: MovieDao,
    ) : MovieRepository {
        override fun getMovies(): Flow<List<Movie>> = dao.getAllMovies().map { it.toDomainList() }

        override suspend fun refreshMovies() {
            try {
                val response = api.getPopularMovies()
                val entities = response.results.toEntityList()

                dao.insertMovies(entities)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }

        override suspend fun toggleFavorite(
            movieId: Int,
            isFavorite: Boolean,
        ) {
            dao.updateFavoriteStatus(movieId, isFavorite)
        }
    }
