package com.example.pokedex2024.views
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.pokedex2024.components.MainTopBar
import com.example.pokedex2024.components.PokedexText
import com.example.pokedex2024.model.Pokemon
import com.example.pokedex2024.utils.Transitions.LocalNavAnimatedVisibilityScope
import com.example.pokedex2024.utils.Transitions.LocalSharedTransitionScope
import com.example.pokedex2024.utils.Transitions.ParentClip
import com.example.pokedex2024.utils.Transitions.boundsTransform
import com.example.pokedex2024.utils.Transitions.snackDetailBoundsTransform
import com.example.pokedex2024.viewModel.PokemonViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.pokedex2024.components.Loader


@Composable
fun HomeView(
    viewModel: PokemonViewModel, navController: NavController
) {

    Scaffold(
        topBar = {
            MainTopBar(title = "Pokédex Compose", onClickBackButton = {}) {}
        }
    ) {
        ContentHome(
            viewModel = viewModel, it , navController = navController
        )
    }
}


@Composable
fun ContentHome(
    viewModel: PokemonViewModel, pad:PaddingValues,  navController: NavController
) {
    val pokemonPage = viewModel.pokemonPage.collectAsLazyPagingItems()
    Box(
        modifier = Modifier.fillMaxSize().padding(pad)
    ) {
        when {
            //Carga inicial
            pokemonPage.loadState.refresh is LoadState.Loading && pokemonPage.itemCount == 0 -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Loader()
                }
            }

            //Estado vacio
            pokemonPage.loadState.refresh is LoadState.NotLoading && pokemonPage.itemCount == 0 -> {

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "no existen pokemones")
                }

            }

            pokemonPage.loadState.hasError -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Ha ocurrido un error")
                }
            }

            else -> {
                PokedexList(pokemonPage, navController)

                if (pokemonPage.loadState.append is LoadState.Loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Loader()
                    }
                }
            }

        }
    }
}


@Composable
fun PokedexList(
    pokemonList: LazyPagingItems<Pokemon>, navController: NavController
) {
    LazyVerticalGrid(
        modifier = Modifier.testTag("PokedexList"),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(6.dp)
    ) {
        items(pokemonList.itemCount) { index ->
            val item = pokemonList[index]
            if (item != null) {
                CardPokemon(
                    item
                ) { navController.navigate("PokedexDetailsView/${item.namePokemon}/?${item.imageUrl}") }

            }
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CardPokemon(
    pokemon: Pokemon, onClick: () -> Unit
) {

    val sharedTransitionScope =
        LocalSharedTransitionScope.current ?: throw IllegalStateException("No Scope found")
    val animatedVisibilityScope =
        LocalNavAnimatedVisibilityScope.current ?: throw IllegalStateException("No Scope found")

    Card(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .testTag("Pokemon")
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        with(sharedTransitionScope) {
            Column {
                MainImage(
                    image = pokemon.imageUrl,
                    pokemon.namePokemon,
                    animatedVisibilityScope,
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                        .size(120.dp)
                        .sharedElement(
                            state = rememberSharedContentState(key = "image-${pokemon.name}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = boundsTransform,
                            placeHolderSize = SharedTransitionScope.PlaceHolderSize.contentSize,
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
                            placeHolderSize = SharedTransitionScope.PlaceHolderSize.contentSize,
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
        modifier = modifier.sharedElement(
            state = rememberSharedContentState(key = "image-${namePokemon}"),
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = snackDetailBoundsTransform,
            placeHolderSize = SharedTransitionScope.PlaceHolderSize.contentSize,
            renderInOverlayDuringTransition = true,
            zIndexInOverlay = 0f,
            clipInOverlayDuringTransition = ParentClip

        )
    )
}

