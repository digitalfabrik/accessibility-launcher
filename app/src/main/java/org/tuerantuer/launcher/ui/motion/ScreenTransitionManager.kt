package org.tuerantuer.launcher.ui.motion

import androidx.compose.animation.ExperimentalAnimationApi
import org.tuerantuer.launcher.ui.data.ScreenState
import org.tuerantuer.launcher.ui.data.UiState

private const val ANIMATION_SCALE_START = 0.92f
private const val ANIMATION_SCALE_END = 1f / ANIMATION_SCALE_START

private const val ANIMATION_ALPHA_START = 0.0f
private const val ANIMATION_ALPHA_END = 0.0f

/**
 * This class handles all screen transitions and in-screen transitions. Because each screen has its
 * own [ScreenState], we determine the transition animation by looking at the last and current
 * [ScreenState].
 */
class ScreenTransitionManager {

    @ExperimentalAnimationApi
    val fadeThroughMotionSpec =
        materialElevationScaleIn(
            initialAlpha = ANIMATION_ALPHA_START,
            initialScale = ANIMATION_SCALE_START,
        ) with materialElevationScaleOut(
            targetAlpha = ANIMATION_ALPHA_END,
            targetScale = ANIMATION_SCALE_END,
        )

    @ExperimentalAnimationApi
    val fadeThroughMotionSpecReverse =
        materialElevationScaleIn(
            initialAlpha = ANIMATION_ALPHA_START,
            initialScale = ANIMATION_SCALE_END,
        ) with materialElevationScaleOut(
            targetAlpha = ANIMATION_ALPHA_END,
            targetScale = ANIMATION_SCALE_START,
        )

    @OptIn(ExperimentalAnimationApi::class)
    fun loadAnimationForUiStateTransition(oldState: UiState, newState: UiState): MotionSpec? {
        if (oldState.screenState == newState.screenState) {
            // Only settings or apps have Changed, will animate by itself
            return null
        }

        if (oldState.screenState.javaClass == newState.screenState.javaClass) {
            // Screen has changed, but it's the same type of screen, so we don't animate here, screen will animate by
            // itself
            return null
        }

        val depthComparison = getDepthLevelForState(newState)
            .compareTo(getDepthLevelForState(oldState))

        return if (depthComparison != 0) {
            if (depthComparison >= 0) fadeThroughMotionSpec else fadeThroughMotionSpecReverse
        } else {
            null
        }
    }

    /**
     * Each screen has its own depth level. It decides how we animate between two screens. Screens
     * with different depth level get a scale transition so it looks like they are stacked on top
     * of each other. On the other hand, screens with the same depth level get a horizontal
     * translation animation.
     */
    private fun getDepthLevelForState(uiState: UiState): Int = when (uiState.screenState) {
        ScreenState.LoadingState -> 0
        ScreenState.HomeScreenState -> 1
        is ScreenState.OnboardingState -> 2
        ScreenState.AllAppsScreenState -> 2
        ScreenState.EditFavoritesScreenState -> 2
        is ScreenState.SettingsState -> 2
    }
}
