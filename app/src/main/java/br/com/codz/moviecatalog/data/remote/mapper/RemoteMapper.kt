package br.com.codz.moviecatalog.data.remote.mapper

import br.com.codz.moviecatalog.data.local.entity.MovieEntity
import br.com.codz.moviecatalog.data.remote.dto.MovieDto

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun MovieDto.toEntity(timestamp: Long): MovieEntity =
    MovieEntity(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterUrl = this.posterPath?.let { "$IMAGE_BASE_URL$it" } ?: "",
        releaseDate = this.releaseDate ?: "",
        voteAverage = this.voteAverage ?: 0.0,
        isFavorite = false,
        lastUpdated = timestamp,
    )

fun List<MovieDto>.toEntityList(): List<MovieEntity> {
    val currentTime = System.currentTimeMillis()
    return this.mapIndexed { index, dto ->
        dto.toEntity(timestamp = currentTime + index)
    }
}
