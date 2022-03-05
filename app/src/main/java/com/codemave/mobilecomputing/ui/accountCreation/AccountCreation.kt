package com.codemave.mobilecomputing.ui.accountCreation

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codemave.mobilecomputing.R
import com.google.accompanist.insets.systemBarsPadding



@Composable
fun AccountCreation(
    navController: NavController,
    sharedPref : SharedPreferences
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val username = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TopAppBar (
                modifier = Modifier,
                backgroundColor = Color.Transparent,
                contentColor = Color.Gray,
            ){

                IconButton( onClick = { navController.navigate(route = "login") } ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.account)
                    )
                }
                Text(text = "Account Creation")
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val appBarColor = MaterialTheme.colors.secondary.copy(alpha = 0.87f)


            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = username.value,
                onValueChange = { data -> username.value = data },
                label = { Text("Username")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { data -> password.value = data },
                label = { Text("Password")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(10.dp))



            Button(
                onClick = {
                    if (registerNewAccount(sharedPref, username.value, password.value)) {
                        navController.navigate("login")
                    }
                },
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(55.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "Save new account")
            }
        }
    }
}


fun registerNewAccount(
    sharedPref : SharedPreferences,
    username : String,
    password : String
) : Boolean
{
    //verify that the username is not already used
    return if(!sharedPref.contains(username)) {
        val myEdit: SharedPreferences.Editor = sharedPref.edit()
        myEdit.putString(username, password)
        myEdit.commit()
        true
    } else {
        false
    }
}

