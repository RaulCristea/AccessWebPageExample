package com.example.accesswebpageexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.accesswebpageexample.ui.theme.AppTheme
import com.example.accesswebpageexample.ui.webpage.WebPageScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                WebPageScreen()
            }
        }
    }
}
