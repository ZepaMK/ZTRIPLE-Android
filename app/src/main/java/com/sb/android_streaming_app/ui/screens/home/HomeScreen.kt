package com.sb.android_streaming_app.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sb.android_streaming_app.data.models.MovieItem
import com.sb.android_streaming_app.data.models.Response
import com.sb.android_streaming_app.ui.components.ProgressBar

/**
 * Created by Zep S. on 03/03/2023.
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(onClick: (Int) -> Unit) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        content = {
            Home(
                homeContent = { movies ->
                    HomeContent(
                        movies = movies,
                        onClick = { onClick(it) }
                    )
                },
            )
        }
    )
}

@Composable
fun Home(
    viewModel: HomeViewModel = hiltViewModel(),
    homeContent: @Composable (movies: List<MovieItem>) -> Unit,
) {
    viewModel.fetchMovies()
    when (val moviesResponse = viewModel.moviesResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> homeContent(moviesResponse.data)
        is Response.Failure -> print(moviesResponse.e)
    }
}

@Composable
fun HomeContent(
    movies: List<MovieItem>,
    onClick: (Int) -> Unit
) {
    Column {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = movies
            ) { movie ->
                MovieItemCard(
                    movieItem = movie,
                    onClick = { onClick(it) }
                )
            }
        }
    }
}

@Composable
fun MovieItemCard(
    movieItem: MovieItem,
    onClick: (id: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onClick(movieItem.id) }
            .background(MaterialTheme.colors.primary)
            .aspectRatio(0.8f),
        shape = MaterialTheme.shapes.large,
        elevation = 0.dp
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movieItem.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier.background(MaterialTheme.colors.primary)
        )
    }
}
