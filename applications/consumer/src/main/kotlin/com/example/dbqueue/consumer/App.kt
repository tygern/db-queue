package com.example.dbqueue.consumer

import com.example.dbqueue.databasesupport.DatabaseConfiguration
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration.Companion.seconds

class App

private val logger = LoggerFactory.getLogger(App::class.java)

fun main(): Unit = runBlocking {
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

    fixedRateTimer(name = "counter", period = 1.seconds.inWholeMilliseconds) {
        val count = consumer.countUnsent()
        logger.info("There are $count messages in the queue")
    }
}
