package com.androidlead.parentpath.ui.screen.registration

import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.androidlead.parentpath.R
import com.androidlead.parentpath.ui.components.AuthenticationScreenTemplate
import com.androidlead.parentpath.ui.theme.PrimaryViolet
import com.androidlead.parentpath.ui.theme.PrimaryVioletDark
import com.androidlead.parentpath.ui.theme.PrimaryVioletLight

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    onRegisterClicked: () -> Unit,
    onLoginClicked: () -> Unit
){
    AuthenticationScreenTemplate(
        modifier = modifier,
        backgroundGradient = arrayOf(
            0f to PrimaryViolet,
            1f to PrimaryVioletDark
        ),
        imgRes = R.drawable.img2,
        title = "Hi there!",
        subtitle = "Let's Get Started",
        mainActionButtonTitle = "Create an Account",
        secondaryActionButtonTitle = "Log In",
        mainActionButtonColors = ButtonDefaults.buttonColors(
            containerColor = PrimaryVioletDark,
            contentColor = Color.White
        ),
        secondaryActionButtonColors = ButtonDefaults.buttonColors(
            containerColor = PrimaryVioletLight,
            contentColor = Color.White
        ),
        actionButtonShadow = PrimaryVioletDark,
        onMainActionButtonClicked = onRegisterClicked,
        onSecondaryActionButtonClicked = onLoginClicked
    )
}