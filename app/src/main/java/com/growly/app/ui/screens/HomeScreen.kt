package com.growly.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.growly.app.ui.components.GrowlyCard
import com.growly.app.ui.components.GrowlyQuickAccessCard
import com.growly.app.ui.theme.GrowlyColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: androidx.navigation.NavController? = null
) {
    // Sample tasks for today - in real app, this would come from ViewModel
    val todaysTasks = remember {
        listOf(
            "Review project proposal" to true,
            "Call dentist" to false,
            "Grocery shopping" to true,
            "Finish presentation" to false,
            "Team meeting" to true
        )
    }

    val completedTasks = todaysTasks.count { it.second }
    val totalTasks = todaysTasks.size
    val completionPercentage = if (totalTasks > 0) (completedTasks.toFloat() / totalTasks) else 0f
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Daily Motivation Quote
        item {
            GrowlyCard(
                backgroundColor = GrowlyColors.SkyBlue.copy(alpha = 0.1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Daily Motivation",
                        style = MaterialTheme.typography.titleMedium,
                        color = GrowlyColors.TextSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "\"The journey of a thousand miles begins with one step.\"",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                        color = GrowlyColors.TextPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "- Lao Tzu",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrowlyColors.TextSecondary,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }

        // Quick Access Cards
        item {
            Text(
                text = "Quick Access",
                style = MaterialTheme.typography.titleLarge,
                color = GrowlyColors.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(getQuickAccessItems()) { item ->
                    GrowlyQuickAccessCard(
                        title = item.title,
                        subtitle = item.subtitle,
                        icon = { Icon(item.icon, contentDescription = item.title, tint = item.color) },
                        onClick = {
                            when (item.title) {
                                "Start Journaling" -> navController?.navigate("journal")
                                "Focus Timer" -> navController?.navigate("focus")
                                "View Tasks" -> navController?.navigate("tasks")
                                "Profile" -> navController?.navigate("profile")
                            }
                        },
                        modifier = Modifier.width(160.dp),
                        accentColor = item.color
                    )
                }
            }
        }

        // Pomodoro Timer Widget
        item {
            Text(
                text = "Focus Timer",
                style = MaterialTheme.typography.titleLarge,
                color = GrowlyColors.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            GrowlyCard {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = 0.75f,
                            modifier = Modifier.fillMaxSize(),
                            color = GrowlyColors.LightMint,
                            strokeWidth = 8.dp
                        )
                        Text(
                            text = "18:45",
                            style = MaterialTheme.typography.headlineMedium,
                            color = GrowlyColors.TextPrimary
                        )
                    }
                    
                    Text(
                        text = "Work Session",
                        style = MaterialTheme.typography.titleMedium,
                        color = GrowlyColors.TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { /* TODO: Start/Pause timer */ },
                            colors = ButtonDefaults.buttonColors(containerColor = GrowlyColors.LightMint)
                        ) {
                            Text("Pause", color = GrowlyColors.DarkerGray)
                        }
                        
                        OutlinedButton(
                            onClick = { /* TODO: Reset timer */ }
                        ) {
                            Text("Reset", color = GrowlyColors.TextPrimary)
                        }
                    }
                }
            }
        }

        // Today's Tasks Overview
        item {
            Text(
                text = "Today's Tasks",
                style = MaterialTheme.typography.titleLarge,
                color = GrowlyColors.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            GrowlyCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$completedTasks of $totalTasks completed",
                            style = MaterialTheme.typography.titleMedium,
                            color = GrowlyColors.TextPrimary
                        )
                        Text(
                            text = "${(completionPercentage * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium,
                            color = GrowlyColors.LightMint
                        )
                    }

                    LinearProgressIndicator(
                        progress = completionPercentage,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        color = GrowlyColors.LightMint,
                        trackColor = GrowlyColors.LightGray
                    )

                    Column(
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        todaysTasks.take(3).forEach { (task, isCompleted) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (isCompleted) "✓" else "•",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isCompleted) GrowlyColors.LightMint else GrowlyColors.TextSecondary,
                                    modifier = Modifier.width(16.dp)
                                )
                                Text(
                                    text = task,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isCompleted) GrowlyColors.TextSecondary else GrowlyColors.TextPrimary,
                                    textDecoration = if (isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                                )
                            }
                        }
                        if (totalTasks > 3) {
                            Text(
                                text = "... and ${totalTasks - 3} more",
                                style = MaterialTheme.typography.bodySmall,
                                color = GrowlyColors.TextSecondary,
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Mood & Habit Summary
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Mood Summary
                GrowlyCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = GrowlyColors.BlushPink.copy(alpha = 0.1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Today's Mood",
                            style = MaterialTheme.typography.titleSmall,
                            color = GrowlyColors.TextSecondary
                        )
                        Text(
                            text = "😊",
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = "Happy",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrowlyColors.TextPrimary
                        )
                    }
                }
                
                // Habit Summary
                GrowlyCard(
                    modifier = Modifier.weight(1f),
                    backgroundColor = GrowlyColors.LightMint.copy(alpha = 0.1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Habits",
                            style = MaterialTheme.typography.titleSmall,
                            color = GrowlyColors.TextSecondary
                        )
                        Text(
                            text = "4/6",
                            style = MaterialTheme.typography.displaySmall,
                            color = GrowlyColors.TextPrimary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = "Completed",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrowlyColors.TextPrimary
                        )
                    }
                }
            }
        }
    }
}

data class QuickAccessItem(
    val title: String,
    val subtitle: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: androidx.compose.ui.graphics.Color
)

private fun getQuickAccessItems() = listOf(
    QuickAccessItem(
        title = "Daily Reflections",
        subtitle = "Today's thoughts",
        icon = Icons.Filled.Edit,
        color = GrowlyColors.SkyBlue
    ),
    QuickAccessItem(
        title = "Gratitude",
        subtitle = "What I'm grateful for",
        icon = Icons.Filled.Favorite,
        color = GrowlyColors.BlushPink
    ),
    QuickAccessItem(
        title = "Goals",
        subtitle = "Track progress",
        icon = Icons.Filled.Star,
        color = GrowlyColors.LightMint
    ),
    QuickAccessItem(
        title = "Free Writing",
        subtitle = "Express yourself",
        icon = Icons.Filled.Create,
        color = GrowlyColors.SkyBlue
    )
)
