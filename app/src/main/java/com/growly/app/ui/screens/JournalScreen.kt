package com.growly.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.growly.app.data.entities.JournalCategory
import com.growly.app.ui.components.GrowlyCard
import com.growly.app.ui.components.SyncIndicator
import com.growly.app.ui.theme.GrowlyColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    onCategoryClick: (JournalCategoryItem) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Journal",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                color = GrowlyColors.TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Capture your thoughts, track your growth, and reflect on your journey.",
                style = MaterialTheme.typography.bodyLarge,
                color = GrowlyColors.TextSecondary
            )
        }

        // Demo Sync Indicator
        item {
            var demoSyncState by remember { mutableStateOf(0) } // 0=synced, 1=syncing, 2=offline

            SyncIndicator(
                isSyncing = demoSyncState == 1,
                isOffline = demoSyncState == 2,
                syncMessage = when (demoSyncState) {
                    0 -> "All journal entries synced"
                    else -> null
                },
                onRetrySync = { demoSyncState = 1 }
            )

            // Demo controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = { demoSyncState = 0 }) {
                    Text("Synced", style = MaterialTheme.typography.bodySmall)
                }
                TextButton(onClick = { demoSyncState = 1 }) {
                    Text("Syncing", style = MaterialTheme.typography.bodySmall)
                }
                TextButton(onClick = { demoSyncState = 2 }) {
                    Text("Offline", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        items(getJournalCategories()) { category ->
            JournalCategoryCard(
                category = category,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

@Composable
fun JournalCategoryCard(
    category: JournalCategoryItem,
    onClick: () -> Unit
) {
    GrowlyCard(
        onClick = onClick,
        backgroundColor = category.backgroundColor
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.title,
                    tint = category.iconColor,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = GrowlyColors.TextPrimary
                )
                
                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrowlyColors.TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                if (category.lastEntry.isNotEmpty()) {
                    Text(
                        text = "Last entry: ${category.lastEntry}",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrowlyColors.TextSecondary.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Open",
                tint = GrowlyColors.TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

data class JournalCategoryItem(
    val category: JournalCategory,
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val iconColor: androidx.compose.ui.graphics.Color,
    val backgroundColor: androidx.compose.ui.graphics.Color,
    val lastEntry: String = ""
)

fun getJournalCategories() = listOf(
    JournalCategoryItem(
        category = JournalCategory.DAILY_REFLECTIONS,
        title = "Daily Reflections",
        description = "Reflect on your day, thoughts, and experiences",
        icon = Icons.Filled.Star,
        iconColor = GrowlyColors.SkyBlue,
        backgroundColor = GrowlyColors.SkyBlue.copy(alpha = 0.05f),
        lastEntry = "2 hours ago"
    ),
    JournalCategoryItem(
        category = JournalCategory.GRATITUDE_JOURNALING,
        title = "Gratitude Journaling",
        description = "Write about what you're grateful for today",
        icon = Icons.Filled.Favorite,
        iconColor = GrowlyColors.BlushPink,
        backgroundColor = GrowlyColors.BlushPink.copy(alpha = 0.05f),
        lastEntry = "Yesterday"
    ),
    JournalCategoryItem(
        category = JournalCategory.GOAL_TRACKING,
        title = "Goal Tracking",
        description = "Track your progress towards your goals",
        icon = Icons.Filled.Star,
        iconColor = GrowlyColors.LightMint,
        backgroundColor = GrowlyColors.LightMint.copy(alpha = 0.05f),
        lastEntry = "3 days ago"
    ),
    JournalCategoryItem(
        category = JournalCategory.FREE_WRITING,
        title = "Free Writing",
        description = "Let your thoughts flow freely without constraints",
        icon = Icons.Filled.Create,
        iconColor = GrowlyColors.SkyBlue,
        backgroundColor = GrowlyColors.SkyBlue.copy(alpha = 0.05f),
        lastEntry = ""
    ),
    JournalCategoryItem(
        category = JournalCategory.CUSTOM_PROMPTS,
        title = "Custom Prompts",
        description = "Explore guided writing prompts and exercises",
        icon = Icons.Filled.Star,
        iconColor = GrowlyColors.BlushPink,
        backgroundColor = GrowlyColors.BlushPink.copy(alpha = 0.05f),
        lastEntry = "1 week ago"
    )
)
