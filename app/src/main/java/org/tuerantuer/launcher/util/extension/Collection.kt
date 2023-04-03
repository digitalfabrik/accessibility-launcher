package org.tuerantuer.launcher.util.extension

/**
 * @return true if the collection contains one element exactly.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> Collection<T>.isSingle(): Boolean = size == 1
