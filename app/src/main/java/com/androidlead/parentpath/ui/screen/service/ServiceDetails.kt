package com.androidlead.parentpath.ui.screen.service

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.androidlead.parentpath.R
import com.androidlead.parentpath.ui.theme.*
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import com.androidlead.parentpath.ui.screen.container.NavGraph
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ServiceDetails(
    onRestartFlowClicked: () -> Unit,
    navHost: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val selectedDate = remember { mutableStateOf(getDatesOfWeek()[0]) }
    val selectedTime = remember { mutableStateOf("10:00 AM") }

    val showDialog = remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(240.dp)) {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.avatar),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(80.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Emna", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(24.dp))
                Divider()
                listOf(
                    MenuItem("Home", Icons.Default.Home) { navHost.navigate(NavGraph.Home.route) },
                    MenuItem("Edit Profile", Icons.Default.Person) { navHost.navigate(NavGraph.Profile.route) },
                    MenuItem("Offer a Service", Icons.Default.Add) { navHost.navigate(NavGraph.Service.route) },
                    MenuItem("Booking List", Icons.Default.List) { navHost.navigate(NavGraph.Booking.route) }
                ).forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.title) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            item.onClick()
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Spacer(modifier = Modifier.weight(1f))
                Divider()
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onRestartFlowClicked()
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(
                    0f to PrimaryPinkBlended,
                    0.6f to PrimaryYellowLight,
                    1f to PrimaryYellow
                ))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
                Text(
                    text = "Service Details",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(R.drawable.babysitter),
                contentDescription = "Service Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Service Title", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("By: Provider Name", style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
            Text("This is a description of the service. It explains what the service offers and how it helps the user.", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Price: \$29.99", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))

            Spacer(modifier = Modifier.height(16.dp))

            Text("Select Date", style = MaterialTheme.typography.titleMedium)
            Row(Modifier.horizontalScroll(rememberScrollState())) {
                getDatesOfWeek().forEach { date ->
                    val isSelected = selectedDate.value == date
                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (isSelected) PrimaryPink else Color.White),
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { selectedDate.value = date }
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(SimpleDateFormat("EEE", Locale.getDefault()).format(date), fontWeight = FontWeight.Bold)
                            Text(SimpleDateFormat("dd MMM", Locale.getDefault()).format(date))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Select Time", style = MaterialTheme.typography.titleMedium)
            Row(Modifier.horizontalScroll(rememberScrollState())) {
                listOf("10:00 AM", "12:00 PM", "2:00 PM", "4:00 PM", "6:00 PM").forEach { time ->
                    val isSelected = selectedTime.value == time
                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (isSelected) PrimaryPink else Color.White),
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { selectedTime.value = time }
                    ) {
                        Box(modifier = Modifier.padding(12.dp)) {
                            Text(time)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { showDialog.value = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Book Now and Pay")
            }

            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("Confirm Booking") },
                    text = {
                        Text("You are about to book this service on ${SimpleDateFormat("dd MMM", Locale.getDefault()).format(selectedDate.value)} at ${selectedTime.value}. Proceed to PayPal?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDialog.value = false
                                // Simulate redirection
                                navHost.navigate(NavGraph.Booking.route)
                            }
                        ) {
                            Text("Continue")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog.value = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

private fun getDatesOfWeek(): List<Date> {
    val calendar = Calendar.getInstance()
    return List(7) {
        val date = calendar.time
        calendar.add(Calendar.DATE, 1)
        date
    }
}


