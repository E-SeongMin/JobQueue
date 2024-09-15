package com.job.queue.manager

import kotlinx.coroutines.CompletableDeferred

data class JobEventTask<T> (val eventTaskType: T, val sameSkip: Boolean = false, val eventAction: suspend() -> Unit)