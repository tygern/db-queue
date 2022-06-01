package com.example.dbqueue.producer

import com.example.dbqueue.databasesupport.DatabaseConfiguration
import org.slf4j.LoggerFactory

class App

private val logger = LoggerFactory.getLogger(App::class.java)

fun main() {
    val databaseUrl = System.getenv("DATABASE_URL")
        ?: throw RuntimeException("Please set the DATABASE_URL environment variable")

    val dbConfig = DatabaseConfiguration(databaseUrl)
    val gateway = MessageDataGateway(dbConfig.db)

    logger.info("Generating messages")

    repeat(1000) {
        val message = generateMessage()
        gateway.save(message)
    }
}
