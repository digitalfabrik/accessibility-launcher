package org.tuerantuer.launcher.itemInfo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * TODO: add description
 *
 * @author Peter Huber
 * Created on 07/03/2023
 */
// TODO: Remove
@Parcelize
data class Key(val packageName: String, val className: String, val user: Long) : Parcelable
