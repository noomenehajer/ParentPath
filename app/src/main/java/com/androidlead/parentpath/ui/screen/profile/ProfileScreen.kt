package com.androidlead.parentpath.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.androidlead.parentpath.R
import com.androidlead.parentpath.ui.theme.PrimaryPinkBlended
import com.androidlead.parentpath.ui.theme.PrimaryYellow
import com.androidlead.parentpath.ui.theme.PrimaryYellowLight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextFieldDefaults

@Composable
fun ProfileScreen(
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp)) // Add space at the top

        // Profile Picture
        Image(
            painter = painterResource(R.drawable.avatar),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Editable Fields
        val name = remember { mutableStateOf("") }
        val number = remember { mutableStateOf("") }
        val email = remember { mutableStateOf("") }
        val bio = remember { mutableStateOf("") }

        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Name", color = Color.DarkGray) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp)),

        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = number.value,
            onValueChange = { number.value = it },
            label = { Text("Number", color = Color.DarkGray) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp)),

        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email", color = Color.DarkGray) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp)),

        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = bio.value,
            onValueChange = { bio.value = it },
            label = { Text("Bio", color = Color.DarkGray) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp)),

        )
        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(
            onClick = { /* Save user info */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFAA4B59),
                contentColor = Color(0xFFFFF5CC)
            )
        ) {
            Text("Save", color = Color(0xFFFFF5CC))
        }
    }
}


