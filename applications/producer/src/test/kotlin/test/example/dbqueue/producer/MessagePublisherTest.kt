package test.example.dbqueue.producer

import com.example.dbqueue.producer.MessagePublisher
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import kotlin.test.*

@Suppress("SqlWithoutWhere")
class MessagePublisherTest {
    private val db by lazy {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/messages_test?user=messages&amp;password=messages"
        )
    }

    private val publisher = MessagePublisher(db)

    @BeforeTest
    fun setUp() {
        transaction(db) {
            exec("delete from messages")
        }
    }

    @Test
    fun testSend() {
        publisher.send("hello there")

        val result = transaction(db) {
            exec("select id, body, created_at, sent_at from messages") { rs ->
                if (rs.next()) {
                    Message(
                        id = rs.getLong("id"),
                        body = rs.getString("body"),
                        sentAt = rs.getTimestamp("sent_at")?.toInstant(),
                    )
                } else {
                    fail("Record not found")
                }
            }
        }

        require(result != null)
        assertTrue(result.id > 0)
        assertEquals("hello there", result.body)
        assertNull(result.sentAt)
    }
}

private data class Message(
    val id: Long,
    val body: String,
    val sentAt: Instant?
)
