package br.com.codz.moviecatalog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import br.com.codz.moviecatalog.R
import br.com.codz.moviecatalog.ui.model.MovieUiModel
import br.com.codz.moviecatalog.ui.theme.LocalSpacing
import coil3.compose.SubcomposeAsyncImage

@Composable
fun MovieCard(
    movie: MovieUiModel,
    onToggleFavorite: (Int, Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current

    Card(
        modifier =
            modifier
                .fillMaxSize()
                .aspectRatio(0.66f)
                .clickable { onClick() },
        // 2:3 aspect ratio
        shape = RoundedCornerShape(spacing.small),
        elevation = CardDefaults.cardElevation(defaultElevation = spacing.extraSmall),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                model = movie.posterUrl,
                contentDescription = stringResource(R.string.content_desc_poster, movie.title),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    CircularProgressIndicator(modifier = Modifier.requiredSize(spacing.extraLarge))
                },
            )

            IconButton(
                onClick = { onToggleFavorite(movie.id, movie.isFavorite) },
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(spacing.extraSmall),
            ) {
                Icon(
                    imageVector = if (movie.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription =
                        if (movie.isFavorite) {
                            stringResource(R.string.content_desc_favorite_true)
                        } else {
                            stringResource(
                                R.string.content_desc_favorite_false,
                            )
                        },
                    tint = if (movie.isFavorite) Color.Red else Color.White,
                )
            }
            Box(
                modifier =
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(spacing.small)
                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(spacing.extraSmall))
                        .padding(horizontal = spacing.small, vertical = spacing.extraSmall),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = stringResource(R.string.content_desc_movie_rating),
                        tint = Color.Yellow,
                        modifier = Modifier.size(spacing.medium),
                    )
                    Spacer(modifier = Modifier.width(spacing.extraSmall))
                    Text(
                        text =
                            stringResource(
                                R.string.format_rating_year,
                                movie.releaseYear,
                                movie.formattedRating,
                            ),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MovieCardPreview() {
    var isFavorite by remember { mutableStateOf(false) }
    MovieCard(
        movie =
            MovieUiModel(
                id = 1,
                title = "Movie Title",
                overview = "Movie Overview",
                posterUrl = "https://example.com/poster.jpg",
                isFavorite = isFavorite,
                releaseYear = "1999",
                formattedRating = "7.8",
            ),
        onToggleFavorite = { _, _ -> isFavorite = !isFavorite },
        onClick = {},
    )
}
