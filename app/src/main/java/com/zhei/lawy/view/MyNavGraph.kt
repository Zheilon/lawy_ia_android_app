package com.zhei.lawy.view
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zhei.lawy.view.ui.MainScreen
import com.zhei.lawy.view.viewmodel.MainScreenViewModel

@Composable fun MyNavGraph ()
{
    val navHost = rememberNavController()

    NavHost(navController = navHost, startDestination = Screens.MainScreen) {

        composable<Screens.MainScreen> {
            MainScreen(navHost = navHost)
        }

    }
}