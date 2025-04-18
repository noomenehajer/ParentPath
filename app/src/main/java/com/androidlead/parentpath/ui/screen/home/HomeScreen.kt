package com.androidlead.parentpath.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.androidlead.parentpath.R
import com.androidlead.parentpath.ui.screen.container.NavGraph
import com.androidlead.parentpath.ui.theme.*
import kotlinx.coroutines.launch

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
    val imageResId: Int
)

data class Article(
    val title: String,
    val description: String,
    val imageResId: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onRestartFlowClicked: () -> Unit,
    navHost: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }

    val services = listOf(
        Service("Math Tutoring", "Sarah M.", "Expert in algebra and geometry", R.drawable.tutor),
        Service("Child Care", "Lina A.", "Experienced babysitter available anytime", R.drawable.babysitter),
        Service("Fitness Coach", "Hassan B.", "Personalized health programs", R.drawable.fitness),
        Service("Home Cleaning", "Maya K.", "Fast, reliable and spotless service", R.drawable.cleaning),
        Service("Delivery", "Ali R.", "Comfortable transport for school or errands", R.drawable.delivery)
    )

    val articles = listOf(
        Article("5 Parenting Tips", "Connect better with kids", R.drawable.article1),
        Article("Healthy Snacks", "Nutritious recipes for children.", R.drawable.article2),
        Article("Family Time Activities", "Fun things to do together at home.", R.drawable.article3)
    )

    val menuItems = listOf(
        MenuItem("Home", Icons.Default.Home) { navHost.navigate(NavGraph.Home.route)},
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
                        text = "Hello Emna",
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
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.DarkGray
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        actionIconContentColor = Color.DarkGray
                    )
                )
            }
        ) { paddingValues ->
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
                    // Search Bar
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

                    // Categories
                    val categories = listOf("Tutoring", "Babysitting", "Health", "Cleaning", "Transport")
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

                // Services Section
                item {
                    Text(
                        text = "Services",
                        modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.DarkGray
                    )
                }

                items(services) { service ->
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
                                Text(
                                    text = service.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.DarkGray,
                                    maxLines = 2,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                Button(
                                    onClick = { /* handle view details */ },
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

                // Articles Section
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
                                    .height(240.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    // Full-width image with aspect ratio
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp)
                                            .clip(
                                                RoundedCornerShape(
                                                    topStart = 20.dp,
                                                    topEnd = 20.dp
                                                )
                                            )
                                    ) {
                                        Image(
                                            painter = painterResource(article.imageResId),
                                            contentDescription = article.title,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        // Gradient overlay for better text visibility
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
    }
}