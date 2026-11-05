package br.com.codz.moviecatalog.data.repository

import br.com.codz.moviecatalog.data.local.MovieDatabase
import br.com.codz.moviecatalog.data.local.dao.MovieDao
import br.com.codz.moviecatalog.data.remote.api.TmdbApi
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryImplTest {
    private lateinit var api: TmdbApi
    private lateinit var dao: MovieDao
    private lateinit var database: MovieDatabase
    private lateinit var repository: MovieRepositoryImpl

    @Before
    fun setup() {
        api = mockk()
        database = mockk()
        dao = mockk(relaxed = true)

        every { database.movieDao() } returns dao
        repository = MovieRepositoryImpl(api, database)
    }

    @Test
    fun `get PagedMovies should return a Flow of PagingData`() =
        runTest {
            // Arrange
            every { dao.getPagedMovies() } returns mockk(relaxed = true)

            // Act
            val resultFlow = repository.getPagedMovies()

            // Assert
            assertNotNull(resultFlow)
        }

    @Test
    fun `toggleFavorite should delegate to movieDao updateFavoriteStatus`() =
        runTest {
            // Arrange
            val movieId = 1
            val isFavorite = true

            // Act
            repository.toggleFavorite(movieId, isFavorite)

            // Assert
            coVerify(exactly = 1) { dao.updateFavoriteStatus(movieId, isFavorite) }
        }
}
