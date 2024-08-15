package com.project.micss.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object AdminLogin : Screen("admin_login")
    object AdminPortal : Screen("admin_portal")
    // Add more screens here
}

//@Composable
//fun NavGraph(startDestination: String = Screen.AdminLogin.route) {
//    val navController = rememberNavController()
//
//    NavHost(
//        navController = navController,
//        startDestination = startDestination
//    ) {
//        composable(Screen.AdminLogin.route) {
//            AdminLoginScreen(
//                onBack = { /* Handle back action */ },
//                onLoginSuccess = {
//                    navController.navigate(Screen.AdminPortal.route) {
//                        popUpTo(Screen.AdminLogin.route) { inclusive = true }
//                    }
//                }
//            )
//        }
//        composable(Screen.AdminPortal.route) {
//            AdminPortalScreen()
//        }
//        // Add more composable functions here for other screens
//    }
//}