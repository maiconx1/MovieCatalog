package br.com.codz.moviecatalog.data.repository

import br.com.codz.moviecatalog.data.local.dao.MovieDao
import br.com.codz.moviecatalog.data.remote.api.TmdbApi
import br.com.codz.moviecatalog.data.remote.dto.MovieResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryImplTest {
    private lateinit var api: TmdbApi
    private lateinit var dao: MovieDao
    private lateinit var repository: MovieRepositoryImpl

    @Before
    fun setup() {
        api = mockk()
        dao = mockk(relaxed = true)
        repository = MovieRepositoryImpl(api, dao)
    }

    @Test
    fun `refreshMovies should insert into dao when api returns success`() =
        runTest {
            // Arrange
            val mockResponse = MovieResponse(page = 1, results = emptyList())
            coEvery { api.getPopularMovies() } returns mockResponse

            // Act
            repository.refreshMovies()

            // Assert
            coVerify(exactly = 1) { dao.insertMovies(any()) }
        }

    @Test
    fun `refreshMovies should catch IOException and NOT crash when offline`() =
        runTest {
            // Arrange
            coEvery { api.getPopularMovies() } throws IOException("No internet")

            // Act
            repository.refreshMovies()

            // Assert
            coVerify(exactly = 0) { dao.insertMovies(any()) }
        }
}
