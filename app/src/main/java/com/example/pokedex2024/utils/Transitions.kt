package com.example.pokedex2024.utils

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

object Transitions {

    @OptIn(ExperimentalSharedTransitionApi::class)
    val snackDetailBoundsTransform = BoundsTransform { _, _ ->
        spatialExpressiveSpring()
    }

    fun <T> spatialExpressiveSpring() = spring<T>(
        dampingRatio = 0.8f,
        stiffness = 380f
    )

    fun <T> nonSpatialExpressiveSpring() = spring<T>(
        dampingRatio = 1f,
        stiffness = 1600f
    )


    val boundsTransform = { _: Rect, _: Rect -> tween<Rect>(550) }

    @OptIn(ExperimentalSharedTransitionApi::class)
    val ParentClip: SharedTransitionScope.OverlayClip =
        object : SharedTransitionScope.OverlayClip {
            override fun getClipPath(
                state: SharedTransitionScope.SharedContentState,
                bounds: Rect,
                layoutDirection: LayoutDirection,
                density: Density,
            ): Path? {
                return state.parentSharedContentState?.clipPathInOverlay
            }
        }

    val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

    @OptIn(ExperimentalSharedTransitionApi::class)
    val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

}