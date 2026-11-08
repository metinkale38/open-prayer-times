package dev.metinkale.openprayertimes.server.db.table

import dev.metinkale.openprayertimes.City
import dev.metinkale.openprayertimes.Provider
import dev.metinkale.openprayertimes.server.RequestContext
import dev.metinkale.openprayertimes.server.db.provider.ListFromTable
import dev.metinkale.openprayertimes.server.db.provider.SearchFromTable
import org.jetbrains.exposed.v1.core.*

sealed class CitiesTable(table: String, val provider: Provider) : Table(table), SearchFromTable, ListFromTable {
    override val id = varchar("id", 16)
    override val latitude = float("lat").nullable()
    override val longitude = float("lng").nullable()
    override val country = varchar("country", 2)
    val nameTr = array<String>("name_tr")
    val nameEn = array<String>("name_en")

    override val primaryKey = PrimaryKey(id)

    override val searchColumn: Expression<String> = joinNames(nameEn, nameTr)
    override suspend fun getNamesForList(): Column<List<String>> =
        if (RequestContext.current()?.languages?.contains("tr") == true) nameTr else nameEn

    override suspend fun mapToCity(row: ResultRow): City {
        val lat = row[latitude]
        val lng = row[longitude]
        return City(
            id = row[id],
            country = row[country],
            names =
                if (RequestContext.current()?.languages?.contains("tr") == true) row[nameTr] else row[nameEn],
            lat = lat,
            lng = lng,
            elv = null,
            timezone = lat?.let { lat -> lng?.let { lng -> GeoNamesTable.findNearestCity(lat, lng) } }?.timezone,
            provider = provider
        )
    }
}

object IGMGTable : CitiesTable("igmg", Provider.IGMG)
object DiyanetTable : CitiesTable("diyanet", Provider.Diyanet)
object SemerkandTable : CitiesTable("semerkand", Provider.Semerkand)
object NVCTable : CitiesTable("nvc", Provider.NVC)
object JAKIMTable : CitiesTable("jakim", Provider.JAKIM)

private class JoinNamesExpression(
    val first: Column<List<String>>, val second: Column<List<String>>
) : Expression<String>() {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder.append("join_names(")
        first.toQueryBuilder(queryBuilder)
        queryBuilder.append(", ")
        second.toQueryBuilder(queryBuilder)
        queryBuilder.append(")")
    }
}

private fun joinNames(first: Column<List<String>>, second: Column<List<String>>) = JoinNamesExpression(first, second)