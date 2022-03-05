package com.codemave.mobilecomputing.ui.login

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun Login(
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
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
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

            var loginOK : Boolean = CheckLogin(sharedPref, username.value, password.value)

            Button(
                onClick = {
                    if (loginOK) {
                        navController.navigate("home/${username.value}")
                    }
                },
                enabled = true,
                modifier = Modifier.fillMaxWidth().size(55.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { navController.navigate("account creation") },
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(55.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "Create a new account")
            }

            //To clear room database
/*
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    database.clearAllTables()
                          },
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(55.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "clear room database")
            }
            */
        }
    }
}

@Composable
fun CheckLogin(
    sharedPref : SharedPreferences,
    username : String,
    password : String
) : Boolean
{
    return if(sharedPref.contains(username)) {
        val p: String = sharedPref.getString(username, null).toString()
        p==password
    } else {
        false
    }
}