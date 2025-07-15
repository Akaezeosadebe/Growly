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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.growly.app.data.entities.Task
import com.growly.app.data.entities.TaskPriority
import com.growly.app.ui.components.GrowlyCard
import com.growly.app.ui.components.GrowlyFloatingActionButton
import com.growly.app.ui.components.GrowlyPrimaryButton
import com.growly.app.ui.components.GrowlySecondaryButton
import com.growly.app.ui.theme.GrowlyColors
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen() {
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var tasks by remember { mutableStateOf(getSampleTaskEntities().toMutableList()) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Tasks",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = GrowlyColors.TextPrimary
                        )
                        Text(
                            text = "Stay organized and productive",
                            style = MaterialTheme.typography.bodyLarge,
                            color = GrowlyColors.TextSecondary
                        )
                    }
                    
                    FilterChip(
                        onClick = { /* TODO: Filter tasks */ },
                        label = { Text("All") },
                        selected = true,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GrowlyColors.SkyBlue.copy(alpha = 0.2f),
                            selectedLabelColor = GrowlyColors.SkyBlue
                        )
                    )
                }
            }

            // Task Statistics
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TaskStatCard(
                        title = "Completed",
                        value = "8",
                        subtitle = "Today",
                        color = GrowlyColors.LightMint,
                        modifier = Modifier.weight(1f)
                    )
                    TaskStatCard(
                        title = "Pending",
                        value = "3",
                        subtitle = "Remaining",
                        color = GrowlyColors.BlushPink,
                        modifier = Modifier.weight(1f)
                    )
                    TaskStatCard(
                        title = "Progress",
                        value = "73%",
                        subtitle = "This week",
                        color = GrowlyColors.SkyBlue,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            items(tasks) { task ->
                TaskCard(
                    task = task,
                    onToggleComplete = {
                        tasks = tasks.map {
                            if (it.id == task.id) it.copy(isCompleted = !it.isCompleted)
                            else it
                        }.toMutableList()
                    },
                    onEdit = { /* TODO: Edit task */ },
                    onDelete = {
                        tasks = tasks.filter { it.id != task.id }.toMutableList()
                    }
                )
            }
        }
        
        GrowlyFloatingActionButton(
            onClick = { showAddTaskDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Add Task",
                modifier = Modifier.size(24.dp)
            )
        }
    }
    
    if (showAddTaskDialog) {
        AddTaskDialog(
            onDismiss = { showAddTaskDialog = false },
            onConfirm = { title, description ->
                val newTask = Task(
                    title = title,
                    description = description,
                    isCompleted = false,
                    priority = TaskPriority.MEDIUM,
                    category = "Personal",
                    createdAt = Date()
                )
                tasks = (tasks + newTask).toMutableList()
                showAddTaskDialog = false
            }
        )
    }
}

@Composable
fun TaskStatCard(
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

@Composable
fun TaskCard(
    task: Task,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    GrowlyCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggleComplete() },
                colors = CheckboxDefaults.colors(
                    checkedColor = GrowlyColors.LightMint,
                    uncheckedColor = GrowlyColors.MediumGray
                )
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (task.isCompleted) GrowlyColors.TextSecondary else GrowlyColors.TextPrimary,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                
                if (task.description.isNotEmpty()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrowlyColors.TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    AssistChip(
                        onClick = { },
                        label = { Text(task.category, style = MaterialTheme.typography.labelSmall) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = getPriorityColor(task.priority).copy(alpha = 0.2f),
                            labelColor = getPriorityColor(task.priority)
                        )
                    )
                    
                    if (task.dueDate != null) {
                        AssistChip(
                            onClick = { },
                            label = { Text(formatDate(task.dueDate!!), style = MaterialTheme.typography.labelSmall) },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }
            }
            
            IconButton(onClick = onEdit) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit",
                    tint = GrowlyColors.TextSecondary
                )
            }
        }
    }
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Task") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(title, description) },
                enabled = title.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


private fun getSampleTaskEntities() = listOf(
    Task(
        id = 1,
        title = "Review project proposal",
        description = "Go through the client's requirements and prepare feedback",
        isCompleted = false,
        priority = TaskPriority.HIGH,
        category = "Work",
        createdAt = Date()
    ),
    Task(
        id = 2,
        title = "Call dentist",
        description = "Schedule appointment for next week",
        isCompleted = true,
        priority = TaskPriority.MEDIUM,
        category = "Personal",
        createdAt = Date()
    ),
    Task(
        id = 3,
        title = "Grocery shopping",
        description = "Buy ingredients for weekend dinner",
        isCompleted = false,
        priority = TaskPriority.LOW,
        category = "Personal",
        createdAt = Date()
    )
)

private fun getPriorityColor(priority: TaskPriority): Color {
    return when (priority) {
        TaskPriority.LOW -> GrowlyColors.LightMint
        TaskPriority.MEDIUM -> GrowlyColors.SkyBlue
        TaskPriority.HIGH -> GrowlyColors.BlushPink
        TaskPriority.URGENT -> Color.Red
    }
}

private fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("MMM dd", Locale.getDefault())
    return formatter.format(date)
}
