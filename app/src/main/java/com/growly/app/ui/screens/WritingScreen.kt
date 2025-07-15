package com.growly.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.growly.app.data.entities.JournalCategory
import com.growly.app.ui.theme.GrowlyColors
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritingScreen(
    category: JournalCategory,
    onNavigateBack: () -> Unit,
    onSave: (String, String) -> Unit = { _, _ -> }
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var wordCount by remember { mutableStateOf(0) }
    var isAutoSaved by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    
    // Auto-save functionality
    LaunchedEffect(content) {
        if (content.isNotEmpty()) {
            delay(2000) // Auto-save after 2 seconds of inactivity
            onSave(title, content)
            isAutoSaved = true
            delay(2000)
            isAutoSaved = false
        }
    }
    
    // Update word count
    LaunchedEffect(content) {
        wordCount = if (content.isBlank()) 0 else content.trim().split("\\s+".toRegex()).size
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Navigation Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = GrowlyColors.TextPrimary
                )
            }
            
            Text(
                text = getCategoryDisplayName(category),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                color = GrowlyColors.TextPrimary
            )
            
            Row {
                // Auto-save indicator
                if (isAutoSaved) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "Auto-saved",
                            tint = GrowlyColors.LightMint,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Saved",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrowlyColors.LightMint
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                IconButton(onClick = { showSettings = true }) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = GrowlyColors.TextSecondary
                    )
                }
                
                IconButton(onClick = { onSave(title, content) }) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = "Save",
                        tint = GrowlyColors.SkyBlue
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Title Input
        BasicTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.headlineSmall.copy(
                color = GrowlyColors.TextPrimary,
                fontWeight = FontWeight.Medium
            ),
            cursorBrush = SolidColor(GrowlyColors.SkyBlue),
            decorationBox = { innerTextField ->
                if (title.isEmpty()) {
                    Text(
                        text = "Enter a title...",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = GrowlyColors.TextSecondary.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                innerTextField()
            }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Content Input - Distraction-free writing space
        BasicTextField(
            value = content,
            onValueChange = { content = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = GrowlyColors.TextPrimary,
                lineHeight = 28.sp
            ),
            cursorBrush = SolidColor(GrowlyColors.SkyBlue),
            decorationBox = { innerTextField ->
                if (content.isEmpty()) {
                    Text(
                        text = getPlaceholderText(category),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = GrowlyColors.TextSecondary.copy(alpha = 0.6f),
                            lineHeight = 28.sp
                        )
                    )
                }
                innerTextField()
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Word Count and Status Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Words: $wordCount",
                style = MaterialTheme.typography.bodyMedium,
                color = GrowlyColors.TextSecondary
            )
            
            Text(
                text = getCurrentDateTime(),
                style = MaterialTheme.typography.bodySmall,
                color = GrowlyColors.TextSecondary
            )
        }
    }
    
    // Writing Settings Dialog
    if (showSettings) {
        WritingSettingsDialog(
            onDismiss = { showSettings = false },
            onApplySettings = { settings ->
                // TODO: Apply writing environment settings
                showSettings = false
            }
        )
    }
}

