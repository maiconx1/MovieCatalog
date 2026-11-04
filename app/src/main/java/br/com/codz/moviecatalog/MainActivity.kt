package br.com.codz.moviecatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.codz.moviecatalog.ui.screens.movielist.MovieListScreenRoute
import br.com.codz.moviecatalog.ui.theme.MovieCatalogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieCatalogTheme {
                MovieListScreenRoute()
            }
        }
    }
}
