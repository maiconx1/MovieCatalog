package br.com.codz.moviecatalog.ui.screens.movielist.viewmodel

import android.net.ConnectivityManager
import androidx.paging.PagingData
import br.com.codz.moviecatalog.MainDispatcherRule
import br.com.codz.moviecatalog.domain.model.Movie
import br.com.codz.moviecatalog.domain.usecase.GetMoviesUseCase
import br.com.codz.moviecatalog.domain.usecase.ToggleFavoriteUseCase
import br.com.codz.moviecatalog.ui.model.MovieUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getMoviesUseCase: GetMoviesUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk(relaxed = true)
    private val connectivityManager: ConnectivityManager = mockk(relaxed = true)

    private lateinit var viewModel: MovieListViewModel

    private fun setupViewModel() {
        every { getMoviesUseCase() } returns flowOf(PagingData.empty())

        viewModel =
            MovieListViewModel(getMoviesUseCase, toggleFavoriteUseCase, connectivityManager)
    }

    @Test
    fun `init should observe database and update state with movies`() =
        runTest {
            // Arrange
            val mockMovies =
                listOf(
                    Movie(1, "Movie 1", "Overview 1", "posterUrl1", "1999", 8.9, false),
                )
            coEvery { getMoviesUseCase() } returns flowOf(PagingData.from(mockMovies))

            // Act
            setupViewModel()

            // Assert
            val currentState = viewModel.state.value
            assertEquals(null, currentState.selectedMovie)
        }

    @Test
    fun `onAction OnMovieClicked should update selectedMovie state`() {
        // Arrange
        setupViewModel()
        val movie =
            MovieUiModel(
                id = 1,
                title = "Title",
                overview = "Overview",
                posterUrl = "",
                isFavorite = false,
                releaseYear = "2000",
                formattedRating = "9.5",
            )

        // Act
        viewModel.onAction(MovieListAction.OnMovieClicked(movie))

        // Assert
        val currentState = viewModel.state.value
        assertEquals(movie, currentState.selectedMovie)
    }

    @Test
    fun `onAction OnDismissMovieDetails should set selectedMovie to null`() {
        // Arrange
        setupViewModel()

        val movie = MovieUiModel(1, "Title", "Overview", "url", false, "2023", "8.0")
        viewModel.onAction(MovieListAction.OnMovieClicked(movie))

        // Act
        viewModel.onAction(MovieListAction.OnDismissMovieDetails)

        // Assert
        val currentState = viewModel.state.value
        assertNull(currentState.selectedMovie)
    }

    @Test
    fun `onToggleFavorite should call use case`() =
        runTest {
            // Arrange
            setupViewModel()

            // Act
            viewModel.onAction(MovieListAction.OnToggleFavorite(movieId = 1, currentStatus = false))

            // Assert
            coVerify(exactly = 1) { toggleFavoriteUseCase(1, false) }
        }
}
