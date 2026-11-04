package br.com.codz.moviecatalog.domain.usecase

import br.com.codz.moviecatalog.domain.repository.MovieRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ToggleFavoriteUseCaseTest {
    private lateinit var repository: MovieRepository
    private lateinit var useCase: ToggleFavoriteUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = ToggleFavoriteUseCase(repository)
    }

    @Test
    fun `invoke should toggle false to true and call repository`() =
        runTest {
            val movieId = 1
            val currentStatus = false

            useCase(movieId, currentStatus)

            coVerify(exactly = 1) { repository.toggleFavorite(movieId, true) }
        }

    @Test
    fun `invoke should toggle true to false and call repository`() =
        runTest {
            val movieId = 1
            val currentStatus = true

            useCase(movieId, currentStatus)

            coVerify(exactly = 1) { repository.toggleFavorite(movieId, false) }
        }
}
