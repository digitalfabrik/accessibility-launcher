package org.tuerantuer.launcher.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import org.tuerantuer.launcher.data.AppIconSize
import org.tuerantuer.launcher.itemInfo.AppItemInfo

/**
 * View Model of [MainActivity]. This class is not an extension of [ViewModel] because the [ViewModel]s lifecycle is
 * broken with launchers (doing the home gesture the first time recreates the view model). Since our [MainActivity]
 * lives as long as the launcher is running if the launcher is set as default, it's okay that our [MainViewModelImpl] lives
 * as long as the launcher is running.
 *
 * @author Peter Huber
 */
interface MainViewModel {
    /**
     * Determines the current state ui. Each subclass of [ScreenState] represents a different screen in
     * the app. You can subscribe to this [Flow] to be notified when the underlying [ScreenState]
     * changes.
     */
    val uiState: StateFlow<UiState>

    /**
     * @return true if pressing back event was handled and should not be propagated further.
     */
    fun goBack(): Boolean

    fun openApp(appItemInfo: AppItemInfo)
    fun onEditFavorites()
    fun onShowAllApps()
    fun onOpenSettings()
    fun onOpenOnboarding()

    fun onSetDefaultLauncher()
    fun onGoToNextOnboardingStep()
    fun onShareLauncher()
    fun onOpenSystemSettings()
    fun onUninstallLauncher()
    fun cancelOnboarding()

    suspend fun onSetFavorites(newFavorites: List<AppItemInfo>)
    suspend fun onSetIconSize(appIconSize: AppIconSize)
}
