package br.com.codz.moviecatalog.ui.screens.movielist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.platform.app.InstrumentationRegistry
import br.com.codz.moviecatalog.R
import br.com.codz.moviecatalog.ui.model.MovieUiModel
import br.com.codz.moviecatalog.ui.screens.movielist.viewmodel.MovieListState
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class MovieListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val id = 1
    private val title = "Title"
    private val overview = "Overview"
    private val posterUrl = "https://image.tmdb.org/t/p/w500/poster.jpg"
    private val isFavorite = false
    private val releaseYear = "1999"
    private val formattedRating = "8.6"

    private val fakeMovie =
        MovieUiModel(
            id = id,
            title = title,
            overview = overview,
            posterUrl = posterUrl,
            isFavorite = isFavorite,
            releaseYear = releaseYear,
            formattedRating = formattedRating,
        )

    @Test
    fun movieListScreen_whenMovieIsSelected_rendersBottomSheetWithOverview() {
        // Arrange
        val stateWithSelectedMovie = MovieListState(selectedMovie = fakeMovie)

        // Act
        composeTestRule.setContent {
            val fakePagingItems = flowOf(PagingData.from(listOf(fakeMovie))).collectAsLazyPagingItems()
            MovieListScreen(
                state = stateWithSelectedMovie,
                pagedMovies = fakePagingItems,
                isOnline = true,
                onAction = {},
            )
        }

        // Assert
        composeTestRule
            .onNodeWithText(title)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(overview)
            .assertIsDisplayed()
    }

    @Test
    fun movieListScreen_whenIsNotOnline_showsOfflineText() {
        // Arrange
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val stateWithSelectedMovie = MovieListState()

        // Act
        composeTestRule.setContent {
            val fakePagingItems = flowOf(PagingData.from(listOf(fakeMovie))).collectAsLazyPagingItems()
            MovieListScreen(
                state = stateWithSelectedMovie,
                pagedMovies = fakePagingItems,
                isOnline = false,
                onAction = {},
            )
        }

        // Assert
        composeTestRule
            .onNodeWithText(context.getString(R.string.you_are_offline))
            .assertIsDisplayed()
    }
}
