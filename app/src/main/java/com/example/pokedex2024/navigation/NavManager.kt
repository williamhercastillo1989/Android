package com.example.pokedex2024.navigation


import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex2024.viewModel.PokemonViewModel
import com.example.pokedex2024.views.HomeView
import com.example.pokedex2024.views.PokedexDetails


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavManager(viewModel: PokemonViewModel, animatedVisibilityScope: AnimatedVisibilityScope) {
    SharedTransitionLayout {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "Home") {
            composable("Home") {
                HomeView(viewModel, navController, animatedVisibilityScope)
            }
            composable("PokedexDetails/{name}/?{imgUrl}", arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("imgUrl") { type = NavType.StringType }
            )) {
                val name = it.arguments?.getString("name") ?: ""
                val imgUrl = it.arguments?.getString("imgUrl") ?: ""
                PokedexDetails(viewModel, navController, name, imgUrl, animatedVisibilityScope)
            }
        }
    }
}

