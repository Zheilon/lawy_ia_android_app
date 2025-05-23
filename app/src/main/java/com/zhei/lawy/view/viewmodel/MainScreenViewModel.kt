package com.zhei.lawy.view.viewmodel
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.zhei.lawy.EntityExecuted
import com.zhei.lawy.data.model.ChattingEntity
import com.zhei.lawy.data.repository.AnswerAIRepository
import com.zhei.lawy.data.repository.CommonActionsRepository
import com.zhei.lawy.data.repository.QuestionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainScreenViewModel : ViewModel() {

    private val repositoryCommon = CommonActionsRepository()
    private val repositoryQuestion = QuestionRepository()
    private val repositoryAnswer = AnswerAIRepository()

    var onTextFill by mutableStateOf(false)
        private set

    var appIsOn by mutableStateOf(false)
        private set

    var onHistoryButton by mutableStateOf(false)
        private set

    private val _textfield = mutableStateOf("")
    val textfield: State<String> = _textfield

    private val _chattingList = MutableStateFlow<List<ChattingEntity>>(emptyList())
    val chattingList: StateFlow<List<ChattingEntity>> = _chattingList.asStateFlow()


    fun updateOnTextFill () { onTextFill = textfield.value.isNotEmpty() }

    fun updateOnHistoryPress () { onHistoryButton = !onHistoryButton }

    fun updateAppIsOn () { appIsOn = !appIsOn }

    fun updateTextField (value: String) { _textfield.value = value}


    fun addPernsonQuestion()
    {
        if (onTextFill) {

            val chatting = ChattingEntity(
                generatedInfo = _textfield.value,
                timestamp = Timestamp.now(),
                entityExecuted = EntityExecuted.PERSON,
                hoursRepresent = repositoryCommon.getHoursFromTimestamp(Timestamp.now())
            )
            _chattingList.update { it + chatting }
        }
    }


    fun sendQuestion()
    {
        viewModelScope.launch {
            repositoryQuestion.updateQuestion(map = mapOf("question" to _textfield.value))
            delay(1000)
            repositoryQuestion.updateQuestion(map = mapOf("activate" to true))
        }
    }


    fun getAnswerAI()
    {
        viewModelScope.launch {
           repositoryAnswer.isCurrent().collect { bool ->
               bool?.let { Log.e("Trusted", it.toString()) }
               if (bool == true) {
                   repositoryAnswer.getAnswerWithFlow().distinctUntilChanged().collect { response ->
                       val chatting = ChattingEntity(
                           generatedInfo = response.toString(),
                           timestamp = Timestamp.now(),
                           entityExecuted = EntityExecuted.AI,
                           hoursRepresent = repositoryCommon.getHoursFromTimestamp(Timestamp.now())
                       )
                       _chattingList.update { it + chatting }
                   }
               }
           }
        }
    }


    fun clearTextField () { _textfield.value = "" }
}