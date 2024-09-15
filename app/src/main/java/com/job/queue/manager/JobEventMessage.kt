package com.job.queue.manager

enum class JobEventMessage(var type: Int) {
    ADD(1),
    SUBTRACT(2),
    UNKNOWN(0);

    companion object {
        fun conv(type: Int) : JobEventMessage{
            for (jobEventMessage in values()) {
                if (jobEventMessage.type == type) {
                    return jobEventMessage
                }
            }
            return UNKNOWN
        }
    }
}