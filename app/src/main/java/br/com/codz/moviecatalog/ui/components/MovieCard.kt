package br.com.codz.moviecatalog.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.codz.moviecatalog.ui.model.MovieUiModel
import coil3.compose.AsyncImage

@Composable
fun MovieCard(
    movie: MovieUiModel,
    onToggleFavorite: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxSize()
                .aspectRatio(0.66f),
        // 2:3 aspect ratio
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = "Poster for ${movie.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

            IconButton(
                onClick = { onToggleFavorite(movie.id, movie.isFavorite) },
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp),
            ) {
                Icon(
                    imageVector = if (movie.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (movie.isFavorite) Color.Red else Color.White,
                )
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
            ),
        onToggleFavorite = { _, _ -> isFavorite = !isFavorite },
        modifier = Modifier.padding(8.dp),
    )
}
