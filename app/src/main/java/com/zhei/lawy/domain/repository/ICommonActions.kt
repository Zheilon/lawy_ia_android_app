package com.zhei.lawy.domain.repository

import com.google.firebase.Timestamp

interface ICommonActions {

    fun getHoursFromTimestamp(timestamp: Timestamp) : String

}