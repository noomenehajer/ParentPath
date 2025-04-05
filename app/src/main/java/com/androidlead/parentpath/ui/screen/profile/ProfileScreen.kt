package com.androidlead.parentpath.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.androidlead.parentpath.R
import com.androidlead.parentpath.ui.theme.PrimaryPinkBlended
import com.androidlead.parentpath.ui.theme.PrimaryYellow
import com.androidlead.parentpath.ui.theme.PrimaryYellowDark
import com.androidlead.parentpath.ui.theme.PrimaryYellowLight
import com.androidlead.parentpath.ui.components.InputField

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onRestartFlowClicked: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Edit Profile",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(R.drawable.avatar),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            InputField(
                value = name,
                onValueChange = { name = it },
                leadingIconRes = R.drawable.ic_user,
                placeholderText = "Name"
            )

            Spacer(modifier = Modifier.height(12.dp))

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

            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = address,
                onValueChange = { address = it },
                leadingIconRes = R.drawable.ic_address,
                placeholderText = "Address"
            )

            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                leadingIconRes = R.drawable.ic_phone,
                placeholderText = "Phone Number"
            )

            Spacer(modifier = Modifier.height(12.dp))

            InputField(
                value = bio,
                onValueChange = { bio = it },
                leadingIconRes = R.drawable.ic_bio,
                placeholderText = "Bio"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Handle save action */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryYellowDark,
                contentColor = Color.DarkGray
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp)
        ) {
            Text("Save")
        }
    }
}
