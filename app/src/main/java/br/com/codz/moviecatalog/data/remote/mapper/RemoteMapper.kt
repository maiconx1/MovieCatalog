package br.com.codz.moviecatalog.data.remote.mapper

import br.com.codz.moviecatalog.data.local.entity.MovieEntity
import br.com.codz.moviecatalog.data.remote.dto.MovieDto

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun MovieDto.toEntity(): MovieEntity =
    MovieEntity(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterUrl = this.posterPath?.let { "$IMAGE_BASE_URL$it" } ?: "",
        isFavorite = false,
        lastUpdated = System.currentTimeMillis(),
    )

fun List<MovieDto>.toEntityList(): List<MovieEntity> = this.map { it.toEntity() }
