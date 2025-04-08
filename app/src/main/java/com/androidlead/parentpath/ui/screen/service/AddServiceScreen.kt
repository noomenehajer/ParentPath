package com.androidlead.parentpath.ui.screen.service

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

data class Service(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceScreen(
    modifier: Modifier = Modifier,
    onRestartFlowClicked: () -> Unit,
    navHost: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showAddServiceDialog by remember { mutableStateOf(false) }

    // Sample services data
    val services = remember {
        listOf(
            Service("1", "Babysitting", "Professional childcare services", "$15/hr", "Childcare"),
            Service("2", "Tutoring", "Math and science tutoring", "$20/hr", "Education"),
            Service("3", "Pet Care", "Dog walking and pet sitting", "$12/hr", "Pets")
        )
    }

    val menuItems = listOf(
        MenuItem("Edit Profile", Icons.Default.Person) { navHost.navigate(NavGraph.Profile.route) },
        MenuItem("Home", Icons.Default.Home) { navHost.navigate(NavGraph.Home.route) },
        MenuItem("Offer a Service", Icons.Default.Add) {navHost.navigate(NavGraph.Service.route) },
        MenuItem("Booking List", Icons.Default.List) { /* add navigation */ }
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
            // Header with menu icon and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "My Services",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.weight(1f)
                )
                FloatingActionButton(
                    onClick = { showAddServiceDialog = true },
                    modifier = Modifier.size(40.dp),
                    containerColor = PrimaryPink,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Service")
                }
            }

            // Services List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(services) { service ->
                    ServiceCard(service = service)
                }
            }
        }
    }

    // Add Service Dialog
    if (showAddServiceDialog) {
        AlertDialog(
            onDismissRequest = { showAddServiceDialog = false },
            title = { Text("Add New Service") },
            text = {
                Column {
                    OutlinedTextField(
                        value = "",
                        onValueChange = { /* Handle input */ },
                        label = { Text("Service Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = { /* Handle input */ },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = { /* Handle input */ },
                        label = { Text("Price") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = { /* Handle input */ },
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showAddServiceDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink)
                ) {
                    Text("Add Service")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAddServiceDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ServiceCard(service: Service) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = service.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Text(
                    text = service.price,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPink
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = service.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = service.category,
                style = MaterialTheme.typography.labelSmall,
                color = PrimaryPink,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(PrimaryYellowLight.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { /* Edit service */ }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = PrimaryYellowDark
                    )
                }
                IconButton(onClick = { /* Delete service */ }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFAA4B59)
                    )
                }
            }
        }
    }
}