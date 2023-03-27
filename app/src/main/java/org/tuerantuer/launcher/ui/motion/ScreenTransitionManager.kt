package org.tuerantuer.launcher.ui.motion

import androidx.compose.animation.ExperimentalAnimationApi
import org.tuerantuer.launcher.ui.UiState

private const val ANIMATION_SCALE_START = 0.92f
private const val ANIMATION_SCALE_END = 1f / ANIMATION_SCALE_START

private const val ANIMATION_ALPHA_START = 0.8f
private const val ANIMATION_ALPHA_END = 1f

/**
 * This class handles all screen transitions and in-screen transitions. Because each screen has its
 * own [UiState], we determine the transition animation by looking at the last and current
 * [UiState].
 *
 * @author Peter Huber
 */
class ScreenTransitionManager {

    @ExperimentalAnimationApi
    val sharedXMotionSpec = materialSharedAxisXIn() with materialSharedAxisXOut()

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

    //    TODO
    @OptIn(ExperimentalAnimationApi::class)
    fun loadAnimationForUiStateTransition(oldState: UiState, newState: UiState): MotionSpec? {
//        if (oldState.javaClass == newState.javaClass) {
//            // only animate transitions within the same screen for the answer question screen.
//            return if (newState is UiState.AnswerQuestions) {
//                oldState as UiState.AnswerQuestions
//                if (newState.roundQuestionIndex != oldState.roundQuestionIndex) {
//                    sharedXMotionSpec
//                } else {
//                    null
//                }
//            } else {
//                null
//            }
//        }
//
//        val depthComparison = getDepthLevelForState(newState)
//            .compareTo(getDepthLevelForState(oldState))
//
//        return if (depthComparison != 0) {
//            if (depthComparison >= 0) fadeThroughMotionSpec else fadeThroughMotionSpecReverse
//        } else {
//            sharedXMotionSpec
//        }
        return fadeThroughMotionSpec
    }

//    TODO
//    /**
//     * Each screen has its own depth level. It decides how we animate between two screens. Screens
//     * with different depth level get a scale transition so it looks like they are stacked on top
//     * of each other. On the other hand, screens with the same depth level get a horizontal
//     * translation animation.
//     */
//    private fun getDepthLevelForState(uiState: UiState): Int = when (uiState) {
//        is UiState.Onboarding -> 0
//        is UiState.SetPlayer.Grade -> 0
//        is UiState.SetPlayer.Name -> 0
//        is UiState.LoadHomeScreen -> 0
//        is UiState.SetPlayer.Subjects -> 0
//        is UiState.MainMenu -> 1
//        is UiState.MatchOverview -> 2
//        is UiState.SelectRoundSubject -> 3
//        is UiState.AnswerQuestions -> 4
//        is UiState.Settings -> 2
//    }
}
