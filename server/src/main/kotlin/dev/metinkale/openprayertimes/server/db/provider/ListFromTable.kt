package dev.metinkale.openprayertimes.server.db.provider

import dev.metinkale.openprayertimes.City
import dev.metinkale.openprayertimes.server.feature.ListableProvider
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.core.get
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

interface ListFromTable : ListableProvider, FieldSet {

    suspend fun mapToCity(row: ResultRow): City
    val country: Column<String>
    val id: Column<String>
    suspend fun getNamesForList(): Column<List<String>>


    override suspend fun getChildren(path: List<String>): List<String> = suspendTransaction {
        if (path.isEmpty()) {
            (this@ListFromTable as ColumnSet).select(country).withDistinct(true).map { it[country] }
        } else if (path.size == 1) {
            val names = getNamesForList()
            (this@ListFromTable as ColumnSet).select(names[1]).withDistinct(true)
                .where { country.lowerCase() eq path[0].lowercase() }
                .mapNotNull { it[names[1]] }
        } else {
            val names = getNamesForList()
            (this@ListFromTable as ColumnSet).select(names[path.size]).withDistinct(true).where {
                path.drop(1).foldIndexed(country.lowerCase() eq path[0].lowercase()) { idx, prev, path ->
                    prev and (names[idx + 1] eq path)
                }
            }.mapNotNull { it[names[path.size]] }
        }
    }

    override suspend fun getCity(path: List<String>): City? = suspendTransaction {
        val names = getNamesForList()
        selectAll().where {
            path.drop(1).foldIndexed(country.lowerCase() eq path[0].lowercase()) { idx, prev, path ->
                prev and (names[idx + 1] eq path)
            }
        }.singleOrNull()?.let { mapToCity(it) }
    }


    override suspend fun getCityById(id: String): City? = suspendTransaction {
        selectAll().where { this@ListFromTable.id eq id }.singleOrNull()
            ?.let { return@suspendTransaction mapToCity(it) }
    }
}