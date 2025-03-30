package com.androidlead.parentpath.ui.screen.registration

import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.androidlead.parentpath.R
import com.androidlead.parentpath.ui.components.AuthenticationScreenTemplate
import com.androidlead.parentpath.ui.theme.DarkTextColor
import com.androidlead.parentpath.ui.theme.PrimaryPinkBlended
import com.androidlead.parentpath.ui.theme.PrimaryViolet
import com.androidlead.parentpath.ui.theme.PrimaryVioletDark
import com.androidlead.parentpath.ui.theme.PrimaryVioletLight
import com.androidlead.parentpath.ui.theme.PrimaryYellow
import com.androidlead.parentpath.ui.theme.PrimaryYellowDark
import com.androidlead.parentpath.ui.theme.PrimaryYellowLight

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    onRegisterClicked: () -> Unit,
    onLoginClicked: () -> Unit
){
    AuthenticationScreenTemplate(
        modifier = modifier,
        backgroundGradient = arrayOf(
            0f to PrimaryYellow,
            0.6f to PrimaryYellowLight,
            1f to PrimaryPinkBlended
        ),
        imgRes = R.drawable.img2,
        title = "Hi there!",
        subtitle = "Let's Get Started",
        mainActionButtonTitle = "Sign up",
        secondaryActionButtonTitle = "Log In",
        mainActionButtonColors = ButtonDefaults.buttonColors(
            containerColor = PrimaryYellowDark,
            contentColor = Color.DarkGray
        ),
        secondaryActionButtonColors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFAA4B59),
            contentColor = Color.White
        ),
        actionButtonShadow = PrimaryYellowDark,
        onMainActionButtonClicked = onRegisterClicked,
        onSecondaryActionButtonClicked = onLoginClicked
    )
}