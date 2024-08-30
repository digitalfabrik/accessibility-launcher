package org.tuerantuer.launcher.ui.data

/**
 * An enum class that represents the different pages of the onboarding screen.
 */
enum class OnboardingPage {
    INTRODUCTION_1,
    INTRODUCTION_2,
    INTRODUCTION_3,
    INTRODUCTION_4,
    PROGRESS_EXPLANATION_1,
    PROGRESS_EXPLANATION_2,
    PROGRESS_EXPLANATION_3,
    SETTINGS_INTRODUCTION,
    SET_AS_DEFAULT_1,
    SET_AS_DEFAULT_2,
    SET_AS_DEFAULT_3,
    PRIVACY_POLICY,
    SET_SIZE_INTRO,
    SET_SIZE_ICONS,
    SET_SIZE_TEXT,
    SET_FAVORITES_INTRO_1,
    SET_FAVORITES_INTRO_2,
    SET_FAVORITES_INTRO_3,
    SET_FAVORITES_MAIN,
    SCROLL_BEHAVIOR_INTRO,
    SET_SCROLL_BEHAVIOR,
    SETUP_FINISHED_1,
    SETUP_FINISHED_2,
    SETUP_FINISHED_3;

    val pageNumber: Int = ordinal + 1
}
