package com.example.dbqueue.consumer

import com.example.dbqueue.databasesupport.DatabaseConfiguration
import org.slf4j.LoggerFactory

class App

private val logger = LoggerFactory.getLogger(App::class.java)

fun main() {
    logger.info("Starting consumer")
    val databaseUrl = System.getenv("DATABASE_URL")
        ?: throw RuntimeException("Please set the DATABASE_URL environment variable")

    val dbConfig = DatabaseConfiguration(databaseUrl)

    val gateway = MessageDataGateway(dbConfig.db)


    while (true) {
        gateway.withMessage { message ->
            logger.info("Processing ${message.body}")
        }
    }
}
