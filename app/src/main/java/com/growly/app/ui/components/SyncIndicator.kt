package com.growly.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.growly.app.ui.theme.GrowlyColors

@Composable
fun SyncIndicator(
    isSyncing: Boolean,
    isOffline: Boolean,
    syncMessage: String?,
    onRetrySync: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isSyncing && !isOffline && syncMessage == null) return
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isOffline -> GrowlyColors.BlushPink.copy(alpha = 0.1f)
                isSyncing -> GrowlyColors.SkyBlue.copy(alpha = 0.1f)
                else -> GrowlyColors.LightMint.copy(alpha = 0.1f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Status Icon
            when {
                isSyncing -> {
                    val rotation by rememberInfiniteTransition(label = "sync_rotation").animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        ), label = "sync_animation"
                    )
                    
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = "Syncing",
                        tint = GrowlyColors.SkyBlue,
                        modifier = Modifier
                            .size(16.dp)
                            .rotate(rotation)
                    )
                }
                isOffline -> {
                    Icon(
                        imageVector = Icons.Default.CloudOff,
                        contentDescription = "Offline",
                        tint = GrowlyColors.BlushPink,
                        modifier = Modifier.size(16.dp)
                    )
                }
                else -> {
                    Icon(
                        imageVector = Icons.Default.CloudDone,
                        contentDescription = "Synced",
                        tint = GrowlyColors.LightMint,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            // Status Text
            Text(
                text = when {
                    isSyncing -> "Syncing with cloud..."
                    isOffline -> "Offline - Data saved locally"
                    syncMessage != null -> syncMessage
                    else -> "Synced"
                },
                style = MaterialTheme.typography.bodySmall,
                color = when {
                    isOffline -> GrowlyColors.BlushPink
                    isSyncing -> GrowlyColors.SkyBlue
                    else -> GrowlyColors.LightMint
                },
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            
            // Retry Button (only when offline)
            if (isOffline) {
                TextButton(
                    onClick = onRetrySync,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Retry",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrowlyColors.SkyBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun CompactSyncIndicator(
    isSyncing: Boolean,
    isOffline: Boolean,
    modifier: Modifier = Modifier
) {
    if (!isSyncing && !isOffline) return
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when {
            isSyncing -> {
                val rotation by rememberInfiniteTransition(label = "compact_sync_rotation").animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ), label = "compact_sync_animation"
                )
                
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = "Syncing",
                    tint = GrowlyColors.SkyBlue,
                    modifier = Modifier
                        .size(12.dp)
                        .rotate(rotation)
                )
            }
            isOffline -> {
                Icon(
                    imageVector = Icons.Default.CloudOff,
                    contentDescription = "Offline",
                    tint = GrowlyColors.BlushPink,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
        
        Text(
            text = if (isSyncing) "Syncing..." else "Offline",
            style = MaterialTheme.typography.bodySmall,
            color = if (isSyncing) GrowlyColors.SkyBlue else GrowlyColors.BlushPink
        )
    }
}
