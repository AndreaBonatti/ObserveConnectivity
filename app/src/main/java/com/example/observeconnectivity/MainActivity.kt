package com.example.observeconnectivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.observeconnectivity.ui.theme.ObserveConnectivityTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : ComponentActivity() {

    private lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        // This part live as long the lifecycleScope exists instead as long as the Activity exist
        connectivityObserver.observe().onEach {
            println("Status is $it")
        }.launchIn(lifecycleScope)

        setContent {
            ObserveConnectivityTheme {
                val status by connectivityObserver.observe().collectAsState(
                    initial = ConnectivityObserver.Status.Unavailable
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Network status: $status")
                }
            }
        }
    }
}