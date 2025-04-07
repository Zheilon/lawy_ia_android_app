package com.zhei.lawy.view.ui.mainscreen
import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import android.view.View
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Timestamp
import com.zhei.lawy.EntityExecuted
import com.zhei.lawy.MyFont
import com.zhei.lawy.R
import com.zhei.lawy.data.model.ChattingEntity
import com.zhei.lawy.ui.theme.BLACK_ONE
import com.zhei.lawy.ui.theme.BLACK_THREE
import com.zhei.lawy.ui.theme.BLACK_TWO
import com.zhei.lawy.ui.theme.GREEN_EIGHT
import com.zhei.lawy.ui.theme.RED_FOUR
import com.zhei.lawy.ui.theme.YELLOW_THREE
import com.zhei.lawy.view.viewmodel.MainScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Preview
@Composable fun PreviewMainScreen ()
{
    MainScreen(
        navHost = rememberNavController(),
        viewMainS =  remember { MainScreenViewModel() }
    )
}


@SuppressLint("ContextCastToActivity")
@Composable fun MainScreen (
    navHost: NavHostController = rememberNavController(),
    viewMainS: MainScreenViewModel = viewModel()
) {

    /*Status bar detais in color black*/
    val activity = LocalContext.current as? Activity
    LaunchedEffect(key1 = activity) {
        activity?.window?.let { window ->
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.isAppearanceLightStatusBars = true
        }
    }

    LaunchedEffect(key1 = viewMainS.textfield.value) {
        viewMainS.updateOnTextFill()
    }
    
    LaunchedEffect(Unit) { delay(1500); viewMainS.updateAppIsOn() }

    viewMainS.getAnswerAI()

    Log.e("LISTA ORIGIN", viewMainS.chattingList.collectAsState().value.toString())

    /*Main Box*/

    val heightPx = with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }

    val animateTranstion = animateTranslationToHistory(viewMainS = viewMainS, distance = -heightPx)

    Log.e("PX", heightPx.toString())


    /* <----------------------- Screens ----------------------> */


    Box (
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
            .background(Color.Black)
            .graphicsLayer {
                translationY = animateTranstion
            }
    ) {

        /*BackgroundImageMainScreen()*/

        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top, 
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeaderMainScreenArea(viewMainS = viewMainS)

            Spacer(modifier = Modifier.height(5.dp))

            ChattingSectionArea(viewMainS = viewMainS)
        }

        BottomMainScreenArea(viewMainS = viewMainS)
    }

    HistoryScreen(viewMainS = viewMainS, distanceDevice = heightPx / 2)
}


@Composable fun BackgroundImageMainScreen ()
{
    Image(
        painter = painterResource(id = R.drawable.imagelawy2),
        contentDescription ="hola",
        modifier = Modifier
            .alpha(0.1f)
            .fillMaxSize()
            .scale(1.25f)
    )
}


@Composable fun HeaderMainScreenArea (
    viewMainS: MainScreenViewModel
) {
    val heightDevicePx = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }
    val widthDevicePx = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }

    val imageHistorySize = (heightDevicePx * widthDevicePx) / 25000
    val surfaceMainSize = (heightDevicePx * widthDevicePx) / 10000

    val focusKeyBoard = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val animateY = animateTranslationYRocket(viewMainS = viewMainS)
    val scope = rememberCoroutineScope()

    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .height(with(LocalDensity.current) { surfaceMainSize.toDp() }), /*120pd*/
        color = Color.Transparent,
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
    ) {

        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Lawy",
                    fontFamily = MyFont.soraSemiBold,
                    fontSize = 30.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    imageVector = Icons.Default.History,
                    contentDescription = "history",
                    modifier = Modifier
                        .size(35.dp)/*35*/
                        .pointerInput(Unit) {
                            detectTapGestures {
                                scope.launch {
                                    focusKeyBoard.clearFocus()
                                    keyboardController?.hide()
                                    delay(600)
                                    viewMainS.updateOnHistoryPress()
                                }
                            }
                        },
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }
    }
}


@Composable fun animateTranslationToHistory (
    viewMainS: MainScreenViewModel,
    distance: Float
) : Float {
    return animateFloatAsState(
        targetValue = if (viewMainS.onHistoryButton) distance / 2 else 0f,
        animationSpec = tween(500), label = "").value
}


