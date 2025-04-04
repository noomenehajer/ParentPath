package com.androidlead.parentpath.ui.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.androidlead.parentpath.R
import com.androidlead.parentpath.ui.components.AuthenticationScreenTemplate
import com.androidlead.parentpath.ui.components.InputField
import com.androidlead.parentpath.ui.theme.PrimaryPinkBlended
import com.androidlead.parentpath.ui.theme.PrimaryYellow
import com.androidlead.parentpath.ui.theme.PrimaryYellowLight
import com.androidlead.parentpath.ui.theme.PrimaryYellowDark
import com.androidlead.parentpath.viewmodel.UserViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginClicked: () -> Unit,
    onRegistrationClicked: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: UserViewModel = viewModel()
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    AuthenticationScreenTemplate(
        modifier = modifier,
        backgroundGradient = arrayOf(
            0f to PrimaryYellow,
            0.6f to PrimaryYellowLight,
            1f to PrimaryPinkBlended
        ),
        imgRes = R.drawable.img1,
        title = "Welcome back!",
        subtitle = "Please sign in",
        mainActionButtonTitle = "Continue",
        secondaryActionButtonTitle = "Create an account",
        mainActionButtonColors = ButtonDefaults.buttonColors(
            containerColor = PrimaryYellowDark,
            contentColor = Color.DarkGray
        ),
        secondaryActionButtonColors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFAA4B59),
            contentColor = Color.White
        ),
        actionButtonShadow = PrimaryYellowDark,
        onMainActionButtonClicked = {
            if (email.isBlank() || password.isBlank()) {
                message = "Please fill in all fields"
                return@AuthenticationScreenTemplate
            }
            
            val success = viewModel.loginUser(email, password)
            if (success) {
                message = ""
                onLoginClicked()
            } else {
                message = "Invalid email or password"
            }
        },
        onSecondaryActionButtonClicked = onRegistrationClicked
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            InputField(
                value = email,
                onValueChange = { email = it },
                leadingIconRes = R.drawable.ic_email,
                placeholderText = "Email"
            )

            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
                leadingIconRes = R.drawable.ic_lock,
                placeholderText = "Password"
            )

            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
