package com.zhei.lawy.view.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SplashScreenViewModel : ViewModel() {

    var alreadyExecuted by mutableStateOf(false)
        private set

    fun updateIsActivate () { alreadyExecuted = !alreadyExecuted }

}