package br.com.codz.moviecatalog.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.codz.moviecatalog.data.local.dao.MovieDao
import br.com.codz.moviecatalog.data.local.dao.RemoteKeysDao
import br.com.codz.moviecatalog.data.local.entity.MovieEntity
import br.com.codz.moviecatalog.data.local.entity.RemoteKeysEntity

@Database(entities = [MovieEntity::class, RemoteKeysEntity::class], version = 2, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    abstract fun remoteKeysDao(): RemoteKeysDao
}
