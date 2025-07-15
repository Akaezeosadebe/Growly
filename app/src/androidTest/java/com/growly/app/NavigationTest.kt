package com.growly.app

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.growly.app.ui.navigation.GrowlyNavigation
import com.growly.app.ui.theme.GrowlyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun navigationBar_displaysAllTabs() {
        composeTestRule.setContent {
            GrowlyTheme {
                GrowlyNavigation()
            }
        }

        // Verify all navigation tabs are displayed
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Journal").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tasks").assertIsDisplayed()
        composeTestRule.onNodeWithText("Focus").assertIsDisplayed()
        composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
    }

    @Test
    fun navigationBar_homeTabSelectedByDefault() {
        composeTestRule.setContent {
            GrowlyTheme {
                GrowlyNavigation()
            }
        }

        // Verify Home tab is selected by default
        composeTestRule.onNodeWithText("Home").assertIsSelected()
    }

    @Test
    fun navigationBar_canNavigateBetweenTabs() {
        composeTestRule.setContent {
            GrowlyTheme {
                GrowlyNavigation()
            }
        }

        // Navigate to Journal tab
        composeTestRule.onNodeWithText("Journal").performClick()
        composeTestRule.onNodeWithText("Journal").assertIsSelected()

        // Navigate to Tasks tab
        composeTestRule.onNodeWithText("Tasks").performClick()
        composeTestRule.onNodeWithText("Tasks").assertIsSelected()

        // Navigate to Focus tab
        composeTestRule.onNodeWithText("Focus").performClick()
        composeTestRule.onNodeWithText("Focus").assertIsSelected()

        // Navigate to Profile tab
        composeTestRule.onNodeWithText("Profile").performClick()
        composeTestRule.onNodeWithText("Profile").assertIsSelected()
    }

    @Test
    fun homeScreen_displaysMainComponents() {
        composeTestRule.setContent {
            GrowlyTheme {
                GrowlyNavigation()
            }
        }

        // Verify main components are displayed on home screen
        composeTestRule.onNodeWithText("Daily Motivation").assertIsDisplayed()
        composeTestRule.onNodeWithText("Quick Access").assertIsDisplayed()
        composeTestRule.onNodeWithText("Focus Timer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Today's Tasks").assertIsDisplayed()
    }

    @Test
    fun journalScreen_displaysCategories() {
        composeTestRule.setContent {
            GrowlyTheme {
                GrowlyNavigation()
            }
        }

        // Navigate to Journal tab
        composeTestRule.onNodeWithText("Journal").performClick()

        // Verify journal categories are displayed
        composeTestRule.onNodeWithText("Daily Reflections").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gratitude Journaling").assertIsDisplayed()
        composeTestRule.onNodeWithText("Goal Tracking").assertIsDisplayed()
        composeTestRule.onNodeWithText("Free Writing").assertIsDisplayed()
        composeTestRule.onNodeWithText("Custom Prompts").assertIsDisplayed()
    }

    @Test
    fun tasksScreen_displaysAddButton() {
        composeTestRule.setContent {
            GrowlyTheme {
                GrowlyNavigation()
            }
        }

        // Navigate to Tasks tab
        composeTestRule.onNodeWithText("Tasks").performClick()

        // Verify add task button is displayed
        composeTestRule.onNodeWithContentDescription("Add Task").assertIsDisplayed()
    }

    @Test
    fun focusScreen_displaysPomodoroTimer() {
        composeTestRule.setContent {
            GrowlyTheme {
                GrowlyNavigation()
            }
        }

        // Navigate to Focus tab
        composeTestRule.onNodeWithText("Focus").performClick()

        // Verify Pomodoro timer components are displayed
        composeTestRule.onNodeWithText("Work Session").assertIsDisplayed()
        composeTestRule.onNodeWithText("Start").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reset").assertIsDisplayed()
    }

    @Test
    fun profileScreen_displaysUserInfo() {
        composeTestRule.setContent {
            GrowlyTheme {
                GrowlyNavigation()
            }
        }

        // Navigate to Profile tab
        composeTestRule.onNodeWithText("Profile").performClick()

        // Verify profile components are displayed
        composeTestRule.onNodeWithText("Welcome back!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Current Streak").assertIsDisplayed()
        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
    }
}
