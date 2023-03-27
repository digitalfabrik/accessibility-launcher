package org.tuerantuer.launcher.util.extension

import kotlin.collections.Collection

/**
 * @return true if the collection contains one element exactly.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> Collection<T>.isSingle(): Boolean = size == 1

/**
 * Looping in reverse lets you remove items in the list while looping without raising a
 * [ConcurrentModificationException].
 */
fun <T> List<T>.forEachReversed(action: (T) -> Unit) {
    if (isEmpty()) return
    for (i in indices.reversed()) {
        val t = this[i]
        action(t)
    }
}
