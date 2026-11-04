package br.com.codz.moviecatalog.di

import android.content.Context
import androidx.room.Room
import br.com.codz.moviecatalog.data.local.MovieDatabase
import br.com.codz.moviecatalog.data.local.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
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
            ).build()

    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao = database.movieDao()
}
