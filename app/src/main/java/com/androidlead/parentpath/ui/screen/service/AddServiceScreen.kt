package com.androidlead.parentpath.ui.screen.service

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.androidlead.parentpath.R
import com.androidlead.parentpath.ui.theme.*
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import com.androidlead.parentpath.ui.screen.container.NavGraph
import java.util.*

data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

data class Availability(
    val date: String,
    val startTime: String,
    val endTime: String
)

data class ClientBooking(
    val id: String,
    val clientName: String,
    val bookingDate: String,
    val bookingTime: String,
    val isCompleted: Boolean = false
)

data class Service(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val imageRes: Int,
    val availabilities: List<Availability> = emptyList(),
    val clientBookings: List<ClientBooking> = emptyList()
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
    var showEditServiceDialog by remember { mutableStateOf(false) }
    var editingService by remember { mutableStateOf<Service?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var serviceToDelete by remember { mutableStateOf<Service?>(null) }
    var showClientBookingsDialog by remember { mutableStateOf(false) }
    var selectedServiceForBookings by remember { mutableStateOf<Service?>(null) }

    // Make services mutable and observable for immediate updates
    val services = remember { mutableStateListOf(
        Service(
            "1",
            "Babysitting",
            "Childcare services",
            "15TND/hr",
            R.drawable.babysitter,
            clientBookings = listOf(
                ClientBooking("1", "Ahmed", "2023-06-15", "14:00"),
                ClientBooking("2", "Eya", "2023-06-16", "10:00", true)
            )
        ),
        Service(
            "2",
            "Tutoring",
            "Math and science tutoring",
            "20TND/hr",
            R.drawable.tutor,
            clientBookings = listOf(
                ClientBooking("3", "Mohamed", "2023-06-17", "16:00")
            )
        ),
        Service(
            "3",
            "Fitness Coach",
            "Fitness health care",
            "12TND/hr",
            R.drawable.fitness
        )
    )}

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
                        text = "Hello Sarra",
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(services) { service ->
                    ServiceCard(
                        service = service,
                        onEditClick = {
                            editingService = service
                            showEditServiceDialog = true
                        },
                        onDeleteClick = {
                            serviceToDelete = service
                            showDeleteConfirmation = true
                        },
                        onViewBookingsClick = {
                            selectedServiceForBookings = service
                            showClientBookingsDialog = true
                        }
                    )
                }
            }

            if (showAddServiceDialog) {
                AddEditServiceDialog(
                    service = null,
                    onDismiss = { showAddServiceDialog = false },
                    onConfirm = { newService ->
                        services.add(newService)
                        showAddServiceDialog = false
                    }
                )
            }

            if (showEditServiceDialog && editingService != null) {
                AddEditServiceDialog(
                    service = editingService,
                    onDismiss = { showEditServiceDialog = false },
                    onConfirm = { updatedService ->
                        val index = services.indexOfFirst { it.id == editingService?.id }
                        if (index != -1) {
                            services[index] = updatedService
                        }
                        showEditServiceDialog = false
                    }
                )
            }

            if (showDeleteConfirmation && serviceToDelete != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmation = false },
                    title = { Text("Confirm Deletion") },
                    text = { Text("Are you sure you want to delete '${serviceToDelete?.title}'? This action cannot be undone.") },
                    confirmButton = {
                        Button(
                            onClick = {
                                services.remove(serviceToDelete)
                                showDeleteConfirmation = false
                                serviceToDelete = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDeleteConfirmation = false
                                serviceToDelete = null
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }

            if (showClientBookingsDialog && selectedServiceForBookings != null) {
                val service = selectedServiceForBookings!!
                var currentService by remember { mutableStateOf(service) }

                AlertDialog(
                    onDismissRequest = { showClientBookingsDialog = false },
                    title = {
                        Text(
                            text = "Bookings for ${currentService.title}",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    text = {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            if (currentService.clientBookings.isEmpty()) {
                                Text(
                                    text = "No bookings yet",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            } else {
                                LazyColumn(
                                    modifier = Modifier.heightIn(max = 400.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(currentService.clientBookings) { booking ->
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (booking.isCompleted) Color(0xFFE8F5E9) else Color(0xFFFFF8E1)
                                            )
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(12.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = booking.clientName,
                                                        style = MaterialTheme.typography.titleMedium.copy(
                                                            color = Color.DarkGray,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )
                                                    if (booking.isCompleted) {
                                                        Text(
                                                            text = "Completed",
                                                            color = Color(0xFF2E7D32),
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    } else {
                                                        Text(
                                                            text = "Pending",
                                                            color = Color(0xFFFB8C00),
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }

                                                Spacer(modifier = Modifier.height(4.dp))

                                                Text(
                                                    text = "Date: ${booking.bookingDate} at ${booking.bookingTime}",
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        color = Color.DarkGray
                                                    )
                                                )

                                                Spacer(modifier = Modifier.height(8.dp))

                                                if (!booking.isCompleted) {
                                                    Button(
                                                        onClick = {
                                                            // Update the booking status
                                                            val updatedBookings = currentService.clientBookings.toMutableList()
                                                            val bookingIndex = updatedBookings.indexOfFirst { it.id == booking.id }
                                                            if (bookingIndex != -1) {
                                                                updatedBookings[bookingIndex] = booking.copy(isCompleted = true)
                                                                val updatedService = currentService.copy(clientBookings = updatedBookings)

                                                                // Update the service in the main list
                                                                val serviceIndex = services.indexOfFirst { it.id == currentService.id }
                                                                if (serviceIndex != -1) {
                                                                    services[serviceIndex] = updatedService
                                                                }

                                                                // Update the local state for immediate UI update
                                                                currentService = updatedService
                                                            }
                                                        },
                                                        modifier = Modifier.fillMaxWidth(),
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = PrimaryPink
                                                        )
                                                    ) {
                                                        Text("Mark as Completed")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { showClientBookingsDialog = false }
                        ) {
                            Text("Close")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun AddEditServiceDialog(
    service: Service?,
    onDismiss: () -> Unit,
    onConfirm: (Service) -> Unit
) {
    var title by remember { mutableStateOf(service?.title ?: "") }
    var description by remember { mutableStateOf(service?.description ?: "") }
    var price by remember { mutableStateOf(service?.price ?: "") }
    var selectedImage by remember { mutableStateOf(service?.imageRes ?: R.drawable.im) }
    var availabilities by remember { mutableStateOf(service?.availabilities ?: emptyList()) }

    // Date and Time Picker states
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Calendar?>(null) }
    var selectedStartTime by remember { mutableStateOf<Calendar?>(null) }
    var selectedEndTime by remember { mutableStateOf<Calendar?>(null) }

    val context = LocalContext.current

    // Image picker
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedImage = R.drawable.babysitter
        }
    )

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, day ->
                val selectedCal = Calendar.getInstance()
                selectedCal.set(year, month, day)
                selectedDate = selectedCal
                showDatePicker = false
                showStartTimePicker = true
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    if (showStartTimePicker) {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            context,
            { _, hour, minute ->
                val selectedCal = Calendar.getInstance()
                selectedCal.time = selectedDate?.time ?: Date()
                selectedCal.set(Calendar.HOUR_OF_DAY, hour)
                selectedCal.set(Calendar.MINUTE, minute)
                selectedStartTime = selectedCal
                showStartTimePicker = false
                showEndTimePicker = true
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    if (showEndTimePicker) {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            context,
            { _, hour, minute ->
                val selectedCal = Calendar.getInstance()
                selectedCal.time = selectedDate?.time ?: Date()
                selectedCal.set(Calendar.HOUR_OF_DAY, hour)
                selectedCal.set(Calendar.MINUTE, minute)
                selectedEndTime = selectedCal

                val dateStr = "${selectedDate?.get(Calendar.DAY_OF_MONTH)}/${selectedDate?.get(Calendar.MONTH)?.plus(1)}/${selectedDate?.get(Calendar.YEAR)}"
                val startTimeStr = "${selectedStartTime?.get(Calendar.HOUR_OF_DAY)}:${selectedStartTime?.get(Calendar.MINUTE)?.toString()?.padStart(2, '0')}"
                val endTimeStr = "${selectedEndTime?.get(Calendar.HOUR_OF_DAY)}:${selectedEndTime?.get(Calendar.MINUTE)?.toString()?.padStart(2, '0')}"

                availabilities = availabilities + Availability(dateStr, startTimeStr, endTimeStr)
                showEndTimePicker = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (service == null) "Add New Service" else "Edit Service") },
        text = {
            Column {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .clickable {
                                imagePicker.launch("image/*")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(selectedImage),
                            contentDescription = "Service Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Change Image",
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .background(Color.White, CircleShape)
                                .padding(4.dp),
                            tint = PrimaryPink
                        )
                    }
                    Text(
                        text = "Tap to change image",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Service Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Availability",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryYellowLight)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Availability Slot")
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (availabilities.isNotEmpty()) {
                    Column {
                        Text(
                            text = "Scheduled Availability:",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        availabilities.forEachIndexed { index, availability ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "${availability.date} from ${availability.startTime} to ${availability.endTime}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 2.dp)
                                )
                                IconButton(
                                    onClick = {
                                        availabilities = availabilities.toMutableList().apply {
                                            removeAt(index)
                                        }
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Remove",
                                        tint = Color.Red,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newService = Service(
                        id = service?.id ?: UUID.randomUUID().toString(),
                        title = title,
                        description = description,
                        price = price,
                        imageRes = selectedImage,
                        availabilities = availabilities,
                        clientBookings = service?.clientBookings ?: emptyList()
                    )
                    onConfirm(newService)
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink)
            ) {
                Text(if (service == null) "Add Service" else "Save Changes")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ServiceCard(
    service: Service,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onViewBookingsClick: () -> Unit
) {
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
            Row {
                Image(
                    painter = painterResource(service.imageRes),
                    contentDescription = service.title,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
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
                }
            }

            if (service.availabilities.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Availability:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))

                service.availabilities.forEach { availability ->
                    Text(
                        text = "${availability.date} from ${availability.startTime} to ${availability.endTime}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }

            if (service.clientBookings.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                val pendingCount = service.clientBookings.count { !it.isCompleted }
                val completedCount = service.clientBookings.count { it.isCompleted }

                Text(
                    text = "Bookings:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Text(
                        text = "$completedCount completed",
                        color = Color(0xFF2E7D32),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$pendingCount pending",
                        color = Color(0xFFFB8C00),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (service.clientBookings.isNotEmpty()) {
                    Button(
                        onClick = onViewBookingsClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPink
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.List, contentDescription = "View Bookings")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("View Bookings")
                    }
                }

                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = PrimaryYellowDark
                        )
                    }
                    IconButton(onClick = onDeleteClick) {
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
}