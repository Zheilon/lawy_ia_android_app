package com.zhei.lawy.view.ui.splashscreen
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.zhei.lawy.MyFont
import com.zhei.lawy.view.viewmodel.SplashScreenViewModel
import kotlinx.coroutines.delay


@Preview
@Composable fun PreviewSplash ()
{
    SplashScreenLawy(
        onNavigate = {},
        viewSplash = remember { SplashScreenViewModel() }
    )
}


@Composable fun SplashScreenLawy (
    viewSplash: SplashScreenViewModel,
    onNavigate: () -> Unit,
) {

    val letterL = animatedMovementForL(distance = -600f, delay = 200)
    val letterA = animatedMovementForL(distance = -1300f, delay = 200)
    val letterW = animatedMovementForL(distance = 1300f, delay = 200)
    val letterY = animatedMovementForL(distance = 600f, delay = 200)

    LaunchedEffect(Unit) {
        delay(1100)
        onNavigate()
    }

    Box (
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Row (
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                text = "L",
                fontFamily = MyFont.soraSemiBold,
                fontSize = 35.sp,
                color = Color.Black,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = letterL
                    }
            )

            Text(
                text = "a",
                fontFamily = MyFont.soraSemiBold,
                fontSize = 35.sp,
                color = Color.Black,
                modifier = Modifier
                    .graphicsLayer { 
                        translationY = letterA
                    }
            )

            Text(
                text = "w",
                fontFamily = MyFont.soraSemiBold,
                fontSize = 35.sp,
                color = Color.Black,
                modifier = Modifier
                    .graphicsLayer {
                        translationY = letterW
                    }
            )

            Text(
                text = "y",
                fontFamily = MyFont.soraSemiBold,
                fontSize = 35.sp,
                color = Color.Black,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = letterY
                    }
            )
        }
    }
}


@Composable fun animatedMovementForL (distance: Float, delay: Long) : Float
{
    var init by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delay)
        init = !init
    }

    return animateFloatAsState(
        targetValue = if (init) 0f else distance,
        animationSpec = tween(600),
        label = "").value
}