@Composable fun animateTranslationYRocket (
    viewMainS: MainScreenViewModel
) : Float {
    return animateFloatAsState(
        targetValue = if (viewMainS.appIsOn) 1f else 200f,
        animationSpec = tween(1500), label = "").value
}


@Composable fun animateRendeColorIconMainScreen () : Color
{
    val infinity = rememberInfiniteTransition("")
    return infinity.animateColor(
        initialValue = YELLOW_THREE,
        targetValue = RED_FOUR,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    ).value
}


@Composable
fun ChattingSectionArea(
    viewMainS: MainScreenViewModel
) {

    val lazyListState = rememberLazyListState()

    val originList by viewMainS.chattingList.collectAsState()

    /*Lista para testing!!!*/
    val testing = listOf(
        ChattingEntity(
            "Hola lawy!",
            Timestamp.now(),
            EntityExecuted.PERSON,
            "1:08 p.m."
        ),
        ChattingEntity(
            "Hola! como estas?",
            Timestamp.now(),
            EntityExecuted.AI,
            "1:08 p.m."
        ),
        ChattingEntity(
            "Muy bien! tengo una pregunta respecto a un área legal.",
            Timestamp.now(),
            EntityExecuted.PERSON,
            "1:10 p.m."
        ),
        ChattingEntity(
            "Adelante...pregunta! estoy para ayudarte",
            Timestamp.now(), EntityExecuted.AI,
            "1:10 p.m."
        ),
        ChattingEntity(
            "Bueno...Queria preguntarte sobre el código de comercio, ¿Que es?",
            Timestamp.now(),
            EntityExecuted.PERSON,
            "1:11 p.m."
        ),
        ChattingEntity(
            "El Código de Comercio es el documento legal en el que se establecen las normas para garantizar la transparencia y estandarización de prácticas comerciales en Colombia. Fue publicado en 1971 a través del Decreto 410 del 27 de marzo. ¿Tienes otra pregunta respecto al codigo de comercio?",
            Timestamp.now(),
            EntityExecuted.AI,
            "1:12 p.m."
        ),
        ChattingEntity(
            "Perfecto, gracias!",
            Timestamp.now(),
            EntityExecuted.PERSON,
            "1:12 p.m."
        ),
        ChattingEntity(
            "No hay de que...estoy para ayudar! =)",
            Timestamp.now(),
            EntityExecuted.AI,
            "1:12 p.m."
        )
    )

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .animateContentSize()
            .padding(start = 20.dp, end = 20.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        /*reverseLayout = true /*para que los elementos entrantes sea los primeros en verse.*/*/
        state = lazyListState
    ) {

        itemsIndexed(testing, key = {ind, item -> "${item.generatedInfo}_$ind"}
        ) { index, item ->
            CustomsChattingCardsItems(
                index = index,
                item = item,
                list = testing
            )
        }
    }
}


@Composable fun CustomsChattingCardsItems (
    index: Int,
    item: ChattingEntity,
    list: List<ChattingEntity>
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (item.entityExecuted == EntityExecuted.PERSON) Arrangement.End else Arrangement.Start
    ) {

        Column (
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = if (item.entityExecuted == EntityExecuted.PERSON)
                Alignment.End else Alignment.Start
        ) {

            Row (
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ){

                if (item.entityExecuted == EntityExecuted.PERSON) {
                    Image(
                        imageVector = Icons.Default.EmojiPeople,
                        contentDescription = "person",
                        colorFilter = ColorFilter.tint(Color.White),
                    )
                }

                Text(
                    text = item.hoursRepresent,
                    fontFamily = MyFont.soraRegular,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Start,
                    lineHeight = 16.5.sp,
                    color = Color.White
                )

                if (item.entityExecuted == EntityExecuted.AI) {
                    Text(
                        text = "  IA",
                        fontFamily = MyFont.soraSemiBold,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Start,
                        lineHeight = 16.5.sp,
                        color = Color.White
                    )
                }
            }

            BoxInnerMScreen(item = item)
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
}


@Composable fun BoxInnerMScreen (
    item: ChattingEntity
) {
    Box(
        modifier = Modifier
            .padding(
                start = if (item.entityExecuted == EntityExecuted.PERSON) 30.dp else 0.dp,
                end = if (item.entityExecuted == EntityExecuted.PERSON) 0.dp else 30.dp
            )
            .background(
                color = if (item.entityExecuted == EntityExecuted.PERSON) Color.Transparent else Color.White,
                shape = RoundedCornerShape(
                    topStart = if (item.entityExecuted == EntityExecuted.PERSON) 20.dp else 0.dp,
                    topEnd = if (item.entityExecuted == EntityExecuted.PERSON) 0.dp else 20.dp,
                    bottomStart = if (item.entityExecuted == EntityExecuted.PERSON) 0.dp else 20.dp,
                    bottomEnd = if (item.entityExecuted == EntityExecuted.PERSON) 20.dp else 0.dp
                )
            )
    ) {

        Column (
            Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = item.generatedInfo,
                fontFamily = MyFont.soraRegular,
                fontSize = 17.sp,
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Justify,
                lineHeight = 19.sp,
                color = if (item.entityExecuted == EntityExecuted.PERSON) Color.White else Color.Black
            )
        }
    }
}


