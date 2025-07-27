package dev.metinkale.prayertimes

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

internal interface IPlatform {
    fun createSqlDelightDriver(schema: SqlSchema<QueryResult.Value<Unit>>, url: String): SqlDriver
}

internal expect val Platform: IPlatform