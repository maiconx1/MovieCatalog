package br.com.codz.moviecatalog.ui.screens.movielist.viewmodel

import br.com.codz.moviecatalog.MainDispatcherRule
import br.com.codz.moviecatalog.domain.model.Movie
import br.com.codz.moviecatalog.domain.usecase.GetMoviesUseCase
import br.com.codz.moviecatalog.domain.usecase.RefreshMoviesUseCase
import br.com.codz.moviecatalog.domain.usecase.ToggleFavoriteUseCase
import br.com.codz.moviecatalog.ui.mapper.toUiModelList
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getMoviesUseCase: GetMoviesUseCase = mockk()
    private val refreshMoviesUseCase: RefreshMoviesUseCase = mockk(relaxed = true)
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk(relaxed = true)

    private lateinit var viewModel: MovieListViewModel

    private fun setupViewModel() {
        viewModel =
            MovieListViewModel(getMoviesUseCase, refreshMoviesUseCase, toggleFavoriteUseCase)
    }

    @Test
    fun `init should observe database and update state with movies`() =
        runTest {
            // Arrange
            val mockMovies =
                listOf(
                    Movie(1, "Movie 1", "Overview 1", "posterUrl1", false),
                )
            coEvery { getMoviesUseCase() } returns flowOf(mockMovies)

            // Act
            setupViewModel()

            // Assert
            val currentState = viewModel.state.value
            assertEquals(mockMovies.toUiModelList(), currentState.movies)
            assertEquals(false, currentState.isLoading)
        }

    @Test
    fun `onRefresh should trigger refreshUseCase and set isOfflineMode true on error`() =
        runTest {
            // Arrange
            coEvery { getMoviesUseCase() } returns flowOf(emptyList())
            coEvery { refreshMoviesUseCase() } throws IOException("No internet")

            setupViewModel()

            // Act
            viewModel.onAction(MovieListAction.OnRefresh)

            // Assert
            val currentState = viewModel.state.value
            assertTrue(currentState.isOfflineMode)
            assertEquals(false, currentState.isLoading)
        }

    @Test
    fun `onToggleFavorite should call use case`() =
        runTest {
            // Arrange
            coEvery { getMoviesUseCase() } returns flowOf(emptyList())
            setupViewModel()

            // Act
            viewModel.onAction(MovieListAction.OnToggleFavorite(movieId = 1, currentStatus = false))

            // Assert
            coVerify(exactly = 1) { toggleFavoriteUseCase(1, false) }
        }
}
