package com.example.dbqueue.producer

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.VarCharColumnType
import org.jetbrains.exposed.sql.transactions.transaction

class MessagePublisher(private val db: Database) {
    fun send(body: String) = transaction(db) {
        exec("insert into messages (body) values (?)", listOf(Pair(VarCharColumnType(), body)))
    }
}
