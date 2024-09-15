package com.job.queue

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.job.queue.databinding.ActivityMainBinding
import com.job.queue.manager.JobEventMessage
import com.job.queue.manager.JobEventTask
import com.job.queue.manager.JobManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var eventManager = JobManager<JobEventMessage>()

    private var testData = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
    }

    private fun init() {
        binding.apply {
            tvCount.text = testData.toString()

            btnAdd.setOnClickListener {
                eventManager.addEvent(JobEventTask(JobEventMessage.ADD, eventAction = {
                    add(binding.etCount.text.toString().toInt())
                }))
            }

            btnSubtract.setOnClickListener {
                eventManager.addEvent(JobEventTask(JobEventMessage.SUBTRACT, eventAction = {
                    subtract(binding.etCount.text.toString().toInt())
                }))
            }
        }
    }

    private suspend fun add(count: Int) {
        for(i in 0 until count) {
            testData++
            setView()
        }
    }

    private suspend fun subtract(count: Int) {
        for(i in 0 until count) {
            testData--
            setView()
        }
    }

    private suspend fun setView() {
        withContext(Dispatchers.Main) {
            binding.tvCount.text = testData.toString()
        }
    }
}