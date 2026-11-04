package br.com.codz.moviecatalog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import br.com.codz.moviecatalog.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM ${MovieEntity.TABLE_NAME} ORDER BY ${MovieEntity.COLUMN_LAST_UPDATED} DESC")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query(
        "SELECT * FROM ${MovieEntity.TABLE_NAME} WHERE ${MovieEntity.COLUMN_IS_FAVORITE} = 1 ORDER BY ${MovieEntity.COLUMN_LAST_UPDATED} DESC",
    )
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = IGNORE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("UPDATE ${MovieEntity.TABLE_NAME} SET ${MovieEntity.COLUMN_IS_FAVORITE} = :isFavorite WHERE ${MovieEntity.COLUMN_ID} = :movieId")
    suspend fun updateFavoriteStatus(
        movieId: Int,
        isFavorite: Boolean,
    )
}
