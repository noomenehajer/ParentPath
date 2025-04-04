package com.androidlead.parentpath.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.androidlead.parentpath.ui.components.ActionButton
import com.androidlead.parentpath.ui.theme.PrimaryGreen
import com.androidlead.parentpath.ui.theme.PrimaryGreenDark
import com.androidlead.parentpath.ui.theme.PrimaryPinkBlended
import com.androidlead.parentpath.ui.theme.PrimaryPinkDark
import com.androidlead.parentpath.ui.theme.PrimaryViolet
import com.androidlead.parentpath.ui.theme.PrimaryYellow
import com.androidlead.parentpath.ui.theme.PrimaryYellowLight
import nl.dionsegijn.konfetti.compose.KonfettiView

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onRestartFlowClicked: () -> Unit
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
        Spacer(
            modifier = Modifier.weight(weight = 1f)
        )
        Greetings()
        Spacer(
            modifier = Modifier.weight(weight = 1f)
        )
        ActionButton(
            text = "Press to restart",
            isNavigationArrowVisible = false,
            onClicked = onRestartFlowClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFAA4B59),
                contentColor = Color(0xFFFFF5CC)
            ),
            shadowColor = PrimaryPinkDark,
            modifier = Modifier.padding(24.dp)
        )
    }

}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Hello",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )

    }
}