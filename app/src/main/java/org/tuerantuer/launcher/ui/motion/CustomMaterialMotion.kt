package org.tuerantuer.launcher.ui.motion

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * This component applies a transition animation (passed through [animationForStateTransition]) to
 * its [content].
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <S> CustomMaterialMotion(
    targetState: S,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    animationForStateTransition: (oldState: S, newState: S) -> MotionSpec?,
    content: @Composable (targetState: S) -> Unit,
) {
    val transition = updateTransition(targetState = targetState, label = "MaterialMotion")
    val animation = animationForStateTransition(transition.currentState, transition.targetState)
    if (animation == null) {
        // Don't animate screen
        Box(modifier = modifier, contentAlignment = contentAlignment) {
            content(targetState)
        }
    } else {
        transition.MaterialMotion(
            animation,
            modifier,
            pop = false,
            contentAlignment,
            content,
        )
    }
}
