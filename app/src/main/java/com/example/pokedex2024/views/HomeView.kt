package com.example.pokedex2024.views


import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pokedex2024.components.Loader
import com.example.pokedex2024.components.MainTopBar
import com.example.pokedex2024.components.PokedexText
import com.example.pokedex2024.model.Pokemon
import com.example.pokedex2024.utils.Transitions.ParentClip
import com.example.pokedex2024.utils.Transitions.boundsTransform
import com.example.pokedex2024.utils.Transitions.snackDetailBoundsTransform
import com.example.pokedex2024.viewModel.PokemonViewModel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeView(
    viewModel: PokemonViewModel,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column(modifier = Modifier.fillMaxSize()) {
        MainTopBar(title = "Pokédex Compose", onClickBackButton = {}) {}

        ContentHome(
            viewModel = viewModel,
            navController = navController,
            animatedVisibilityScope
        )

    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ContentHome(
    viewModel: PokemonViewModel,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {

    val homeScreenUiState by viewModel.pokemonFlow.collectAsState()

    when (homeScreenUiState) {
        is UiState.Error -> {}
        UiState.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Loader()
            }

        }

        is UiState.Success -> {
            PokedexList(
                (homeScreenUiState as UiState.Success).data,
                animatedVisibilityScope,
                homeScreenUiState,
                navController
            )
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PokedexList(
    pokemonList: List<Pokemon>,
    animatedVisibilityScope: AnimatedVisibilityScope,
    uiState: UiState,
    navController: NavController
) {
    LazyVerticalGrid(
        modifier = Modifier.testTag("PokedexList"),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(6.dp)
    ) {
        val threadHold = 8
        itemsIndexed(items = pokemonList, key = { _, pokemon -> pokemon.name }) { index, pokemon ->
            if ((index + threadHold) >= pokemonList.size && uiState != UiState.Loading) {
                //fetchNextPokemonList()
            }
            CardPokemon(
                pokemon,
                { navController.navigate("PokedexDetails/${pokemon.namePokemon}/?${pokemon.imageUrl}") },
                animatedVisibilityScope
            )

        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CardPokemon(
    pokemon: Pokemon,
    onClick: () -> Unit, animatedVisibilityScope: AnimatedVisibilityScope,
) {

    val roundedCornerAnimation by animatedVisibilityScope.transition
        .animateDp(label = "rounded corner") { enterExit: EnterExitState ->
            when (enterExit) {
                EnterExitState.PreEnter -> 0.dp
                EnterExitState.Visible -> 20.dp
                EnterExitState.PostExit -> 20.dp
            }
        }


    Card(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .testTag("Pokemon")
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column {
            MainImage(
                image = pokemon.imageUrl,
                pokemon.namePokemon,
                animatedVisibilityScope,
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp)
                    .size(120.dp).sharedElement(
                        state = rememberSharedContentState(key = "image-${pokemon.name}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = boundsTransform,
                        placeHolderSize =  SharedTransitionScope.PlaceHolderSize.contentSize,
                        renderInOverlayDuringTransition = true,
                        zIndexInOverlay = 0f,
                        clipInOverlayDuringTransition = ParentClip
                    )
            )
            PokedexText(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(12.dp)
                    .sharedElement(
                        state = rememberSharedContentState(key = "name-${pokemon.namePokemon}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = boundsTransform,
                        placeHolderSize =  SharedTransitionScope.PlaceHolderSize.contentSize,
                        renderInOverlayDuringTransition = true,
                        zIndexInOverlay = 0f,
                        clipInOverlayDuringTransition = ParentClip
                    ),

                text = pokemon.namePokemon,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }

}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MainImage(
    image: String,
    namePokemon: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier
) {

    val imagePokemon = rememberAsyncImagePainter(model = image)

    Image(
        painter = imagePokemon,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .sharedElement(
                state = rememberSharedContentState(key = "image-${namePokemon}"),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = snackDetailBoundsTransform,
                placeHolderSize = SharedTransitionScope.PlaceHolderSize.contentSize,
                renderInOverlayDuringTransition = true,
                zIndexInOverlay =  0f,
                clipInOverlayDuringTransition =  ParentClip

            )
    )
}

