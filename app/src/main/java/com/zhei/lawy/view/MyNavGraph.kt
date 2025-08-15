package com.zhei.lawy.view
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zhei.lawy.view.ui.mainscreen.MainScreen
import com.zhei.lawy.view.ui.splashscreen.SplashScreenLawy
import com.zhei.lawy.view.viewmodel.MainScreenViewModel
import com.zhei.lawy.view.viewmodel.SplashScreenViewModel

@Composable fun MyNavGraph ()
{
    val navHost = rememberNavController()

    val viewMainS: MainScreenViewModel = viewModel()
    val viewSplash: SplashScreenViewModel = viewModel()

    NavHost(navController = navHost, startDestination = Screens.SplashScreen) {

        composable<Screens.SplashScreen> {
            SplashScreenLawy (
                viewSplash = viewSplash,
                onNavigate = {
                    navHost.navigate(Screens.MainScreen) {
                        launchSingleTop = true
                        popUpTo<Screens.SplashScreen> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Screens.MainScreen> (
            enterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(1500))
            }
        ) {
            MainScreen(viewMainS = viewMainS)
        }
    }
}