package br.com.codz.moviecatalog.data.remote.api

import br.com.codz.moviecatalog.data.remote.dto.MovieResponse
import retrofit2.http.GET

interface TmdbApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(): MovieResponse
}
