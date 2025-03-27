package com.zhei.lawy.view.ui.mainscreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zhei.lawy.MyFont
import com.zhei.lawy.view.viewmodel.MainScreenViewModel


@Composable fun HistoryScreen (
    viewMainS: MainScreenViewModel,
    distanceDevice: Float
) {
    val onPressHistory by viewMainS::onHistoryButton
    val heightDp = with(LocalDensity.current) { distanceDevice.toDp() }

    /* <---------------------------------- Testing Items --------------------------------------> */
    val heightPx = with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    val dp = with(LocalDensity.current) { heightPx.toDp() }
    /* <---------------------------------------------------------------------------------------> */

    Column (
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                translationY = if (onPressHistory) -0f else distanceDevice
            },
              /*if (onPressHistory) -0f else distanceDevice*/
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box (
            modifier = Modifier
                .height(heightDp)
                .fillMaxWidth()
                .background(Color.Black)
        ) {

            Column (
                modifier = Modifier.fillMaxSize()
            ) {

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    Text(
                        text = "Historial",
                        fontFamily = MyFont.soraRegular,
                        fontSize = 22.sp,
                        color = Color.White,
                        modifier = Modifier.padding(start = 20.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    ImageToReturnWriteArea(viewMainS = viewMainS)
                }
            }
        }
    }
}


@Composable fun ImageToReturnWriteArea (
    viewMainS: MainScreenViewModel
) {
    Column (
        modifier = Modifier
            .fillMaxHeight()
            .padding(end = 20.dp),
        verticalArrangement = Arrangement.Center, 
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            imageVector = Icons.Default.KeyboardDoubleArrowUp,
            contentDescription = "arrow",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(35.dp)
                .pointerInput(Unit) {
                    detectTapGestures {
                        viewMainS.updateOnHistoryPress()
                    }
                }
        )
    }
}