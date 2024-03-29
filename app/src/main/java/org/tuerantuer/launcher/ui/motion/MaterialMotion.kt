/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("unused")

package org.tuerantuer.launcher.ui.motion

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

@ExperimentalAnimationApi
val sharedXMotionSpec = materialSharedAxisXIn() with materialSharedAxisXOut()

@ExperimentalAnimationApi
val sharedXMotionSpecReverse =
    materialSharedAxisXIn(-DefaultSlideDistance) with materialSharedAxisXOut(-DefaultSlideDistance)


/**
 * [MaterialMotion] allows to switch between two layouts with a material motion animation.
 *
 * @param targetState is a key representing your target layout state. Every time you change a key
 * the animation will be triggered. The [content] called with the old key will be faded out while
 * the [content] called with the new key will be faded in.
 * @param motionSpec the [MotionSpec] to configure the enter/exit animation.
 * @param modifier Modifier to be applied to the animation container.
 * @param pop whether motion contents are rendered in reverse order.
 */
@ExperimentalAnimationApi
@Composable
fun <S> MaterialMotion(
    targetState: S,
    motionSpec: MotionSpec,
    modifier: Modifier = Modifier,
    pop: Boolean = false,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable (targetState: S) -> Unit,
) {
    val transition = updateTransition(targetState = targetState, label = "MaterialMotion")
    transition.MaterialMotion(
        motionSpec,
        modifier,
        pop,
        contentAlignment,
        content,
    )
}

/**
 * [MaterialMotion] allows to switch between two layouts with a material motion animation.
 *
 * @param motionSpec the [MotionSpec] to configure the enter/exit animation.
 * @param modifier Modifier to be applied to the animation container.
 * @param pop whether motion contents are rendered in reverse order.
 */
@ExperimentalAnimationApi
@Composable
fun <S> Transition<S>.MaterialMotion(
    motionSpec: MotionSpec,
    modifier: Modifier = Modifier,
    pop: Boolean = false,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable (targetState: S) -> Unit,
) {
    val forward: Boolean = pop.not()
    val density: Density = LocalDensity.current
    AnimatedContent(
        modifier = modifier,
        transitionSpec = {
            (
                    motionSpec.enter.transition(forward, density) with motionSpec.exit.transition(
                        forward,
                        density,
                    )
                    )
                .apply {
                    // Show forward contents over the backward contents.
                    targetContentZIndex = if (forward) 0.1f else 0f
                }
        },
        contentAlignment = contentAlignment,
    ) { currentState ->
        val scaleModifier: Modifier
        if (currentState == targetState) {
            val scaleTransition = remember(motionSpec, forward) {
                motionSpec.enter.transitionExtra(forward)
            }
            scaleModifier = if (scaleTransition == ScaleTransition.None) {
                Modifier
            } else {
                with(scaleTransition) {
                    Modifier.animateModifier(transition)
                }
            }
        } else {
            val scaleTransition = remember(motionSpec, forward) {
                motionSpec.exit.transitionExtra(forward)
            }
            scaleModifier = if (scaleTransition == ScaleTransition.None) {
                Modifier
            } else {
                with(scaleTransition) {
                    Modifier.animateModifier(transition)
                }
            }
        }
        Box(modifier = Modifier.then(scaleModifier)) {
            content(currentState)
        }
    }
}

/**
 * [MaterialMotion] allows to switch between two layouts with a material motion animation.
 *
 * @param targetState is a key representing your target layout state. Every time you change a key
 * the animation will be triggered. The [content] called with the old key will be faded out while
 * the [content] called with the new key will be faded in.
 * @param enterMotionSpec the [MotionSpec] to configure the enter animation.
 * @param exitMotionSpec the [MotionSpec] to configure the exit animation.
 * @param modifier Modifier to be applied to the animation container.
 * @param pop whether motion contents are rendered in reverse order.
 */
@Deprecated(
    message = "Replaced with MaterialMotion()",
    replaceWith = ReplaceWith(
        expression = """MaterialMotion(
            targetState = targetState,
            motionSpec = enterMotionSpec with exitMotionSpec,
            modifier = modifier,
            pop = pop,
            content = content
        )""",
    ),
)
@ExperimentalAnimationApi
@Composable
fun <T> MaterialMotion(
    targetState: T,
    enterMotionSpec: EnterMotionSpec,
    exitMotionSpec: ExitMotionSpec,
    modifier: Modifier = Modifier,
    pop: Boolean = false,
    content: @Composable (T) -> Unit,
) {
    MaterialMotion(
        targetState = targetState,
        motionSpec = enterMotionSpec with exitMotionSpec,
        modifier = modifier,
        pop = pop,
        content = content,
    )
}
