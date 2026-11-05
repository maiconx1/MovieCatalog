package br.com.codz.moviecatalog.data.remote.api

import br.com.codz.moviecatalog.data.remote.dto.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
    ): MovieResponse
}
