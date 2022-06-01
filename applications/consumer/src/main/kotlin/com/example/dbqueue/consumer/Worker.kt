package com.example.dbqueue.consumer

import org.slf4j.LoggerFactory

class Worker(
    private val id: Int,
    private val gateway: MessageDataGateway,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(Worker::class.java)
    }

    suspend fun listen() {
        while (true) {
            gateway.withMessage { message ->
                logger.info("Worker $id processed ${message.body}")
            }
        }
    }
}
