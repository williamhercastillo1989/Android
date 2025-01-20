package com.example.pokedex2024.views

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil.compose.rememberAsyncImagePainter
import com.example.pokedex2024.R
import com.example.pokedex2024.components.PokedexText
import com.example.pokedex2024.components.paletteBackgroundBrush
import com.example.pokedex2024.utils.Transitions.LocalNavAnimatedVisibilityScope
import com.example.pokedex2024.utils.Transitions.LocalSharedTransitionScope
import com.example.pokedex2024.utils.Transitions.ParentClip
import com.example.pokedex2024.utils.Transitions.boundsTransform
import com.example.pokedex2024.viewModel.PokemonViewModel


@Composable
fun PokedexDetailsView(
    viewModel: PokemonViewModel,
    navController: NavController,
    name: String?,
    imgUrl: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .testTag("PokedexDetails"),
    ) {
        DetailsHeader(name, imgUrl, navController)
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailsHeader(
    name: String?,
    imgUrl: String?,
    navController: NavController
) {

    var palette by remember { mutableStateOf<Palette?>(null) }
    val backgroundBrush by palette.paletteBackgroundBrush()

    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No Scope found")

    val roundedCornerAnimation by animatedVisibilityScope.transition
        .animateDp(label = "rounded corner") { enterExit: EnterExitState ->
            when (enterExit) {
                EnterExitState.PreEnter -> 0.dp
                EnterExitState.Visible -> 20.dp
                EnterExitState.PostExit -> 20.dp
            }
        }

    val shape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 64.dp,
        bottomEnd = 64.dp,
    )
    with(sharedTransitionScope) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(290.dp)
                .shadow(elevation = 9.dp, shape = shape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.inversePrimary
                        )
                    ), shape = shape
                ),
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .clickable { navController.navigateUp() },
                    painter = painterResource(R.drawable.arrow_back),
                    tint = MaterialTheme.colorScheme.background,
                    contentDescription = null,
                )

                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = name.orEmpty(),
                    color = MaterialTheme.colorScheme.background,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }

            PokedexText(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .statusBarsPadding(),
                text = "#010",
                previewText = "#001",
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )


            val imagePokemon = rememberAsyncImagePainter(model = imgUrl)

            Image(
                painter = imagePokemon,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .size(190.dp)
                    .sharedElement(
                        state = rememberSharedContentState(key = "image-${name}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = boundsTransform,
                        placeHolderSize = SharedTransitionScope.PlaceHolderSize.contentSize,
                        renderInOverlayDuringTransition = true,
                        zIndexInOverlay = 0f,
                        clipInOverlayDuringTransition = ParentClip
                    )
            )
        }

        PokedexText(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .sharedElement(
                    state = rememberSharedContentState(key = "name-${name}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = boundsTransform,
                    placeHolderSize = SharedTransitionScope.PlaceHolderSize.contentSize,
                    renderInOverlayDuringTransition = true,
                    zIndexInOverlay = 0f,
                    clipInOverlayDuringTransition = ParentClip
                ),

            text = name!!,
            previewText = "skydoves",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
        )
    }
}