package com.codemave.mobilecomputing.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.codemave.mobilecomputing.ui.theme.MobileComputingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var sharedPref : SharedPreferences

        super.onCreate(savedInstanceState)
        setContent {

            sharedPref = getSharedPreferences("MysharedPref" , MODE_PRIVATE)
            val myEdit: SharedPreferences.Editor = sharedPref.edit()

            //to remove all the logins :
            //myEdit.clear()

            myEdit.commit()


            MobileComputingTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MobileComputingApp(rememberMobileComputingAppState(),sharedPref)
                }
            }
        }
    }
}
