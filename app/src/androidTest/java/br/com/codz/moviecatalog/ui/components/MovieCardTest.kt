package br.com.codz.moviecatalog.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import br.com.codz.moviecatalog.R
import br.com.codz.moviecatalog.ui.model.MovieUiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MovieCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val releaseYear = "1999"
    private val formattedRating = "8.6"

    private val fakeMovie =
        MovieUiModel(
            id = 1,
            title = "Title",
            overview = "Overview",
            posterUrl = "https://image.tmdb.org/t/p/w500/poster.jpg",
            isFavorite = false,
            releaseYear = releaseYear,
            formattedRating = formattedRating,
        )

    @Test
    fun movieCard_displaysRatingAndYearCorrectly() {
        // Arrange & Act
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        composeTestRule.setContent {
            MovieCard(
                movie = fakeMovie,
                onToggleFavorite = { _, _ -> },
                onClick = {},
            )
        }

        // Assert
        composeTestRule
            .onNodeWithText(context.getString(R.string.format_rating_year, releaseYear, formattedRating))
            .assertIsDisplayed()
    }

    @Test
    fun movieCard_clickOnFavoriteIcon_triggersCallbackWithCorrectParams() {
        // Arrange
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        var toggleId = -1
        var toggleStatus = true

        composeTestRule.setContent {
            MovieCard(
                movie = fakeMovie,
                onToggleFavorite = { id, status ->
                    toggleId = id
                    toggleStatus = status
                },
                onClick = {},
            )
        }

        // Act
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.content_desc_favorite_false))
            .performClick()

        // Assert
        assertEquals(1, toggleId)
        assertEquals(false, toggleStatus)
    }

    @Test
    fun movieCard_clickOnCard_triggersOnClickCallback() {
        // Arrange
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        var cardClicked = false

        composeTestRule.setContent {
            MovieCard(
                movie = fakeMovie,
                onToggleFavorite = { _, _ -> },
                onClick = { cardClicked = true },
            )
        }

        // Act
        composeTestRule
            .onNodeWithText(context.getString(R.string.format_rating_year, releaseYear, formattedRating))
            .performClick()

        // Assert
        assertTrue(cardClicked)
    }
}
