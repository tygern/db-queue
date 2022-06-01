package com.example.dbqueue.producer

import com.example.dbqueue.databasesupport.DatabaseConfiguration
import org.slf4j.LoggerFactory

class App

private val logger = LoggerFactory.getLogger(App::class.java)

fun main() {
    val databaseUrl = System.getenv("DATABASE_URL") ?: throw RuntimeException("Please set DATABASE_URL")
    val numberOfMessages = System.getenv("NUMBER_OF_MESSAGES")?.toInt() ?: 1000

    logger.info("Now generating  $numberOfMessages messages")

    val dbConfig = DatabaseConfiguration(databaseUrl)
    val gateway = MessageDataGateway(dbConfig.db)

    repeat(numberOfMessages) {
        val message = generateMessage()
        gateway.save(message)
    }

    logger.info("Done generating $numberOfMessages messages")
}
