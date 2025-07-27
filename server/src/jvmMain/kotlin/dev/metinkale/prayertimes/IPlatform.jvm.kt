package dev.metinkale.prayertimes

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

internal actual val Platform: IPlatform = object : IPlatform {
    override fun createSqlDelightDriver(schema: SqlSchema<QueryResult.Value<Unit>>, url: String): SqlDriver {
        return JdbcSqliteDriver("jdbc:sqlite:times.db")
    }
}