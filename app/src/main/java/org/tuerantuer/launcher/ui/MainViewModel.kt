package org.tuerantuer.launcher.ui

import androidx.annotation.RawRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import org.tuerantuer.launcher.app.AppItemInfo
import org.tuerantuer.launcher.data.datastore.AppIconSize
import org.tuerantuer.launcher.data.datastore.WallpaperType
import org.tuerantuer.launcher.ui.data.SettingsPage
import org.tuerantuer.launcher.ui.data.UiState

/**
 * View Model of [MainActivity]. This class is not an extension of [ViewModel] because the [ViewModel]s lifecycle is
 * broken with launchers (doing the home gesture the first time recreates the view model). Since our [MainActivity]
 * lives as long as the launcher is running if the launcher is set as default, it's okay that our [MainViewModelImpl] lives
 * as long as the launcher is running.
 */
interface MainViewModel {
    /**
     * Determines the current state ui. Each subclass of [UiState] represents a different screen in
     * the app. You can subscribe to this [StateFlow] to be notified when the underlying [UiState]
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
    fun onOpenOnboarding()
    fun openSettingsPage(settingsPage: SettingsPage)

    fun onSetDefaultLauncher()
    fun onGoToNextOnboardingStep()
    fun onShareLauncher()
    fun onOpenSystemSettings()
    fun onOpenAccessibilitySettings()
    fun onOpenDisplaySettings()
    fun onOpenSoundSettings()
    fun onUninstallLauncher()
    fun onWriteFeedbackMail()
    fun cancelOnboarding()

    suspend fun onSetFavorites(newFavorites: List<AppItemInfo>)
    suspend fun onSetIconSize(appIconSize: AppIconSize)
    suspend fun onSetWallpaperType(wallpaperType: WallpaperType)
    suspend fun onSetWallpaper(@RawRes wallpaperRes: Int)
}
