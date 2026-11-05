package br.com.codz.moviecatalog.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import br.com.codz.moviecatalog.data.local.MovieDatabase
import br.com.codz.moviecatalog.data.local.entity.MovieEntity
import br.com.codz.moviecatalog.data.local.entity.RemoteKeysEntity
import br.com.codz.moviecatalog.data.remote.api.TmdbApi
import br.com.codz.moviecatalog.data.remote.mapper.toEntityList
import coil3.network.HttpException
import okio.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val api: TmdbApi,
    private val database: MovieDatabase,
) : RemoteMediator<Int, MovieEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>,
    ): MediatorResult {
        return try {
            val page =
                when (loadType) {
                    LoadType.REFRESH -> 1
                    LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)

                        val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                        nextKey
                    }
                }

            val response = api.getPopularMovies(page = page)
            val movies = response.results
            val endOfPaginationReached = movies.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.movieDao().clearNonFavoriteMovies()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys =
                    movies.map {
                        RemoteKeysEntity(movieId = it.id, prevKey = prevKey, nextKey = nextKey)
                    }

                database.remoteKeysDao().insertAll(keys)
                database.movieDao().insertMovies(movies.toEntityList())
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): RemoteKeysEntity? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { movie ->
            database.remoteKeysDao().getRemoteKeysByMovieId(movie.id)
        }
}
