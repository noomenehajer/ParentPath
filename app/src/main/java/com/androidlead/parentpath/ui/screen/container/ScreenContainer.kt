package com.androidlead.parentpath.ui.screen.container

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.androidlead.parentpath.ui.screen.home.HomeScreen
import com.androidlead.parentpath.ui.screen.login.LoginScreen
import com.androidlead.parentpath.ui.screen.profile.ProfileScreen
import com.androidlead.parentpath.ui.screen.registration.RegistrationScreen
import com.androidlead.parentpath.ui.screen.service.AddServiceScreen
import com.androidlead.parentpath.ui.screen.service.BookingScreen
import com.androidlead.parentpath.ui.screen.welcome.WelcomeScreen
import com.androidlead.parentpath.ui.screen.article.ArticleDetails
import com.androidlead.parentpath.ui.screen.service.ServiceDetails

@Composable
fun ScreenContainer() {
    val navHost = rememberNavController()

    NavHost(
        navController = navHost,
        startDestination = NavGraph.Welcome.route
    ) {
        composable(NavGraph.Welcome.route) {
            WelcomeScreen(
                onOpenLoginClicked = {
                    navHost.navigate(NavGraph.Login.route)
                }
            )
        }
        composable(NavGraph.Login.route) {
            LoginScreen(
                onLoginClicked = {
                    navHost.navigate(NavGraph.Home.route)
                },
                onRegistrationClicked = {
                    navHost.navigate(NavGraph.Registration.route)
                }
            )
        }
        composable(NavGraph.Registration.route) {
            RegistrationScreen(
                onRegisterClicked = {
                    navHost.navigate(NavGraph.Home.route)
                },
                onLoginClicked = {
                    navHost.navigate(NavGraph.Login.route)
                }
            )
        }
        composable(NavGraph.Profile.route) {
            ProfileScreen(
                onRestartFlowClicked = {
                    navHost.navigate(NavGraph.Profile.route)
                },
                navHost = navHost
            )
        }
        composable(NavGraph.Service.route) {
            AddServiceScreen(
                onRestartFlowClicked = {
                    navHost.navigate(NavGraph.Service.route)
                },
                navHost = navHost
            )
        }
        composable(NavGraph.Booking.route) {
            BookingScreen(
                onRestartFlowClicked = {
                    navHost.navigate(NavGraph.Booking.route)
                },
                navHost = navHost
            )
        }
        composable(NavGraph.Home.route) {
            HomeScreen(
                onRestartFlowClicked = {
                    navHost.navigate(NavGraph.Welcome.route)
                },
                navHost = navHost
            )
        }
        composable(NavGraph.Article.route) {
            ArticleDetails(
                onRestartFlowClicked = {
                    navHost.navigate(NavGraph.Article.route)
                },
                navHost = navHost
            )
        }
        composable(NavGraph.Details.route) {
            ServiceDetails(
                onRestartFlowClicked = {
                    navHost.navigate(NavGraph.Details.route)
                },
                navHost = navHost
            )
        }
    }
}