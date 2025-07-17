package com.growly.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.growly.app.ui.components.GrowlyCard
import com.growly.app.ui.components.GrowlyPrimaryButton
import com.growly.app.ui.components.GrowlySecondaryButton
import com.growly.app.ui.theme.GrowlyColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onSignInSuccess: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo/Title
        Text(
            text = "🌱 Growly",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = GrowlyColors.SkyBlue,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Mindful Productivity Companion",
            style = MaterialTheme.typography.bodyLarge,
            color = GrowlyColors.TextSecondary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Auth Form
        GrowlyCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isSignUp) "Create Account" else "Welcome Back",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = GrowlyColors.TextPrimary
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = GrowlyColors.SkyBlue
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GrowlyColors.SkyBlue,
                        focusedLabelColor = GrowlyColors.SkyBlue
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password",
                            tint = GrowlyColors.SkyBlue
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = GrowlyColors.TextSecondary
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GrowlyColors.SkyBlue,
                        focusedLabelColor = GrowlyColors.SkyBlue
                    )
                )
                
                // Confirm Password Field (only for sign up)
                if (isSignUp) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Confirm Password",
                                tint = GrowlyColors.SkyBlue
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GrowlyColors.SkyBlue,
                            focusedLabelColor = GrowlyColors.SkyBlue
                        )
                    )
                }
                
                // Error Message
                errorMessage?.let { error ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Primary Action Button
                GrowlyPrimaryButton(
                    text = if (isSignUp) "Create Account" else "Sign In",
                    onClick = {
                        // TODO: Implement authentication logic
                        if (isSignUp) {
                            if (password == confirmPassword) {
                                onSignUpSuccess()
                            } else {
                                errorMessage = "Passwords don't match"
                            }
                        } else {
                            onSignInSuccess()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading && email.isNotBlank() && password.isNotBlank()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Switch Mode Button
                GrowlySecondaryButton(
                    text = if (isSignUp) "Already have an account? Sign In" else "Don't have an account? Sign Up",
                    onClick = { 
                        isSignUp = !isSignUp
                        errorMessage = null
                        confirmPassword = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Skip for now option
        TextButton(
            onClick = onSignInSuccess // For demo purposes, skip auth
        ) {
            Text(
                text = "Continue without account (Demo)",
                color = GrowlyColors.TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
