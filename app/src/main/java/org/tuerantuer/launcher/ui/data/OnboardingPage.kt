package org.tuerantuer.launcher.ui.data

/**
 * An enum class that represents the different pages of the onboarding screen.
 *
 * @author Peter Huber
 * Created on 04/04/2023
 */
enum class OnboardingPage(val pageNumber: Int) {
    INTRODUCTION_1(1),
    INTRODUCTION_2(2),
    INTRODUCTION_3(3),
    INTRODUCTION_4(4),
    PROGRESS_EXPLANATION_1(5),
    PROGRESS_EXPLANATION_2(6),
    PROGRESS_EXPLANATION_3(7),
    SET_AS_DEFAULT_1(8),
    SET_AS_DEFAULT_2(9),
    SET_AS_DEFAULT_3(10),
    PRIVACY_POLICY(11),
    TERMS_OF_SERVICE(12),
    SET_SIZE_INTRO(13),
    SET_SIZE_MAIN(14),
    SET_FAVORITES_INTRO_1(15),
    SET_FAVORITES_INTRO_2(16),
    SET_FAVORITES_INTRO_3(17),
    SET_FAVORITES_MAIN(18),
    SETUP_FINISHED_1(19),
    SETUP_FINISHED_2(20),
    SETUP_FINISHED_3(21),
}
