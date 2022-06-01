package com.example.dbqueue.consumer

import com.example.dbqueue.databasesupport.Found
import com.example.dbqueue.databasesupport.NotFound
import com.example.dbqueue.databasesupport.QueryResult
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.LongColumnType
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class MessageConsumer(private val db: Database) {
    suspend fun <T> withMessage(block: suspend (Message) -> T): T? = newSuspendedTransaction(db = db) {
        when (val queryResult = findMessage()) {
            is NotFound -> null
            is Found -> {
                val result = try {
                    block(queryResult.record)
                } catch (e: Exception) {
                    return@newSuspendedTransaction null
                }

                result.also {
                    exec(
                        "update messages set sent_at = current_timestamp where id = ?",
                        listOf(Pair(LongColumnType(), queryResult.record.id))
                    )
                }
            }
        }
    }

    private fun Transaction.findMessage(): QueryResult<Message> =
        exec("select id, body, created_at from messages where sent_at is null for update skip locked") { rs ->
            if (rs.next()) {
                Found(
                    Message(
                        id = rs.getLong("id"),
                        body = rs.getString("body"),
                        createdAt = rs.getTimestamp("created_at").toInstant(),
                    )
                )
            } else {
                NotFound()
            }
        } ?: NotFound()
}
