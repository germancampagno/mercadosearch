package com.germancampagno.mercadosearch.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.germancampagno.mercadosearch.ui.navigation.MainNavHost
import com.germancampagno.mercadosearch.ui.theme.MercadoSearchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MercadoSearchTheme {
                Surface {
                    MainNavHost()
                }
            }
        }
    }
}