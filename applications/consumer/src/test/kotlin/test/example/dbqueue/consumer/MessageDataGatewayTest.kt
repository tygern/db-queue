package test.example.dbqueue.consumer

import com.example.dbqueue.consumer.MessageDataGateway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

@Suppress("SqlWithoutWhere")
class MessageDataGatewayTest {
    private val db by lazy {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/messages_test?user=messages&amp;password=messages"
        )
    }

    private val gateway = MessageDataGateway(db)

    @BeforeTest
    fun setUp() {
        transaction(db) {
            exec("delete from messages")
            exec("insert into messages (body) values ('hello there')")
        }
    }

    @Test
    fun testWithMessage() {
        val result = gateway.withMessage { "I've read ${it.body}" }

        assertEquals("I've read hello there", result)
    }

    @Test
    fun testWithMessageSetsSentAt() {
        gateway.withMessage { }

        val sentAt = transaction(db) {
            exec("select id, body, created_at, sent_at from messages") { rs ->
                if (rs.next()) {
                    rs.getTimestamp("sent_at")
                } else {
                    fail("Record not found")
                }
            }
        }

        assertNotNull(sentAt)
    }

    @Test
    fun testWithMessage_skipsSent() {
        gateway.withMessage { }
        val result = gateway.withMessage { it }

        assertNull(result)
    }
}
