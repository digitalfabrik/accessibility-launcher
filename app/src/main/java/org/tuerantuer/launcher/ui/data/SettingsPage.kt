package org.tuerantuer.launcher.ui.data

/**
 * An enum class that represents the different pages of the onboarding screen.
 *
 * @author Peter Huber
 * Created on 10/04/2023
 */
enum class SettingsPage(val parentPage: SettingsPage?) {
    Overview(parentPage = null),
    SetDefaultLauncher(parentPage = Overview),
    SystemSettings(parentPage = Overview),
    ShareLauncher(parentPage = Overview),
    Feedback(parentPage = Overview),
    UninstallApps(parentPage = Overview),
    UninstallLauncher(parentPage = Overview),
    Assistant(parentPage = Overview),
    VisualAssistant(parentPage = Assistant),
    HearingAssistant(parentPage = Assistant),
    SpeechAssistant(parentPage = Assistant),
}
