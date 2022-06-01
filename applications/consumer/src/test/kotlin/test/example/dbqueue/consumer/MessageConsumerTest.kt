package test.example.dbqueue.consumer

import com.example.dbqueue.consumer.MessageConsumer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*
import kotlin.time.Duration.Companion.seconds

@Suppress("SqlWithoutWhere")
class MessageConsumerTest {
    private val db by lazy {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/messages_test?user=messages&amp;password=messages"
        )
    }

    private val consumer = MessageConsumer(db)

    @BeforeTest
    fun setUp() {
        transaction(db) {
            exec("delete from messages")
            exec("insert into messages (body) values ('hello there')")
        }
    }

    @Test
    fun testWithMessage() = runTest {
        val result = consumer.withMessage { "I've read ${it.body}" }

        assertEquals("I've read hello there", result)
    }

    @Test
    fun testWithMessageSetsSentAt() = runTest {
        consumer.withMessage { }

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
    fun testWithMessage_skipsLocked() = runTest {
        launch { consumer.withMessage { delay(2.seconds) } }
        delay(1.seconds)

        val result = consumer.withMessage { it }
        assertNull(result)
    }

    @Test
    fun testWithMessage_exception() = runTest {
        val badResult: Any? = consumer.withMessage<Any?> { throw RuntimeException("bad news") }
        assertNull(badResult)

        val result = consumer.withMessage { "I've read ${it.body}" }
        assertEquals("I've read hello there", result)
    }
}
