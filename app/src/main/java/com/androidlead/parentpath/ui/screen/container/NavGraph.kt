package com.androidlead.parentpath.ui.screen.container

sealed class NavGraph(val route: String) {
    data object Welcome: NavGraph(route = "welcome_screen")
    data object Login: NavGraph(route = "login_screen")
    data object Registration: NavGraph(route = "registration_screen")
    data object Home: NavGraph(route = "home_screen")
    data object  Profile :NavGraph(route="profile_screen")
    data object  Service :NavGraph(route="Service_screen")
    data object  Booking :NavGraph(route="Booking_screen")
    data object  Article :NavGraph(route="Article_details")
    data object  Details :NavGraph(route="Service_details")

   }