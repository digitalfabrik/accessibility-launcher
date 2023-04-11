package org.tuerantuer.launcher.ui.data

/**
 * An enum class that represents the different pages of the onboarding screen.
 *
 * @author Peter Huber
 * Created on 04/04/2023
 */
enum class OnboardingPage(val pageNumber: Int) {
    SCREEN_1(1),
    SCREEN_2(2),
    SCREEN_3(3),
    SCREEN_4(4),
    SCREEN_5(5),
    SCREEN_6(6),
    SCREEN_7(7),
    SCREEN_8(8),
    SCREEN_9(9),
    SCREEN_SET_DEFAULT_LAUNCHER(10),
    SCREEN_PRIVACY_POLICY(11),
    SCREEN_TERMS_OF_SERVICE(12),
    SCREEN_SET_SIZE_INTRO(13),
    SCREEN_SET_SIZE_MAIN(14),
    SCREEN_SET_FAVORITES_INTRO_1(15),
    SCREEN_SET_FAVORITES_INTRO_2(16),
    SCREEN_SET_FAVORITES_INTRO_3(17),
    SCREEN_SET_FAVORITES_MAIN(18),
    SCREEN_SETUP_FINISHED_1(19),
    SCREEN_SETUP_FINISHED_2(20),
    SCREEN_SETUP_FINISHED_3(21),
    LAST_PAGE(21),
}
