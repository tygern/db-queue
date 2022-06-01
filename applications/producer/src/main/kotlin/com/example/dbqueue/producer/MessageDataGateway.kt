package com.example.dbqueue.producer

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.VarCharColumnType
import org.jetbrains.exposed.sql.transactions.transaction

class MessageDataGateway(private val db: Database) {
    fun save(body: String) = transaction(db) {
        exec("insert into messages (body) values (?)", listOf(Pair(VarCharColumnType(), body)))
    }
}