@Composable
fun WritingSettingsDialog(
    onDismiss: () -> Unit,
    onApplySettings: (WritingSettings) -> Unit
) {
    var fontSize by remember { mutableStateOf(16) }
    var backgroundTexture by remember { mutableStateOf("None") }
    var ambientSoundEnabled by remember { mutableStateOf(false) }
    var ambientSoundType by remember { mutableStateOf("Nature") }
    var ambientSoundVolume by remember { mutableStateOf(0.5f) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Writing Environment",
                style = MaterialTheme.typography.titleLarge,
                color = GrowlyColors.TextPrimary
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Font Size
                Column {
                    Text(
                        text = "Font Size: ${fontSize}sp",
                        style = MaterialTheme.typography.titleMedium,
                        color = GrowlyColors.TextPrimary
                    )
                    Slider(
                        value = fontSize.toFloat(),
                        onValueChange = { fontSize = it.toInt() },
                        valueRange = 12f..24f,
                        steps = 11,
                        colors = SliderDefaults.colors(
                            thumbColor = GrowlyColors.SkyBlue,
                            activeTrackColor = GrowlyColors.SkyBlue
                        )
                    )
                }
                
                // Background Texture
                Column {
                    Text(
                        text = "Background Texture",
                        style = MaterialTheme.typography.titleMedium,
                        color = GrowlyColors.TextPrimary
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("None", "Paper", "Linen").forEach { texture ->
                            FilterChip(
                                onClick = { backgroundTexture = texture },
                                label = { Text(texture) },
                                selected = backgroundTexture == texture,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = GrowlyColors.SkyBlue.copy(alpha = 0.2f),
                                    selectedLabelColor = GrowlyColors.SkyBlue
                                )
                            )
                        }
                    }
                }
                
                // Ambient Sound
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ambient Sound",
                            style = MaterialTheme.typography.titleMedium,
                            color = GrowlyColors.TextPrimary
                        )
                        Switch(
                            checked = ambientSoundEnabled,
                            onCheckedChange = { ambientSoundEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = GrowlyColors.LightMint,
                                checkedTrackColor = GrowlyColors.LightMint.copy(alpha = 0.5f)
                            )
                        )
                    }
                    
                    if (ambientSoundEnabled) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Nature", "Rain", "Ocean", "Cafe").forEach { sound ->
                                FilterChip(
                                    onClick = { ambientSoundType = sound },
                                    label = { Text(sound) },
                                    selected = ambientSoundType == sound,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = GrowlyColors.BlushPink.copy(alpha = 0.2f),
                                        selectedLabelColor = GrowlyColors.BlushPink
                                    )
                                )
                            }
                        }
                        
                        Text(
                            text = "Volume: ${(ambientSoundVolume * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrowlyColors.TextSecondary
                        )
                        Slider(
                            value = ambientSoundVolume,
                            onValueChange = { ambientSoundVolume = it },
                            colors = SliderDefaults.colors(
                                thumbColor = GrowlyColors.BlushPink,
                                activeTrackColor = GrowlyColors.BlushPink
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onApplySettings(
                        WritingSettings(
                            fontSize = fontSize,
                            backgroundTexture = backgroundTexture,
                            ambientSoundEnabled = ambientSoundEnabled,
                            ambientSoundType = ambientSoundType,
                            ambientSoundVolume = ambientSoundVolume
                        )
                    )
                }
            ) {
                Text("Apply", color = GrowlyColors.SkyBlue)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = GrowlyColors.TextSecondary)
            }
        }
    )
}

data class WritingSettings(
    val fontSize: Int,
    val backgroundTexture: String,
    val ambientSoundEnabled: Boolean,
    val ambientSoundType: String,
    val ambientSoundVolume: Float
)

private fun getCategoryDisplayName(category: JournalCategory): String {
    return when (category) {
        JournalCategory.DAILY_REFLECTIONS -> "Daily Reflections"
        JournalCategory.GRATITUDE_JOURNALING -> "Gratitude Journaling"
        JournalCategory.GOAL_TRACKING -> "Goal Tracking"
        JournalCategory.FREE_WRITING -> "Free Writing"
        JournalCategory.CUSTOM_PROMPTS -> "Custom Prompts"
    }
}

private fun getPlaceholderText(category: JournalCategory): String {
    return when (category) {
        JournalCategory.DAILY_REFLECTIONS -> "How was your day? What thoughts and experiences would you like to reflect on?"
        JournalCategory.GRATITUDE_JOURNALING -> "What are you grateful for today? Write about the people, moments, or things that brought you joy..."
        JournalCategory.GOAL_TRACKING -> "How are you progressing towards your goals? What steps did you take today?"
        JournalCategory.FREE_WRITING -> "Let your thoughts flow freely. Write whatever comes to mind without judgment..."
        JournalCategory.CUSTOM_PROMPTS -> "Explore today's writing prompt and let your creativity guide you..."
    }
}

private fun getCurrentDateTime(): String {
    // TODO: Implement proper date/time formatting
    return "Today, 2:30 PM"
}
