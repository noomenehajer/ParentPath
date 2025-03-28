package com.androidlead.loginappui.ui.screen.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.androidlead.loginappui.R
import com.androidlead.loginappui.ui.components.ActionButton
import com.androidlead.loginappui.ui.theme.DarkTextColor
import com.androidlead.loginappui.ui.theme.PrimaryPinkBlended
import com.androidlead.loginappui.ui.theme.PrimaryYellow
import com.androidlead.loginappui.ui.theme.PrimaryYellowDark
import com.androidlead.loginappui.ui.theme.PrimaryYellowLight
import androidx.compose.ui.graphics.Color

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onOpenLoginClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to PrimaryPinkBlended,
                    0.6f to PrimaryYellowLight,
                    1f to PrimaryYellow
                )
            )
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.img_welcome),
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
                .padding(top = 32.dp)
        )
        Spacer(
            modifier = Modifier.height(24.dp)
        )
        Text(
            text = "Welcome to ParentPath",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center,
            color = DarkTextColor
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Text(
            text = "All support you need just one tap away!",
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = DarkTextColor
        )
        Spacer(
            modifier = Modifier.weight(weight = 1f)
        )
        ActionButton(
            text = "Get Started",
            isNavigationArrowVisible = true,
            onClicked = onOpenLoginClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFAA4B59),
                contentColor = Color(0xFFFFF5CC)
            ),
            shadowColor = PrimaryYellowDark,
            modifier = Modifier.padding(24.dp)
        )
    }
}