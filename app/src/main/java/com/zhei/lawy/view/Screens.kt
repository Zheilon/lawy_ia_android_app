package com.zhei.lawy.view
import kotlinx.serialization.Serializable

@Serializable sealed class Screens {

    @Serializable data object MainScreen : Screens()
}