package com.zhei.lawy.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


inline fun Job?.cancelAndLaunch(
    listJobs: MutableList<Job>,
    scope: CoroutineScope,
    crossinline block: suspend CoroutineScope.() -> Unit
) {

    this?.cancel()
    scope.launch { block() }.also { listJobs.add(it) }
}