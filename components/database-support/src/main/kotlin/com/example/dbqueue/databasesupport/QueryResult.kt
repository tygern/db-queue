package com.example.dbqueue.databasesupport

sealed class QueryResult<T>

data class Found<T>(val record: T) : QueryResult<T>()
class NotFound<T> : QueryResult<T>()
