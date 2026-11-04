package br.com.codz.moviecatalog.data.local.mapper

import br.com.codz.moviecatalog.data.local.entity.MovieEntity
import br.com.codz.moviecatalog.domain.model.Movie

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterUrl = this.posterUrl,
        isFavorite = this.isFavorite
    )
}

fun List<MovieEntity>.toDomainList(): List<Movie> {
    return this.map { it.toDomain() }
}