
    package com.androidlead.parentpath.ui.screen.home

    import androidx.compose.foundation.Image
            import androidx.compose.foundation.background
            import androidx.compose.foundation.clickable
            import androidx.compose.foundation.layout.*
            import androidx.compose.foundation.lazy.LazyColumn
            import androidx.compose.foundation.lazy.LazyRow
            import androidx.compose.foundation.lazy.items
            import androidx.compose.foundation.shape.CircleShape
            import androidx.compose.foundation.shape.RoundedCornerShape
            import androidx.compose.foundation.text.KeyboardActions
            import androidx.compose.foundation.text.KeyboardOptions
            import androidx.compose.material.icons.Icons
            import androidx.compose.material.icons.filled.*
            import androidx.compose.material.icons.outlined.Star
            import androidx.compose.material3.*
            import androidx.compose.runtime.*
            import androidx.compose.ui.Alignment
            import androidx.compose.ui.Modifier
            import androidx.compose.ui.draw.clip
            import androidx.compose.ui.draw.shadow
            import androidx.compose.ui.graphics.Brush
            import androidx.compose.ui.graphics.Color
            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.layout.ContentScale
            import androidx.compose.ui.res.painterResource
            import androidx.compose.ui.text.font.FontWeight
            import androidx.compose.ui.text.input.ImeAction
            import androidx.compose.ui.text.style.TextAlign
            import androidx.compose.ui.unit.dp
            import androidx.compose.ui.window.Dialog
            import androidx.navigation.NavController
            import androidx.navigation.compose.rememberNavController
            import com.androidlead.parentpath.R
            import com.androidlead.parentpath.ui.screen.container.NavGraph
            import com.androidlead.parentpath.ui.theme.*
            import kotlinx.coroutines.delay
            import kotlinx.coroutines.launch
            import java.util.*

