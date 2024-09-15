package com.job.queue.manager

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.Exception
import java.util.LinkedList
import java.util.Queue
import java.util.concurrent.CountDownLatch

class JobManager<T> {
    private val eventTypeQueue: Queue<JobEventTask<T>> = LinkedList()
    private val mutex = Mutex()
    private var currentJob: Job? = null
    private var jobThread: Thread = Thread {
        try {
            while (true) {
                val event = eventTypeQueue.poll()
                event?.let { jobEventTask ->
                    val countLatch = CountDownLatch(1)
                    currentJob = CoroutineScope(Dispatchers.Default).launch {
                        jobEventTask.eventAction.invoke()
                        countLatch.countDown()
                    }
                    countLatch.await()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            currentJob?.cancel()
        }
    }

    init {
        jobThread.start()
    }

    fun addEvent(workerEvent: JobEventTask<T>) {
        try {
            CoroutineScope(Dispatchers.Default).launch {
                mutex.withLock {
                    try {
                        Log.d("event", "${workerEvent.eventTaskType}")
                        eventTypeQueue.offer(workerEvent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        jobThread.interrupt()
    }

    fun reset() {
        eventTypeQueue.clear()
        currentJob?.cancel()
    }
}