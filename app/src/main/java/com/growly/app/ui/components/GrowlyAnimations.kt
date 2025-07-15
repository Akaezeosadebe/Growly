package com.growly.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.growly.app.ui.theme.GrowlyColors
import kotlinx.coroutines.delay

@Composable
fun AutoSaveIndicator(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(300, easing = EaseOutCubic)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(300, easing = EaseInCubic)
        ) + fadeOut(animationSpec = tween(300)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .background(
                    color = GrowlyColors.LightMint.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
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
}

@Composable
fun WordCountAnimation(
    wordCount: Int,
    modifier: Modifier = Modifier
) {
    var previousCount by remember { mutableStateOf(wordCount) }
    val animatedCount by animateIntAsState(
        targetValue = wordCount,
        animationSpec = tween(500, easing = EaseOutCubic),
        label = "word_count"
    )
    
    LaunchedEffect(wordCount) {
        previousCount = wordCount
    }
    
    val scale by animateFloatAsState(
        targetValue = if (wordCount != previousCount) 1.1f else 1f,
        animationSpec = tween(200, easing = EaseOutCubic),
        label = "word_count_scale"
    )
    
    Text(
        text = "Words: $animatedCount",
        style = MaterialTheme.typography.bodyMedium,
        color = GrowlyColors.TextSecondary,
        modifier = modifier.scale(scale)
    )
}

@Composable
fun TaskCompletionAnimation(
    isCompleted: Boolean,
    onAnimationComplete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showCheckmark by remember { mutableStateOf(false) }
    
    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            showCheckmark = true
            delay(1500)
            showCheckmark = false
            onAnimationComplete()
        }
    }
    
    AnimatedVisibility(
        visible = showCheckmark,
        enter = scaleIn(
            initialScale = 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(),
        exit = scaleOut(
            targetScale = 0f,
            animationSpec = tween(300)
        ) + fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = GrowlyColors.LightMint,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.Check,
                contentDescription = "Completed",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun PulsingIcon(
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = GrowlyColors.SkyBlue,
    pulseColor: Color = tint.copy(alpha = 0.3f)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Pulsing background
        Icon(
            icon,
            contentDescription = null,
            tint = pulseColor.copy(alpha = alpha),
            modifier = Modifier.scale(scale)
        )
        
        // Main icon
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

@Composable
fun FloatingActionButtonWithAnimation(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = GrowlyColors.LightMint,
    contentColor: Color = GrowlyColors.DarkerGray
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "fab_scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 6.dp,
        animationSpec = tween(150),
        label = "fab_elevation"
    )
    
    FloatingActionButton(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = modifier.scale(scale),
        containerColor = backgroundColor,
        contentColor = contentColor,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = elevation)
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier,
    isLoading: Boolean = true
) {
    if (isLoading) {
        val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
        
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 0.8f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = EaseInOutCubic),
                repeatMode = RepeatMode.Reverse
            ),
            label = "shimmer_alpha"
        )
        
        Box(
            modifier = modifier
                .background(
                    color = GrowlyColors.LightGray.copy(alpha = alpha),
                    shape = RoundedCornerShape(8.dp)
                )
        )
    }
}

@Composable
fun ProgressIndicatorWithAnimation(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = GrowlyColors.LightMint,
    trackColor: Color = GrowlyColors.LightGray
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "progress"
    )
    
    LinearProgressIndicator(
        progress = animatedProgress,
        modifier = modifier,
        color = color,
        trackColor = trackColor
    )
}

@Composable
fun SlideInCard(
    visible: Boolean,
    delay: Int = 0,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(
                durationMillis = 500,
                delayMillis = delay,
                easing = EaseOutCubic
            )
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = 500,
                delayMillis = delay
            )
        )
    ) {
        content()
    }
}

@Composable
fun BouncyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "button_scale"
    )
    
    Box(
        modifier = modifier
            .scale(scale)
    ) {
        Button(
            onClick = {
                isPressed = true
                onClick()
            }
        ) {
            content()
        }
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}
