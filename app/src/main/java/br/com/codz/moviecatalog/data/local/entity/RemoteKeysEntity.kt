package br.com.codz.moviecatalog.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.codz.moviecatalog.data.local.entity.RemoteKeysEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class RemoteKeysEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_MOVIE_ID)
    val movieId: Int,
    @ColumnInfo(name = COLUMN_PREV_KEY)
    val prevKey: Int?,
    @ColumnInfo(name = COLUMN_NEXT_KEY)
    val nextKey: Int?,
) {
    companion object {
        const val TABLE_NAME = "remote_keys"
        const val COLUMN_MOVIE_ID = "movie_id"
        const val COLUMN_PREV_KEY = "prev_key"
        const val COLUMN_NEXT_KEY = "next_key"
    }
}
