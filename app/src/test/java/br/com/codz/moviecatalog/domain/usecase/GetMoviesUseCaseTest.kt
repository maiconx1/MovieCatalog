package br.com.codz.moviecatalog.domain.usecase

import br.com.codz.moviecatalog.domain.repository.MovieRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMoviesUseCaseTest {
    private lateinit var repository: MovieRepository
    private lateinit var useCase: GetMoviesUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = GetMoviesUseCase(repository)
    }

    @Test
    fun `invoke should call getMovies on repository`() =
        runTest {
            useCase()
            coVerify(exactly = 1) { repository.getMovies() }
        }
}
