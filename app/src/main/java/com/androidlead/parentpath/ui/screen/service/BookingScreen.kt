package com.androidlead.parentpath.ui.screen.service

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.androidlead.parentpath.R
import com.androidlead.parentpath.ui.screen.container.NavGraph
import com.androidlead.parentpath.ui.theme.*
import kotlinx.coroutines.launch

data class Booking(
    val id: Int,
    val serviceName: String,
    val serviceDate: String,
    val price: String,
    val isPaid: Boolean = false // Add this field
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    modifier: Modifier = Modifier,
    onRestartFlowClicked: () -> Unit,
    navHost: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val bookings = remember {
        mutableStateListOf(
            Booking(1, "Babysitting", "2025-05-05", "30", isPaid = true),// Marked as paid
            Booking(2, "Private Tutoring", "2025-05-10", "50"),
            Booking(3, "Cooking Assistance", "2025-05-12", "40")
        )
    }


    var showDeleteDialog by remember { mutableStateOf(false) }
    var bookingToDelete by remember { mutableStateOf<Booking?>(null) }

    val menuItems = listOf(
        MenuItem("Home", Icons.Default.Home) { navHost.navigate(NavGraph.Home.route) },
        MenuItem("Edit Profile", Icons.Default.Person) { navHost.navigate(NavGraph.Profile.route) },
        MenuItem("Offer a Service", Icons.Default.Add) { navHost.navigate(NavGraph.Service.route) },
        MenuItem("Booking List", Icons.Default.List) { navHost.navigate(NavGraph.Booking.route) }
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(240.dp)) {
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.avatar),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Emna",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                menuItems.forEach { item ->
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
            modifier = modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to PrimaryPinkBlended,
                        0.6f to PrimaryYellowLight,
                        1f to PrimaryYellow
                    )
                )
                .padding(16.dp)
                .systemBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
                Text(
                    text = "My Booking List",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.weight(1f)
                )
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(bookings) { booking ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Service: ${booking.serviceName}", fontWeight = FontWeight.Bold)
                            Text("Date: ${booking.serviceDate}")
                            Text("Price: ${booking.price} TND")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (booking.isPaid) {
                                    Text(
                                        text = "Paid ✅",
                                        color = Color(0xFF2E7D32), // Green color
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Button(
                                        onClick = {
                                            val paypalUrl = "https://www.paypal.com/checkoutnow\"${booking.price}"
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paypalUrl))
                                            context.startActivity(intent)
                                        }
                                    ) {
                                        Icon(Icons.Default.ShoppingCart, contentDescription = "Pay")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Pay Now")
                                    }

                                    Button(
                                        onClick = {
                                            bookingToDelete = booking
                                            showDeleteDialog = true
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                                    ) {
                                        Text("Cancel", color = Color.White)
                                    }
                                }
                            }

                        }
                    }
                }
            }

            if (showDeleteDialog && bookingToDelete != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = { Text("Cancel Booking") },
                    text = { Text("Are you sure you want to cancel this booking?") },
                    confirmButton = {
                        TextButton(onClick = {
                            bookings.remove(bookingToDelete)
                            showDeleteDialog = false
                            bookingToDelete = null
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDeleteDialog = false
                            bookingToDelete = null
                        }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}
