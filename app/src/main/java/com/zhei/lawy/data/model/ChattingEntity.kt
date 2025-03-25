package com.zhei.lawy.data.model
import com.google.firebase.Timestamp
import com.zhei.lawy.EntityExecuted

data class ChattingEntity (

    val generatedInfo: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val entityExecuted: EntityExecuted,
    val hoursRepresent: String = ""

)