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
    DisplayScale(parentPage = VisualAssistant),
    Wallpaper(parentPage = VisualAssistant),
    DisplayTimeout(parentPage = VisualAssistant),
    Notifications(parentPage = VisualAssistant),
    InputDelay(parentPage = VisualAssistant),
    HearingAssistant(parentPage = Assistant),
    NotificationSounds(parentPage = HearingAssistant),
    ScreenReader(parentPage = HearingAssistant),
    SpeechAssistant(parentPage = Assistant),
    VoiceCommands(parentPage = SpeechAssistant),
}
