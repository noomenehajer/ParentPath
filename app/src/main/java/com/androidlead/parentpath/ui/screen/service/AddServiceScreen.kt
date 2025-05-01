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

data class Service(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val category: String,
    val imageRes: Int,
    val availabilities: List<Availability> = emptyList()
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

    // Sample services data - moved inside the composable
    val services = remember {
        mutableStateListOf(
            Service("1", "Babysitting", "Professional childcare services", "15TND/hr", "Childcare", R.drawable.babysitter),
            Service("2", "Tutoring", "Math and science tutoring", "20TND/hr", "Education", R.drawable.tutor),
            Service("3", "Fitness", "Fitness health care", "12TND/hr", "Pets", R.drawable.fitness)
        )
    }

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
                    ServiceCard(
                        service = service,
                        onEditClick = {
                            editingService = service
                            showEditServiceDialog = true
                        },
                        onDeleteClick = {
                            services.remove(service)
                        }
                    )
                }
            }

            // Add Service Dialog
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

            // Edit Service Dialog
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
    var category by remember { mutableStateOf(service?.category ?: "") }
    var selectedImage by remember { mutableStateOf(service?.imageRes ?: R.drawable.cleaning) }
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
            // In a real app, handle the image URI here
            selectedImage = R.drawable.babysitter
        }
    )

    // Date Picker Dialog
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

    // Start Time Picker Dialog
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
            true // 24 hour format
        )
        timePickerDialog.show()
    }

    // End Time Picker Dialog
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

                // Format the date and times
                val dateStr = "${selectedDate?.get(Calendar.DAY_OF_MONTH)}/${selectedDate?.get(Calendar.MONTH)?.plus(1)}/${selectedDate?.get(Calendar.YEAR)}"
                val startTimeStr = "${selectedStartTime?.get(Calendar.HOUR_OF_DAY)}:${selectedStartTime?.get(Calendar.MINUTE)?.toString()?.padStart(2, '0')}"
                val endTimeStr = "${selectedEndTime?.get(Calendar.HOUR_OF_DAY)}:${selectedEndTime?.get(Calendar.MINUTE)?.toString()?.padStart(2, '0')}"

                availabilities = availabilities + Availability(dateStr, startTimeStr, endTimeStr)
                showEndTimePicker = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // 24 hour format
        )
        timePickerDialog.show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (service == null) "Add New Service" else "Edit Service") },
        text = {
            Column {
                // Image selection
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
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Availability section
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
                        id = "0",
                        title = title,
                        description = description,
                        price = price,
                        category = category,
                        imageRes = selectedImage,
                        availabilities = availabilities
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
    onDeleteClick: () -> Unit
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
                // Image on the left
                Image(
                    painter = painterResource(service.imageRes),
                    contentDescription = service.title,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Content on the right
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

                    Text(
                        text = service.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryYellowDark,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(PrimaryYellowLight.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Availability section
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

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
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