@Composable
fun rememberImePadding(): Dp {
    val imeBottomPadding = remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val view = LocalView.current

    DisposableEffect(LocalView.current) {
        val listener = View.OnApplyWindowInsetsListener { v, insets ->
            val imeHeight = insets.systemWindowInsetBottom
            imeBottomPadding.intValue = imeHeight
            insets
        }
        view.setOnApplyWindowInsetsListener(listener)
        onDispose { view.setOnApplyWindowInsetsListener(null) }
    }

    return with(density) { imeBottomPadding.intValue.toDp() }
}


@Composable
fun BottomMainScreenArea (
    viewMainS: MainScreenViewModel
) {

    val insets = LocalView.current.let { view ->
        remember {
            ViewCompat.getRootWindowInsets(view)
                ?.getInsets(WindowInsetsCompat.Type.ime())?.bottom ?: 0
        }
    }
    val imePaddingToOlds = with(LocalDensity.current) { insets.toDp() }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        verticalArrangement = Arrangement.Bottom

    ) {

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    Brush.verticalGradient(listOf(Color.Black, BLACK_TWO)),
                    RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
        ) {

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp, end = 18.dp, bottom = 35.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CustomTextFieldForEditing(viewMainS = viewMainS)
            }
        }
    }
}


@Composable fun CustomTextFieldForEditing (
    viewMainS: MainScreenViewModel
) {
    val focusRequest = remember { FocusRequester() }
    val keyBoard = LocalSoftwareKeyboardController.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT


    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        TextField(
            value = viewMainS.textfield.value,
            onValueChange = { viewMainS.updateTextField(it) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                disabledContainerColor = Color.Transparent, 
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequest),
            minLines = 1,
            maxLines = 15,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            textStyle = TextStyle(
                color = Color.White,
                fontFamily = MyFont.soraRegular,
                fontSize = 19.sp,
                textAlign = TextAlign.Justify
            ),
            placeholder = {
                Text(
                    text = "Preguntale a Lawy!",
                    color = Color.White,
                    fontFamily = MyFont.soraRegular,
                    fontSize = 19.sp,
                    modifier = Modifier.height(23.5.dp),
                    overflow = TextOverflow.Ellipsis
                )
            },
            enabled = !viewMainS.onHistoryButton
        )

        ImageToSendQuestion(viewMainS = viewMainS)
    }

    LaunchedEffect(key1 = Unit) {
        delay(500)
        focusRequest.requestFocus()
        keyBoard?.show()
    }
}


@Composable fun ImageToSendQuestion (
    viewMainS: MainScreenViewModel
) {
    val onTextFilled by viewMainS::onTextFill

    val focusKeyBoard = LocalSoftwareKeyboardController.current
    val focusIndicator = LocalFocusManager.current

    val infinity = rememberInfiniteTransition("")

    val animatedScale by infinity.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val animatedRotate by animateFloatAsState(
        targetValue = if (onTextFilled) 0f else 180f,
        animationSpec = tween(1300), label = "")

    val animatedColor by animateColorAsState(
        targetValue = if (onTextFilled) Color.White else BLACK_THREE,
        animationSpec = tween(1300), label = "")

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = Icons.Default.ArrowCircleUp,
            contentDescription = "send",
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures {
                        viewMainS.addPernsonQuestion()
                        viewMainS.sendQuestion()
                        viewMainS.clearTextField()
                        focusKeyBoard?.hide()
                        focusIndicator.clearFocus()
                    }
                }
                .size(40.dp)
                .scale(if (onTextFilled) animatedScale else 1f)
                .graphicsLayer {
                    rotationZ = animatedRotate
                },
            colorFilter = ColorFilter.tint(animatedColor)
        )
    }
}
