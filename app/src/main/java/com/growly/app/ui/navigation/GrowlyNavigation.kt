package com.growly.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.growly.app.R
import com.growly.app.ui.screens.*
import com.growly.app.data.entities.JournalCategory
import com.growly.app.ui.theme.GrowlyColors

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Journal : Screen("journal", "Journal", Icons.Filled.Create)
    object Tasks : Screen("tasks", "Tasks", Icons.Filled.CheckCircle)
    object Focus : Screen("focus", "Focus", Icons.Filled.PlayArrow)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)
    object Writing : Screen("writing/{categoryId}", "Writing", Icons.Filled.Create)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowlyNavigation() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Journal,
        Screen.Tasks,
        Screen.Focus,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = GrowlyColors.CardBackground,
                contentColor = GrowlyColors.SkyBlue
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { 
                            Icon(
                                screen.icon, 
                                contentDescription = screen.title,
                                tint = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
                                    GrowlyColors.SkyBlue
                                } else {
                                    GrowlyColors.MediumGray
                                }
                            ) 
                        },
                        label = { 
                            Text(
                                screen.title,
                                color = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
                                    GrowlyColors.SkyBlue
                                } else {
                                    GrowlyColors.MediumGray
                                }
                            ) 
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GrowlyColors.SkyBlue,
                            selectedTextColor = GrowlyColors.SkyBlue,
                            unselectedIconColor = GrowlyColors.MediumGray,
                            unselectedTextColor = GrowlyColors.MediumGray,
                            indicatorColor = GrowlyColors.SkyBlue.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController = navController) }
            composable(Screen.Journal.route) {
                JournalScreen(
                    onCategoryClick = { categoryItem ->
                        navController.navigate("writing/${categoryItem.category.name}")
                    }
                )
            }
            composable(Screen.Tasks.route) { TasksScreen() }
            composable(Screen.Focus.route) { FocusScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(
                route = "writing/{categoryId}",
                arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
            ) { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("categoryId") ?: ""
                val category = try {
                    JournalCategory.valueOf(categoryName)
                } catch (e: Exception) {
                    JournalCategory.DAILY_REFLECTIONS
                }

                WritingScreen(
                    category = category,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
