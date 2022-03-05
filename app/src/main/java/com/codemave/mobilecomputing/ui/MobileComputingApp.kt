package com.codemave.mobilecomputing.ui

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codemave.mobilecomputing.ui.accountCreation.AccountCreation
import com.codemave.mobilecomputing.ui.home.Home
import com.codemave.mobilecomputing.ui.login.Login
import com.codemave.mobilecomputing.ui.editReminder.EditReminder
import com.codemave.mobilecomputing.ui.myProfile.MyProfile
import com.codemave.mobilecomputing.ui.reminder.Reminder


@Composable
fun MobileComputingApp(
    appState: MobileComputingAppState,
    sharedPref : SharedPreferences
) {
    NavHost(
        navController = appState.navController,
        startDestination = "login"
    ) {

        composable(route = "login") {
            Login(navController = appState.navController, sharedPref)
        }
        composable(route = "account creation") {
            AccountCreation(navController = appState.navController, sharedPref)
        }
        composable(route = "home/{username}") {
            route->Home(
                navController = appState.navController,
                (route.arguments?.getString("username")?:"")
            )
        }
        composable(route = "myProfile/{username}") {
                route->
            MyProfile(
            navController = appState.navController,
                sharedPref,
            (route.arguments?.getString("username")?:"")
        )
        }
        composable(route = "reminder") {
            Reminder(onBackPress = appState::navigateBack)
        }
        composable(route = "edit_reminder/{reminderID}") {
            route->EditReminder(
                onBackPress = appState::navigateBack,
                (route.arguments?.getString("reminderID")?:"").toLong()
            )
        }
    }
}
