package dev.metinkale.openprayertimes.server.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.v1.jdbc.Database

object DatabaseFactory {
    fun init(poolSize: Int? = null) {
        val config = HikariConfig().apply {
            jdbcUrl = System.getenv("JDBC_URL")
            username = System.getenv("JDBC_USERNAME")
            password = System.getenv("JDBC_PASSWORD")
            maximumPoolSize = poolSize ?: System.getenv("DB_POOL_SIZE")?.toInt() ?: 5
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"

            validate()
        }
        Database.connect(HikariDataSource(config))

    }
}