// Data Models
            data class MenuItem(
        val title: String,
        val icon: ImageVector,
        val onClick: () -> Unit
    )

    data class Service(
        val name: String,
        val provider: String,
        val description: String,
        val imageResId: Int,
        val rating: Float = 0f
    )

    data class Article(
        val title: String,
        val description: String,
        val imageResId: Int
    )

    data class ChatMessage(
        val text: String,
        val isUser: Boolean,
        val timestamp: Long = System.currentTimeMillis()
    )

    // Notification Data Models
    data class BookingApprovalNotification(
        val id: String,
        val serviceName: String,
        val providerName: String,
        val bookingDate: String,
        var isApproved: Boolean? = null
    )

    data class ServiceCompletionNotification(
        val id: String,
        val serviceName: String,
        val providerName: String,
        val bookingDate: String,
        var status: CompletionStatus = CompletionStatus.PENDING_CONFIRMATION,
        val timestamp: Long = System.currentTimeMillis()
    )

    enum class CompletionStatus {
        PENDING_CONFIRMATION,
        CONFIRMED,
        DISPUTED,
        AUTO_CONFIRMED
    }

    @Composable
    fun StarRating(
        rating: Float,
        modifier: Modifier = Modifier,
        stars: Int = 5,
        starSize: Int = 16
    ) {
        Row(modifier = modifier) {
            for (i in 1..stars) {
                val starIcon = if (i <= rating) {
                    Icons.Outlined.Star
                } else if (i - 0.5 <= rating) {
                    Icons.Outlined.Star
                } else {
                    Icons.Outlined.Star
                }

                Icon(
                    imageVector = starIcon,
                    contentDescription = "Star $i",
                    tint = if (i <= rating) PrimaryPink else Color.LightGray,
                    modifier = Modifier.size(starSize.dp)
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "%.1f".format(rating),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }

    @Composable
    fun ChatBotDialog(
        onDismiss: () -> Unit,
        messages: List<ChatMessage>,
        onSendMessage: (String) -> Unit,
        modifier: Modifier = Modifier
    ) {
        var userInput by remember { mutableStateOf("") }

        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(min = 300.dp, max = 500.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    // Header with smaller bot icon
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PrimaryPink)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.bot),
                            contentDescription = "AI Assistant",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Parenting Assistant",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = onDismiss) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }

                    // Messages
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        reverseLayout = true
                    ) {
                        items(messages.reversed()) { message ->
                            MessageBubble(message = message)
                        }
                    }

                    // Input
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = userInput,
                            onValueChange = { userInput = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Ask about parenting...") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                cursorColor = Color.Black,
                                focusedBorderColor = PrimaryPink,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    if (userInput.isNotBlank()) {
                                        onSendMessage(userInput)
                                        userInput = ""
                                    }
                                }
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (userInput.isNotBlank()) {
                                    onSendMessage(userInput)
                                    userInput = ""
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = "Send",
                                tint = PrimaryPink
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MessageBubble(message: ChatMessage) {
        val alignment = if (message.isUser) Alignment.End else Alignment.Start
        val bubbleColor = if (message.isUser) PrimaryPink else Color.LightGray
        val textColor = if (message.isUser) Color.White else Color.Black

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalAlignment = alignment
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = if (message.isUser) 16.dp else 0.dp,
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = if (message.isUser) 0.dp else 16.dp
                        )
                    )
                    .background(bubbleColor)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = message.text,
                    color = textColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen(
        modifier: Modifier = Modifier,
        onRestartFlowClicked: () -> Unit,
        navHost: NavController
    ) {
        var showApprovalConfirmation by remember { mutableStateOf(false) }
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var searchQuery by remember { mutableStateOf("") }
        var showNotifications by remember { mutableStateOf(false) }
        var showChatBot by remember { mutableStateOf(false) }
        val chatMessages = remember { mutableStateListOf<ChatMessage>(
            ChatMessage("Hello! I'm your parenting assistant. How can I help you today?", false)
        )}

        // Notification states
        val bookingApprovalNotifications = remember { mutableStateListOf(
            BookingApprovalNotification(
                id = "1",
                serviceName = "Babysitting",
                providerName = "Mohamed ",
                bookingDate = "2023-06-15 at 14:00"
            )
        )}

        val serviceCompletionNotifications = remember { mutableStateListOf(
            ServiceCompletionNotification(
                id = "2",
                serviceName = "Tutoring",
                providerName = "Sarra ",
                bookingDate = "2023-06-16 at 10:00"
            )
        )}

        // Dialog states
        var selectedBookingApproval by remember { mutableStateOf<BookingApprovalNotification?>(null) }
        var showBookingApprovalDialog by remember { mutableStateOf(false) }
        var selectedServiceCompletion by remember { mutableStateOf<ServiceCompletionNotification?>(null) }
        var showCompletionDialog by remember { mutableStateOf(false) }
        var showDisputeDialog by remember { mutableStateOf(false) }

        val services = listOf(
            Service("Home Cleaning", "Ali ", "Fast, reliable and spotless service", R.drawable.cleaning, 5f),
            Service("Math Tutoring", "Sarra ", "Expert in algebra and geometry", R.drawable.tutor, 4.8f),
            Service("Delivery", "Amir ", "Comfortable transport for school or errands", R.drawable.delivery, 4f),
            Service("Babysitting", "Sarra ", "Experienced babysitter available anytime", R.drawable.babysitter, 3.5f),
            Service("Fitness Coach", "Sarra ", "Personalized health programs", R.drawable.fitness, 2f)
        )

        val articles = listOf(
            Article("The Art of Parenting", "Tips for Happy Kids", R.drawable.article1),
            Article("Healthy Snacks", "Nutritious recipes for children", R.drawable.article2),
            Article("Family Time Activities", "Fun things to do together at home", R.drawable.article3)
        )

        val menuItems = listOf(
            MenuItem("Home", Icons.Default.Home) { navHost.navigate(NavGraph.Home.route)},
            MenuItem("Edit Profile", Icons.Default.Person) { navHost.navigate(NavGraph.Profile.route) },
            MenuItem("Offer a Service", Icons.Default.Add) { navHost.navigate(NavGraph.Service.route) },
            MenuItem("Booking List", Icons.Default.List) { navHost.navigate(NavGraph.Booking.route) }
        )

        // Handle booking approval
        fun approveBooking(notification: BookingApprovalNotification, isApproved: Boolean) {
            if (isApproved) {
                // Show confirmation dialog for approval
                selectedBookingApproval = notification
                showApprovalConfirmation = true
            } else {
                // Reject directly without confirmation
                val index = bookingApprovalNotifications.indexOfFirst { it.id == notification.id }
                if (index != -1) {
                    bookingApprovalNotifications[index] = notification.copy(isApproved = false)
                }
                showBookingApprovalDialog = false
            }
        }

        // Handle service completion confirmation
        fun confirmServiceCompletion(notification: ServiceCompletionNotification) {
            val index = serviceCompletionNotifications.indexOfFirst { it.id == notification.id }
            if (index != -1) {
                serviceCompletionNotifications[index] = notification.copy(status = CompletionStatus.CONFIRMED)
            }
            showCompletionDialog = false
        }

        // Handle service completion dispute
        fun disputeServiceCompletion(notification: ServiceCompletionNotification) {
            val index = serviceCompletionNotifications.indexOfFirst { it.id == notification.id }
            if (index != -1) {
                serviceCompletionNotifications[index] = notification.copy(status = CompletionStatus.DISPUTED)
            }
            showDisputeDialog = false
            navHost.navigate(NavGraph.Booking.route) // Navigate to booking for reclamation
        }

        // Auto-confirm service completion after 24 hours
        LaunchedEffect(serviceCompletionNotifications) {
            while (true) {
                delay(1000) // Check every second (for demo purposes)

                val now = System.currentTimeMillis()
                serviceCompletionNotifications.forEach { notification ->
                    if (notification.status == CompletionStatus.PENDING_CONFIRMATION &&
                        now - notification.timestamp > 24 * 60 * 60 * 1000) {
                        val index = serviceCompletionNotifications.indexOfFirst { it.id == notification.id }
                        if (index != -1) {
                            serviceCompletionNotifications[index] =
                                notification.copy(status = CompletionStatus.AUTO_CONFIRMED)
                        }
                    }
                }
            }
        }

        fun sendMessage(message: String) {
            chatMessages.add(ChatMessage(message, true))

            // Generate a thoughtful parenting response based on the user's question
            val response = when {
                message.contains("hello", ignoreCase = true) -> {
                    "Hello there! 👋 I'm your parenting assistant. How can I help you today?"
                }
                message.contains("thank you", ignoreCase = true) || message.contains("thanks", ignoreCase = true) -> {
                    "You're very welcome! 😊 Remember, parenting is a journey and you're doing great. " +
                            "Is there anything else I can help you with?"
                }
                message.contains("sleep", ignoreCase = true) -> {
                    "For better sleep routines, establish a consistent bedtime schedule. " +
                            "For infants, try soothing activities before bed. " +
                            "Toddlers benefit from a predictable routine (bath, story, bed)."
                }
                message.contains("tantrum", ignoreCase = true) -> {
                    "When handling tantrums:\n1. Stay calm\n2. Acknowledge their feelings\n" +
                            "3. Offer comfort\n4. Don't give in to unreasonable demands\n" +
                            "5. Teach better ways to express emotions as they calm down."
                }
                message.contains("discipline", ignoreCase = true) -> {
                    "Effective discipline strategies:\n- Set clear expectations\n" +
                            "- Use positive reinforcement\n- Be consistent with consequences\n" +
                            "- Model good behavior\n- Redirect negative behavior when possible."
                }
                message.contains("nutrition", ignoreCase = true) || message.contains("eat", ignoreCase = true) -> {
                    "Nutrition tips for kids:\n- Offer variety of fruits/vegetables\n" +
                            "- Limit processed foods\n- Make mealtimes pleasant\n" +
                            "- Involve kids in meal prep\n- Don't force eating - offer healthy options and let them choose."
                }
                message.contains("screen time", ignoreCase = true) -> {
                    "Screen time recommendations:\n- Under 2: Avoid except video chatting\n" +
                            "- 2-5: 1 hour/day of high-quality programs\n- 6+: Consistent limits\n" +
                            "Tips: Co-view when possible, avoid screens before bedtime, create tech-free zones."
                }
                message.contains("activity", ignoreCase = true) || message.contains("play", ignoreCase = true) -> {
                    "Great developmental activities:\n- For toddlers: Sensory play, blocks, simple puzzles\n" +
                            "- Preschoolers: Pretend play, arts/crafts, outdoor play\n" +
                            "- School-age: Sports, board games, reading together\n" +
                            "Unstructured play is crucial for development!"
                }
                else -> {
                    // General parenting advice for unrecognized questions
                    "Parenting can be challenging but rewarding. Some general tips:\n" +
                            "- Be patient and consistent\n- Show unconditional love\n" +
                            "- Listen actively to your child\n- Set reasonable boundaries\n" +
                            "- Take care of yourself too - you can't pour from an empty cup!\n\n" +
                            "Would you like more specific advice about sleep, nutrition, discipline, or another topic?"
                }
            }

            scope.launch {
                delay(1000) // Simulate thinking time
                chatMessages.add(ChatMessage(response, false))
            }
        }

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
            Box(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = "♡ Here For You ♡",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.DarkGray
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(
                                        Icons.Default.Menu,
                                        contentDescription = "Menu",
                                        tint = Color.DarkGray
                                    )
                                }
                            },
                            actions = {
                                Box {
                                    IconButton(onClick = { showNotifications = !showNotifications }) {
                                        Icon(
                                            imageVector = Icons.Default.Notifications,
                                            contentDescription = "Notifications",
                                            tint = Color.DarkGray
                                        )
                                    }

                                    // Show badge if there are pending notifications
                                    val pendingCount = bookingApprovalNotifications.count { it.isApproved == null } +
                                            serviceCompletionNotifications.count { it.status == CompletionStatus.PENDING_CONFIRMATION }

                                    if (pendingCount > 0) {
                                        Badge(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .align(Alignment.TopEnd)
                                        ) {
                                            Text(
                                                text = pendingCount.toString(),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White
                                            )
                                        }
                                    }

                                    DropdownMenu(
                                        expanded = showNotifications,
                                        onDismissRequest = { showNotifications = false },
                                        modifier = Modifier
                                            .width(300.dp)
                                            .padding(8.dp)
                                    ) {
                                        // Booking Approval Notifications Section
                                        if (bookingApprovalNotifications.isNotEmpty()) {
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = "Booking Approvals",
                                                        style = MaterialTheme.typography.labelLarge,
                                                        fontWeight = FontWeight.Bold,
                                                        color = PrimaryPink
                                                    )
                                                },
                                                onClick = {}
                                            )
                                            Divider()

                                            bookingApprovalNotifications.forEach { notification ->
                                                if (notification.isApproved == null) {
                                                    DropdownMenuItem(
                                                        text = {
                                                            Column {
                                                                Text(
                                                                    text = "${notification.providerName} is awaiting your approval",
                                                                    style = MaterialTheme.typography.bodyMedium,
                                                                    fontWeight = FontWeight.SemiBold
                                                                )
                                                                Text(
                                                                    text = "${notification.serviceName} on ${notification.bookingDate}",
                                                                    style = MaterialTheme.typography.bodySmall,
                                                                    color = Color.Gray
                                                                )
                                                            }
                                                        },
                                                        onClick = {
                                                            selectedBookingApproval = notification
                                                            showBookingApprovalDialog = true
                                                            showNotifications = false
                                                        },
                                                        trailingIcon = {
                                                            Row {
                                                                IconButton(
                                                                    onClick = {
                                                                        selectedBookingApproval = notification
                                                                        approveBooking(notification, false)
                                                                    }
                                                                ) {
//                                                                    Icon(
//                                                                        Icons.Default.Close,
//                                                                        contentDescription = "Reject",
//                                                                        tint = Color.Red
//                                                                    )
                                                                }
                                                                IconButton(
                                                                    onClick = {
                                                                        selectedBookingApproval = notification
                                                                        approveBooking(notification, true)
                                                                    }
                                                                ) {
                                                                    Icon(
                                                                        Icons.Default.Check,
                                                                        contentDescription = "Approve",
                                                                        tint = Color.Green
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    )
                                                }
                                            }
                                        }

                                        // Service Completion Notifications Section
                                        if (serviceCompletionNotifications.isNotEmpty()) {
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = "Service Completions",
                                                        style = MaterialTheme.typography.labelLarge,
                                                        fontWeight = FontWeight.Bold,
                                                        color = PrimaryPink
                                                    )
                                                },
                                                onClick = {}
                                            )
                                            Divider()

                                            serviceCompletionNotifications.forEach { notification ->
                                                DropdownMenuItem(
                                                    text = {
                                                        Column {
                                                            Text(
                                                                text = when (notification.status) {
                                                                    CompletionStatus.PENDING_CONFIRMATION ->
                                                                        "Confirm service completion"
                                                                    CompletionStatus.CONFIRMED ->
                                                                        "Service completed"
                                                                    CompletionStatus.DISPUTED ->
                                                                        "Service disputed"
                                                                    CompletionStatus.AUTO_CONFIRMED ->
                                                                        "Service auto-confirmed"
                                                                },
                                                                style = MaterialTheme.typography.bodyMedium,
                                                                fontWeight = FontWeight.SemiBold,
                                                                color = when (notification.status) {
                                                                    CompletionStatus.PENDING_CONFIRMATION -> Color.White
                                                                    CompletionStatus.CONFIRMED -> Color(0xFF2E7D32)
                                                                    CompletionStatus.DISPUTED -> Color(0xFFD32F2F)
                                                                    CompletionStatus.AUTO_CONFIRMED -> Color(0xFF1976D2)
                                                                }
                                                            )
                                                            Text(
                                                                text = "${notification.serviceName} by ${notification.providerName}",
                                                                style = MaterialTheme.typography.bodySmall,
                                                                color = Color.Gray
                                                            )
                                                            Text(
                                                                text = notification.bookingDate,
                                                                style = MaterialTheme.typography.bodySmall,
                                                                color = Color.Gray
                                                            )
                                                        }
                                                    },
                                                    onClick = {
                                                        if (notification.status == CompletionStatus.PENDING_CONFIRMATION) {
                                                            selectedServiceCompletion = notification
                                                            showCompletionDialog = true
                                                            showNotifications = false
                                                        }
                                                    },
                                                    trailingIcon = {
                                                        when (notification.status) {
                                                            CompletionStatus.PENDING_CONFIRMATION -> {
                                                                Row {
                                                                    IconButton(
                                                                        onClick = {
                                                                            selectedServiceCompletion = notification
                                                                            showDisputeDialog = true
                                                                            showNotifications = false
                                                                        }
                                                                    ) {
                                                                        Icon(
                                                                            Icons.Default.Close,
                                                                            contentDescription = "Dispute",
                                                                            tint = Color.Red
                                                                        )
                                                                    }
                                                                    IconButton(
                                                                        onClick = {
                                                                            selectedServiceCompletion = notification
                                                                            showCompletionDialog = true
                                                                            showNotifications = false
                                                                        }
                                                                    ) {
                                                                        Icon(
                                                                            Icons.Default.Check,
                                                                            contentDescription = "Confirm",
                                                                            tint = Color.Green
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                            CompletionStatus.CONFIRMED -> {
                                                                Icon(
                                                                    Icons.Default.Check,
                                                                    contentDescription = "Confirmed",
                                                                    tint = Color(0xFF2E7D32)
                                                                )
                                                            }
                                                            CompletionStatus.DISPUTED -> {
                                                                Icon(
                                                                    Icons.Default.Close,
                                                                    contentDescription = "Disputed",
                                                                    tint = Color(0xFFD32F2F)
                                                                )
                                                            }
                                                            CompletionStatus.AUTO_CONFIRMED -> {
                                                                Icon(
                                                                    Icons.Default.DateRange,
                                                                    contentDescription = "Auto-confirmed",
                                                                    tint = Color(0xFF1976D2)
                                                                )
                                                            }
                                                        }
                                                    }
                                                )
                                            }
                                        }

                                        if (bookingApprovalNotifications.isEmpty() && serviceCompletionNotifications.isEmpty()) {
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = "No new notifications",
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                },
                                                onClick = { showNotifications = false }
                                            )
                                        }
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent,
                                actionIconContentColor = Color.DarkGray
                            )
                        )
                    }
                ) { paddingValues ->
                    // Add this with your other dialogs in the Scaffold content
                    if (showApprovalConfirmation && selectedBookingApproval != null) {
                        AlertDialog(
                            onDismissRequest = { showApprovalConfirmation = false },
                            title = { Text("Confirm Approval") },
                            text = {
                                Text(
                                    text = "Are you sure you want to approve ${selectedBookingApproval?.providerName}'s ${selectedBookingApproval?.serviceName} request?",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        val index = bookingApprovalNotifications.indexOfFirst { it.id == selectedBookingApproval?.id }
                                        if (index != -1) {
                                            bookingApprovalNotifications[index] = selectedBookingApproval!!.copy(isApproved = true)
                                        }
                                        showApprovalConfirmation = false
                                        showBookingApprovalDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink)
                                ) {
                                    Text("Approve")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showApprovalConfirmation = false }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                    // Replace your existing booking approval dialog with this
                    if (showBookingApprovalDialog && selectedBookingApproval != null) {
                        AlertDialog(
                            onDismissRequest = { showBookingApprovalDialog = false },
                            title = { Text("Booking Request") },
                            text = {
                                Column {
                                    Text(
                                        text = "${selectedBookingApproval?.providerName} requests to book:",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = selectedBookingApproval?.serviceName ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray
                                    )
                                    Text(
                                        text = "Scheduled for ${selectedBookingApproval?.bookingDate}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        selectedBookingApproval?.let { approveBooking(it, true) }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPink)
                                ) {
                                    Text("Approve")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        selectedBookingApproval?.let { approveBooking(it, false) }
                                    }
                                ) {
                                    Text("Reject")
                                }
                            }
                        )
                    }

                    // Service Completion Confirmation Dialog
                    if (showCompletionDialog && selectedServiceCompletion != null) {
                        AlertDialog(
                            onDismissRequest = { showCompletionDialog = false },
                            title = { Text("Confirm Service Completion") },
                            text = {
                                Column {
                                    Text(
                                        text = "Please confirm that ${selectedServiceCompletion?.providerName} has completed the service:",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${selectedServiceCompletion?.serviceName} on ${selectedServiceCompletion?.bookingDate}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "By confirming, you acknowledge that the service was completed to your satisfaction.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        selectedServiceCompletion?.let { confirmServiceCompletion(it) }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryPink
                                    )
                                ) {
                                    Text("Confirm Completion")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showCompletionDialog = false }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }

                    // Service Dispute Dialog
                    if (showDisputeDialog && selectedServiceCompletion != null) {
                        AlertDialog(
                            onDismissRequest = { showDisputeDialog = false },
                            title = { Text("Dispute Service Completion") },
                            text = {
                                Column {
                                    Text(
                                        text = "Are you sure you want to dispute the completion of this service?",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${selectedServiceCompletion?.serviceName} by ${selectedServiceCompletion?.providerName}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "You will be able to provide details about the issue in the next screen.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        selectedServiceCompletion?.let { disputeServiceCompletion(it) }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFD32F2F)
                                    )
                                ) {
                                    Text("Dispute Service")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showDisputeDialog = false }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }

                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    0f to PrimaryPinkBlended,
                                    0.6f to PrimaryYellowLight,
                                    1f to PrimaryYellow
                                )
                            )
                            .padding(paddingValues),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        item {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                placeholder = { Text("Search...", color = Color.DarkGray) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = Color.DarkGray
                                    )
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedBorderColor = Color.Gray,
                                    unfocusedBorderColor = Color.LightGray,
                                    cursorColor = Color.Black,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = { })
                            )

                            val categories = listOf("Math Tutoring", "Babysitting", "Health", "Cleaning", "Transport")
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(categories) { category ->
                                    ElevatedAssistChip(
                                        onClick = {},
                                        label = {
                                            Text(
                                                text = category,
                                                color = Color.DarkGray,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        },
                                        shape = RoundedCornerShape(20.dp),
                                        colors = AssistChipDefaults.elevatedAssistChipColors(
                                            containerColor = Color.White
                                        )
                                    )
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Services",
                                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.DarkGray
                            )
                        }

                        val filteredServices = services.filter {
                            it.name.contains(searchQuery, ignoreCase = true)
                        }

                        if (filteredServices.isEmpty()) {
                            item {
                                Text(
                                    text = "Oops! No services in your area yet.",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center
                                    ),
                                    color = Color.DarkGray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            items(filteredServices) { service ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = service.imageResId),
                                            contentDescription = service.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(100.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = service.name,
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = Color.Black
                                            )
                                            Text(
                                                text = "by ${service.provider}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray
                                            )
                                            StarRating(
                                                rating = service.rating,
                                                modifier = Modifier.padding(vertical = 2.dp)
                                            )
                                            Text(
                                                text = service.description,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.DarkGray,
                                                maxLines = 2,
                                                modifier = Modifier.padding(vertical = 4.dp)
                                            )
                                            Button(
                                                onClick = { navHost.navigate(NavGraph.Details.route) },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = PrimaryPink
                                                )
                                            ) {
                                                Text(
                                                    text = "View Details",
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Text(
                                text = "Articles",
                                modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 12.dp),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.DarkGray
                            )
                        }

                        item {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                items(articles) { article ->
                                    Card(
                                        modifier = Modifier
                                            .width(300.dp)
                                            .height(240.dp)
                                            .clickable { navHost.navigate(NavGraph.Article.route) },
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(150.dp)
                                                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                                            ) {
                                                Image(
                                                    painter = painterResource(article.imageResId),
                                                    contentDescription = article.title,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .background(
                                                            Brush.verticalGradient(
                                                                colors = listOf(
                                                                    Color.Transparent,
                                                                    Color.Black.copy(alpha = 0.3f)
                                                                ),
                                                                startY = 100f
                                                            )
                                                        )
                                                )
                                            }

                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp)
                                            ) {
                                                Text(
                                                    text = article.title,
                                                    style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = Color.Black,
                                                    maxLines = 1
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = article.description,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color.DarkGray,
                                                    maxLines = 2
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = "Read more →",
                                                    style = MaterialTheme.typography.labelMedium.copy(
                                                        fontWeight = FontWeight.SemiBold
                                                    ),
                                                    color = PrimaryPink
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Floating Chatbot Button with optimized size
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    FloatingActionButton(
                        onClick = { showChatBot = true },
                        containerColor = PrimaryPink,
                        contentColor = Color.White,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.bot),
                            contentDescription = "Chat with Parenting Assistant",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                if (showChatBot) {
                    ChatBotDialog(
                        onDismiss = { showChatBot = false },
                        messages = chatMessages,
                        onSendMessage = { message -> sendMessage(message) }
                    )
                }
            }
        }
    }