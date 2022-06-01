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
    val publisher = MessagePublisher(dbConfig.db)

    repeat(numberOfMessages) {
        publisher.send(generateMessage())
    }

    logger.info("Done generating $numberOfMessages messages")
}
