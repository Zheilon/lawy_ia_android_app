package com.zhei.lawy.view.viewmodel
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.zhei.lawy.EntityExecuted
import com.zhei.lawy.MyApplication
import com.zhei.lawy.data.model.ChattingEntity
import com.zhei.lawy.data.repository.AnswerAIRepository
import com.zhei.lawy.data.repository.CommnonActionsRepository
import com.zhei.lawy.data.repository.QuestionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode

class MainScreenViewModel : ViewModel() {

    private val repositoryCommon = CommnonActionsRepository()
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

    private val _lastedValue = mutableStateOf(Any())
    val lastedValue: State<Any> = _lastedValue


    fun updateOnTextFill () { onTextFill = textfield.value.isNotEmpty() }

    fun updateOnHistoryPress () { onHistoryButton = !onHistoryButton }

    fun updateAppIsOn () { appIsOn = !appIsOn }

    fun updateTextField (value: String) { _textfield.value = value }

    fun addPernsonQuestion()
    {
        if (onTextFill) {

            val chatting = ChattingEntity(
                generatedInfo = _textfield.value,
                timestamp = Timestamp.now(),
                entityExecuted = EntityExecuted.PERSON,
                hoursRepresent = repositoryCommon.getHoursFromTimestamp(Timestamp.now())
            )

            if (_chattingList.value.isEmpty()) {
                _chattingList.update { it + chatting }

            } else {
                val confirm = chatting.generatedInfo !=_chattingList.value.last().generatedInfo
                if (confirm) {
                    _chattingList.update { it + chatting }
                }
            }
        }
    }


    fun sendQuestion()
    {
        viewModelScope.launch {
            repositoryQuestion
                .updateQuestion(map = mapOf("question" to (_textfield.value.toIntOrNull() ?: 0)))

            delay(1000)

            repositoryQuestion
                .updateQuestion(map = mapOf("activate" to true))
        }
    }


    fun getAnswerAI()
    {
        viewModelScope.launch {
           repositoryAnswer.isCurrent().collect { bool ->
               bool?.let { Log.e("Trusted", it.toString()) }
               if (bool == true) {
                   repositoryAnswer.getAnswerWithFlow().collect { number ->
                       if (lastedValue.value != number) {
                           number?.let { Log.e("Number", it.toString()) }
                           val chatting = ChattingEntity(
                               generatedInfo = number.toString().toDouble().
                               toBigDecimal().setScale(3, RoundingMode.HALF_UP).toString(),
                               timestamp = Timestamp.now(),
                               entityExecuted = EntityExecuted.AI,
                               hoursRepresent = repositoryCommon.getHoursFromTimestamp(Timestamp.now())
                           )
                           _chattingList.update { it + chatting }
                           number?.let { _lastedValue.value = it }
                       }
                   }
               }
           }
        }

    }


    fun clearTextField () { _textfield.value = "" }
}