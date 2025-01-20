package com.example.pokedex2024

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex2024.navigation.composableWithCompositionLocal
import com.example.pokedex2024.ui.theme.Pokedex2024Theme
import com.example.pokedex2024.utils.Transitions.LocalSharedTransitionScope
import com.example.pokedex2024.viewModel.PokemonViewModel
import com.example.pokedex2024.views.HomeView
import com.example.pokedex2024.views.PokedexDetailsView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: PokemonViewModel by viewModels()
        setContent {
            Pokedex2024Theme {
                val navController = rememberNavController()
                SharedTransitionLayout {
                    CompositionLocalProvider(
                        LocalSharedTransitionScope provides this
                    ) {
                        AnimatedVisibility(true, label = "") {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                NavHost(
                                    navController = navController,
                                    startDestination = "Home"
                                ) {
                                    composableWithCompositionLocal(
                                        route = "Home"
                                    ) { backStackEntry ->
                                        HomeView(
                                            viewModel, navController
                                        )
                                    }
                                    composableWithCompositionLocal(
                                        "PokedexDetailsView/{name}/?{imgUrl}",
                                        arguments = listOf(
                                            navArgument("name") { type = NavType.StringType },
                                            navArgument("imgUrl") { type = NavType.StringType }
                                        ),

                                        ) { backStackEntry ->
                                        val arguments = requireNotNull(backStackEntry.arguments)
                                        val name = arguments.getString("name") ?: ""
                                        val imgUrl = arguments.getString("imgUrl") ?: ""
                                        PokedexDetailsView(
                                            viewModel,
                                            navController,
                                            name,
                                            imgUrl
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}

