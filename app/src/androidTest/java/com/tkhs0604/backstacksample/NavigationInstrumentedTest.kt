package com.tkhs0604.backstacksample

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertContentEquals

class NavigationInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        // Given
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            MainNavHost(navController = navController, startDestination = "A")
        }
    }

    @Test
    fun verify_initial_back_stack_contains_only_start_destination() {
        // When
        // No-op

        // Then: Verify back stack's routes
        assertContentEquals(
            listOf("A"),
            navController.getBackStackRoutes()
        )
    }

    @Test
    fun verify_A_screen_is_stacked_twice_if_navigating_to_A_screen_from_A_screen() {
        // When: Navigating to A screen
        composeTestRule
            .onNodeWithText("Go to A")
            .performClick()

        // Then: Verify back stack's routes
        assertContentEquals(
            listOf("A", "A"),
            navController.getBackStackRoutes()
        )
    }

    @Test
    fun verify_A_screen_is_NOT_stacked_twice_if_navigating_to_A_screen_from_A_screen_with_launchSingleTop_option() {
        // When: Navigating to A screen with launchSingleTop option
        composeTestRule
            .onNodeWithText("Go to A (launchSingleTop)")
            .performClick()

        // Then: Verify back stack's routes
        assertContentEquals(
            listOf("A"),
            navController.getBackStackRoutes()
        )
    }

    @Test
    fun verify_all_screens_routes_are_popped_once_if_navigating_to_A_screen_again_with_popUpTo_and_inclusive_options() {
        // When: Navigating to B screen
        composeTestRule
            .onNodeWithText("Go to B")
            .performClick()
        // And: Navigating to C screen
        composeTestRule
            .onNodeWithText("Go to C")
            .performClick()

        // Then: Verify 1st back stack's routes
        assertContentEquals(
            listOf("A", "B", "C"),
            navController.getBackStackRoutes()
        )

        // =====

        // And: Navigating to A screen again with popUpTo / inclusive options
        composeTestRule
            .onNodeWithText("Go to A (popUpTo / inclusive)")
            .performClick()

        // Then: Verify 2nd back stack's routes
        assertContentEquals(
            listOf("A"),
            navController.getBackStackRoutes()
        )
    }

    @Test
    fun verify_A_screen_route_is_NOT_popped_if_navigating_to_A_screen_again_only_with_popUpTo_option() {
        // When: Navigating to B screen
        composeTestRule
            .onNodeWithText("Go to B")
            .performClick()
        // And: Navigating to C screen
        composeTestRule
            .onNodeWithText("Go to C")
            .performClick()

        // Then: Verify 1st back stack's routes
        assertContentEquals(
            listOf("A", "B", "C"),
            navController.getBackStackRoutes()
        )

        // =====

        // And: Navigating to A screen again only with popUpTo option
        composeTestRule
            .onNodeWithText("Go to A (popUpTo)")
            .performClick()

        // Then: Verify 2nd back stack's routes
        assertContentEquals(
            listOf("A", "A"),
            navController.getBackStackRoutes()
        )
    }

    @Test
    fun verify_A_screen_is_popped_if_returning_back_to_A_screen_without_inclusive_option() {
        // When: Navigating to B screen
        composeTestRule
            .onNodeWithText("Go to B")
            .performClick()
        // And: Navigating to C screen
        composeTestRule
            .onNodeWithText("Go to C")
            .performClick()

        // Then: Verify 1st back stack's routes
        assertContentEquals(
            listOf("A", "B", "C"),
            navController.getBackStackRoutes()
        )

        // =====

        // And: Returning back to A screen without inclusive option
        composeTestRule
            .onNodeWithText("Go back to A")
            .performClick()

        // Then: Verify 2nd back stack's routes
        assertContentEquals(
            listOf("A"),
            navController.getBackStackRoutes()
        )
    }

    @Test
    fun verify_no_screen_route_is_stacked_if_returning_back_to_A_screen_with_inclusive_option() {
        // When: Navigating to B screen
        composeTestRule
            .onNodeWithText("Go to B")
            .performClick()
        // And: Navigating to C screen
        composeTestRule
            .onNodeWithText("Go to C")
            .performClick()

        // Then: Verify 1st back stack's routes
        assertContentEquals(
            listOf("A", "B", "C"),
            navController.getBackStackRoutes()
        )

        // =====

        // And: Returning back to A screen with inclusive option
        composeTestRule
            .onNodeWithText("Go back to A (inclusive)")
            .performClick()

        // Then: Verify 2nd back stack's routes
        assertContentEquals(
            emptyList(),
            navController.getBackStackRoutes()
        )
    }

    private fun TestNavHostController.getBackStackRoutes(): List<String> {
        return backStack.mapNotNull { it.destination.route }
    }
}
