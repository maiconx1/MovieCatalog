package br.com.codz.moviecatalog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import br.com.codz.moviecatalog.data.local.entity.RemoteKeysEntity

@Dao
interface RemoteKeysDao {
    @Query("SELECT * FROM ${RemoteKeysEntity.TABLE_NAME} WHERE ${RemoteKeysEntity.COLUMN_MOVIE_ID} = :id")
    suspend fun getRemoteKeysByMovieId(id: Int): RemoteKeysEntity?

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeysEntity>)

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}
