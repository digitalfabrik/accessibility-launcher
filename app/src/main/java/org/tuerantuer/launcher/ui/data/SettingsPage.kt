package org.tuerantuer.launcher.ui.data

/**
 * An enum class that represents the different pages of the onboarding screen.
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
    Licenses(parentPage = Overview),
    LicensesApache20(parentPage = Licenses),
    VisualAssistant(parentPage = Assistant),
    IconSize(parentPage = VisualAssistant),
    TextSize(parentPage = VisualAssistant),
    Wallpaper(parentPage = VisualAssistant),
    DisplayTimeout(parentPage = VisualAssistant),
    Notifications(parentPage = VisualAssistant),
    InputDelay(parentPage = VisualAssistant),
    ScrollBehavior(parentPage = VisualAssistant),
    HearingAssistant(parentPage = Assistant),
    NotificationSounds(parentPage = HearingAssistant),
    ScreenReader(parentPage = HearingAssistant),
    SpeechAssistant(parentPage = Assistant),
    VoiceCommands(parentPage = SpeechAssistant),
}
