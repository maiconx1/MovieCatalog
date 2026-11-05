package br.com.codz.moviecatalog.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.codz.moviecatalog.data.local.MovieDatabase
import br.com.codz.moviecatalog.data.local.dao.MovieDao
import br.com.codz.moviecatalog.data.local.entity.RemoteKeysEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private val MIGRATION_1_2 =
        object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `${RemoteKeysEntity.TABLE_NAME}` (
                        `${RemoteKeysEntity.COLUMN_MOVIE_ID}` INTEGER NOT NULL,
                        `${RemoteKeysEntity.COLUMN_PREV_KEY}` INTEGER,
                        `${RemoteKeysEntity.COLUMN_NEXT_KEY}` INTEGER,
                        PRIMARY KEY (`${RemoteKeysEntity.COLUMN_MOVIE_ID}`)
                    )
                    """.trimIndent(),
                )
            }
        }

    @Provides
    @Singleton
    fun provideMovieDatabase(
        @ApplicationContext context: Context,
    ): MovieDatabase =
        Room
            .databaseBuilder(
                context,
                MovieDatabase::class.java,
                "movie_database",
            ).addMigrations(MIGRATION_1_2)
            .build()

    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao = database.movieDao()
}
