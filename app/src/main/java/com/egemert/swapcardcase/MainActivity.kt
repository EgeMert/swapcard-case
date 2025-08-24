package com.egemert.swapcardcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.egemert.swapcardcase.navigation.MainNavGraph
import com.egemert.swapcardcase.navigation.RouterImpl
import com.egemert.swapcardcase.screen.UserListScreen
import com.egemert.swapcardcase.ui.theme.SwapCardCaseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SwapCardCaseTheme {
                val navController = rememberNavController()
                val router = remember { RouterImpl(navController) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavGraph(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(8.dp),
                        navController = navController,
                        router = router
                    )
                }
            }
        }
    }
}
