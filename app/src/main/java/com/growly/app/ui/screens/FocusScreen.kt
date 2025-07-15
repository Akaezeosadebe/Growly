package com.growly.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.growly.app.ui.components.GrowlyCard
import com.growly.app.ui.components.GrowlyPrimaryButton
import com.growly.app.ui.components.GrowlySecondaryButton
import com.growly.app.ui.theme.GrowlyColors
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen() {
    var isRunning by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableStateOf("25:00") }
    var currentSession by remember { mutableStateOf("Work Session") }
    var progress by remember { mutableStateOf(0.0f) }
    var totalSeconds by remember { mutableStateOf(25 * 60) } // 25 minutes in seconds
    var remainingSeconds by remember { mutableStateOf(25 * 60) }

    // Timer logic
    LaunchedEffect(isRunning) {
        while (isRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds--
            progress = 1f - (remainingSeconds.toFloat() / totalSeconds.toFloat())

            val minutes = remainingSeconds / 60
            val seconds = remainingSeconds % 60
            timeRemaining = String.format("%02d:%02d", minutes, seconds)
        }

        if (remainingSeconds <= 0) {
            isRunning = false
            // Timer completed - could add notification here
        }
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = "Focus",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                color = GrowlyColors.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Use the Pomodoro Technique to boost your productivity and maintain focus.",
                style = MaterialTheme.typography.bodyLarge,
                color = GrowlyColors.TextSecondary
            )
        }

        // Main Pomodoro Timer
        item {
            GrowlyCard(
                backgroundColor = GrowlyColors.LightMint.copy(alpha = 0.05f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = currentSession,
                        style = MaterialTheme.typography.titleLarge,
                        color = GrowlyColors.TextPrimary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = progress,
                            modifier = Modifier.fillMaxSize(),
                            color = if (currentSession.contains("Work")) GrowlyColors.LightMint else GrowlyColors.BlushPink,
                            strokeWidth = 12.dp,
                            trackColor = GrowlyColors.LightGray
                        )
                        
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = timeRemaining,
                                style = MaterialTheme.typography.displayMedium,
                                color = GrowlyColors.TextPrimary
                            )
                            Text(
                                text = if (isRunning) "Running" else "Paused",
                                style = MaterialTheme.typography.bodyMedium,
                                color = GrowlyColors.TextSecondary
                            )
                        }
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        GrowlyPrimaryButton(
                            text = if (isRunning) "Pause" else "Start",
                            onClick = { isRunning = !isRunning },
                            backgroundColor = if (isRunning) GrowlyColors.BlushPink else GrowlyColors.LightMint,
                            modifier = Modifier.weight(1f)
                        )
                        
                        GrowlySecondaryButton(
                            text = "Reset",
                            onClick = {
                                isRunning = false
                                remainingSeconds = totalSeconds
                                timeRemaining = "25:00"
                                progress = 0.0f
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Timer Settings
        item {
            Text(
                text = "Timer Settings",
                style = MaterialTheme.typography.titleLarge,
                color = GrowlyColors.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            GrowlyCard {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TimerSettingRow(
                        label = "Work Duration",
                        value = "25 min",
                        onEdit = { /* TODO: Edit work duration */ }
                    )
                    
                    TimerSettingRow(
                        label = "Short Break",
                        value = "5 min",
                        onEdit = { /* TODO: Edit short break */ }
                    )
                    
                    TimerSettingRow(
                        label = "Long Break",
                        value = "15 min",
                        onEdit = { /* TODO: Edit long break */ }
                    )
                    
                    TimerSettingRow(
                        label = "Cycles before Long Break",
                        value = "4",
                        onEdit = { /* TODO: Edit cycles */ }
                    )
                }
            }
        }

        // Focus Tips
        item {
            Text(
                text = "Focus Tips",
                style = MaterialTheme.typography.titleLarge,
                color = GrowlyColors.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            GrowlyCard(
                backgroundColor = GrowlyColors.SkyBlue.copy(alpha = 0.05f)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FocusTip(
                        icon = Icons.Filled.Phone,
                        tip = "Turn off notifications during focus sessions"
                    )

                    FocusTip(
                        icon = Icons.Filled.Star,
                        tip = "Stay hydrated - keep water nearby"
                    )

                    FocusTip(
                        icon = Icons.Filled.Favorite,
                        tip = "Take deep breaths during breaks"
                    )

                    FocusTip(
                        icon = Icons.Filled.Star,
                        tip = "Choose one task per focus session"
                    )
                }
            }
        }

        // Today's Focus Stats
        item {
            Text(
                text = "Today's Progress",
                style = MaterialTheme.typography.titleLarge,
                color = GrowlyColors.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FocusStatCard(
                    title = "Sessions",
                    value = "3",
                    subtitle = "Completed",
                    color = GrowlyColors.LightMint,
                    modifier = Modifier.weight(1f)
                )
                
                FocusStatCard(
                    title = "Focus Time",
                    value = "75",
                    subtitle = "Minutes",
                    color = GrowlyColors.SkyBlue,
                    modifier = Modifier.weight(1f)
                )
                
                FocusStatCard(
                    title = "Streak",
                    value = "7",
                    subtitle = "Days",
                    color = GrowlyColors.BlushPink,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TimerSettingRow(
    label: String,
    value: String,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = GrowlyColors.TextPrimary
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = GrowlyColors.SkyBlue
            )
            
            IconButton(
                onClick = onEdit,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = GrowlyColors.TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun FocusTip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tip: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GrowlyColors.SkyBlue,
            modifier = Modifier.size(20.dp)
        )
        
        Text(
            text = tip,
            style = MaterialTheme.typography.bodyMedium,
            color = GrowlyColors.TextPrimary
        )
    }
}

@Composable
fun FocusStatCard(
    title: String,
    value: String,
    subtitle: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    GrowlyCard(
        modifier = modifier,
        backgroundColor = color.copy(alpha = 0.1f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = GrowlyColors.TextSecondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = color,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = GrowlyColors.TextSecondary
            )
        }
    }
}
