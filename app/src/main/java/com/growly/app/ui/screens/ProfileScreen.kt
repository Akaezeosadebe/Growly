package com.growly.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.growly.app.ui.components.GrowlyCard
import com.growly.app.ui.theme.GrowlyColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Profile Header
        item {
            GrowlyCard(
                backgroundColor = GrowlyColors.SkyBlue.copy(alpha = 0.05f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Profile Picture Placeholder
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            colors = CardDefaults.cardColors(containerColor = GrowlyColors.SkyBlue.copy(alpha = 0.2f))
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.Person,
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier.size(40.dp),
                                    tint = GrowlyColors.SkyBlue
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Welcome back!",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                        color = GrowlyColors.TextPrimary
                    )
                    
                    Text(
                        text = "Keep up the great work on your journey",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrowlyColors.TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Current Streak
        item {
            Text(
                text = "Your Progress",
                style = MaterialTheme.typography.titleLarge,
                color = GrowlyColors.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            GrowlyCard(
                backgroundColor = GrowlyColors.LightMint.copy(alpha = 0.05f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Current Streak",
                            style = MaterialTheme.typography.titleMedium,
                            color = GrowlyColors.TextPrimary
                        )
                        Text(
                            text = "Keep journaling daily to maintain your streak!",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrowlyColors.TextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "12",
                            style = MaterialTheme.typography.displayMedium,
                            color = GrowlyColors.LightMint
                        )
                        Text(
                            text = "days",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrowlyColors.TextSecondary
                        )
                    }
                }
            }
        }

        // Settings Section
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                color = GrowlyColors.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            GrowlyCard {
                Column {
                    SettingsItem(
                        icon = Icons.Filled.Notifications,
                        title = "Notifications",
                        subtitle = "Daily reminders and alerts",
                        onClick = { /* TODO: Navigate to notifications settings */ }
                    )
                    
                    Divider(
                        color = GrowlyColors.LightGray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    SettingsItem(
                        icon = Icons.Filled.Settings,
                        title = "Theme Customization",
                        subtitle = "Personalize your app appearance",
                        onClick = { /* TODO: Navigate to theme settings */ }
                    )
                    
                    Divider(
                        color = GrowlyColors.LightGray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    SettingsItem(
                        icon = Icons.Filled.Edit,
                        title = "Writing Preferences",
                        subtitle = "Font, background, and writing environment",
                        onClick = { /* TODO: Navigate to writing settings */ }
                    )
                    
                    Divider(
                        color = GrowlyColors.LightGray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    SettingsItem(
                        icon = Icons.Filled.Star,
                        title = "Data & Backup",
                        subtitle = "Export and backup your entries",
                        onClick = { /* TODO: Navigate to backup settings */ }
                    )
                }
            }
        }

        // Statistics Overview
        item {
            Text(
                text = "Your Statistics",
                style = MaterialTheme.typography.titleLarge,
                color = GrowlyColors.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Entries",
                    value = "47",
                    subtitle = "Total written",
                    color = GrowlyColors.SkyBlue,
                    modifier = Modifier.weight(1f)
                )
                
                StatCard(
                    title = "Words",
                    value = "12.5K",
                    subtitle = "Total written",
                    color = GrowlyColors.BlushPink,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Tasks",
                    value = "89%",
                    subtitle = "Completion rate",
                    color = GrowlyColors.LightMint,
                    modifier = Modifier.weight(1f)
                )
                
                StatCard(
                    title = "Focus",
                    value = "24h",
                    subtitle = "This month",
                    color = GrowlyColors.SkyBlue,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Account Actions
        item {
            GrowlyCard {
                Column {
                    SettingsItem(
                        icon = Icons.Filled.Star,
                        title = "Help & Support",
                        subtitle = "Get help and contact support",
                        onClick = { /* TODO: Navigate to help */ }
                    )
                    
                    Divider(
                        color = GrowlyColors.LightGray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    SettingsItem(
                        icon = Icons.Filled.Info,
                        title = "About Growly",
                        subtitle = "Version 1.0.0",
                        onClick = { /* TODO: Show about dialog */ }
                    )
                    
                    Divider(
                        color = GrowlyColors.LightGray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    SettingsItem(
                        icon = Icons.Filled.ExitToApp,
                        title = "Log Out",
                        subtitle = "Sign out of your account",
                        onClick = { /* TODO: Handle logout */ },
                        textColor = GrowlyColors.Error
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    textColor: androidx.compose.ui.graphics.Color = GrowlyColors.TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = textColor,
            modifier = Modifier.size(24.dp)
        )
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = GrowlyColors.TextSecondary
            )
        }
        
        IconButton(onClick = onClick) {
            Icon(
                Icons.Filled.ArrowForward,
                contentDescription = "Open",
                tint = GrowlyColors.TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun StatCard(
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
