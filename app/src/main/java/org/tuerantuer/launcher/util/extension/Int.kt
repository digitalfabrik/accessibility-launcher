package org.tuerantuer.launcher.util.extension

import kotlin.Int

/**
 * Returns the floored half of the current value. E.g., 10.half() = 5 and 7.half() = 3.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun Int.half(): Int = this / 2
