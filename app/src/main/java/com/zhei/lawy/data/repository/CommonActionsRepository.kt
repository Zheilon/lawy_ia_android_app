package com.zhei.lawy.data.repository
import com.google.firebase.Timestamp
import com.zhei.lawy.domain.repository.ICommonActions
import java.text.SimpleDateFormat
import java.util.Locale

class CommonActionsRepository : ICommonActions {


    override fun getHoursFromTimestamp(timestamp: Timestamp): String
    {
        return SimpleDateFormat("h:mm a", Locale.getDefault()).format(timestamp.toDate())
    }


}