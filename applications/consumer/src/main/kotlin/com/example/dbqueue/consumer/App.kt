package com.example.dbqueue.consumer

import com.example.dbqueue.databasesupport.DatabaseConfiguration
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

class App

private val logger = LoggerFactory.getLogger(App::class.java)

fun main() = runBlocking {
    val databaseUrl = System.getenv("DATABASE_URL") ?: throw RuntimeException("Please set DATABASE_URL")
    val numberOfWorkers = System.getenv("NUMBER_OF_WORKERS")?.toInt() ?: 4

    val dbConfig = DatabaseConfiguration(databaseUrl)
    val consumer = MessageConsumer(dbConfig.db)

    logger.info("Starting $numberOfWorkers workers")

    repeat(numberOfWorkers) { workerId ->
        launch {
            Worker(id = workerId, consumer = consumer).listen()
        }
    }
}
