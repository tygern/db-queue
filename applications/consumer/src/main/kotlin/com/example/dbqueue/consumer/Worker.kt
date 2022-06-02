package com.example.dbqueue.consumer

import org.slf4j.LoggerFactory

class Worker(
    private val id: Int,
    private val consumer: MessageConsumer,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(Worker::class.java)
    }

    suspend fun listen() {
        while (true) {
            consumer.withMessage { message ->
                logger.debug("Worker $id processed ${message.body}")
            }
        }
    }
}
