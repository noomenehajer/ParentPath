package com.androidlead.parentpath.ui.screen.login
import com.androidlead.parentpath.ui.theme.DarkTextColor
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.androidlead.parentpath.R
import com.androidlead.parentpath.ui.components.AuthenticationScreenTemplate
import com.androidlead.parentpath.ui.theme.PrimaryPinkBlended
import com.androidlead.parentpath.ui.theme.PrimaryYellow
import com.androidlead.parentpath.ui.theme.PrimaryYellowLight
import com.androidlead.parentpath.ui.theme.PrimaryYellowDark
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginClicked: () -> Unit,
    onRegistrationClicked: () -> Unit
) {
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
        onMainActionButtonClicked = onLoginClicked,
        onSecondaryActionButtonClicked = onRegistrationClicked
    )
}
