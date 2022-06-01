package com.example.dbqueue.consumer

import java.time.Instant

data class Message(val id: Long, val body: String, val createdAt: Instant)
