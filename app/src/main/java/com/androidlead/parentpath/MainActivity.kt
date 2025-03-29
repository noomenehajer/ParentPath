package com.androidlead.parentpath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.androidlead.parentpath.ui.screen.container.ScreenContainer
import com.androidlead.parentpath.ui.theme.LoginAppUiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginAppUiTheme {
                ScreenContainer()
            }
        }
    }
}
