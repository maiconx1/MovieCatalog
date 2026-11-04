package br.com.codz.moviecatalog.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.codz.moviecatalog.data.local.entity.MovieEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Int,
    @ColumnInfo(name = COLUMN_TITLE)
    val title: String,
    @ColumnInfo(name = COLUMN_OVERVIEW)
    val overview: String,
    @ColumnInfo(name = COLUMN_POSTER_URL)
    val posterUrl: String,
    @ColumnInfo(name = COLUMN_IS_FAVORITE)
    val isFavorite: Boolean = false,
    @ColumnInfo(name = COLUMN_LAST_UPDATED)
    val lastUpdated: Long = System.currentTimeMillis(),
) {
    companion object {
        const val TABLE_NAME = "movies"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_OVERVIEW = "overview"
        const val COLUMN_POSTER_URL = "posterUrl"
        const val COLUMN_IS_FAVORITE = "isFavorite"
        const val COLUMN_LAST_UPDATED = "lastUpdated"
    }
}
