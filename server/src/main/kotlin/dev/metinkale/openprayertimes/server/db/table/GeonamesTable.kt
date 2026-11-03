package dev.metinkale.openprayertimes.server.db.table

import dev.metinkale.openprayertimes.City
import dev.metinkale.openprayertimes.Provider
import dev.metinkale.openprayertimes.server.db.provider.SearchFromTable
import kotlinx.datetime.TimeZone
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.inList

private val latLngFeatureCodes = listOf("PPLG", "PPLC", "PPLA", "PPLA1", "PPLA2", "PPLA3", "PPLA4", "PPLA5")

object GeoNamesTable : Table("geonames"), SearchFromTable {
    val geonameId = integer("geonameid")
    val name = varchar("name", 200)
    val asciiName = varchar("asciiname", 200)
    val alternateNames = text("alternatenames")
    override val latitude = float("latitude").nullable()
    override val longitude = float("longitude").nullable()
    val featureClass = char("featureclass")
    val featureCode = varchar("featurecode", 10)
    val countryCode = char("countrycode", 2)
    val cc2 = varchar("cc2", 200)
    val admin1Code = varchar("admin1_code", 20)
    val admin2Code = varchar("admin2_code", 80)
    val admin3Code = varchar("admin3_code", 20)
    val admin4Code = varchar("admin4_code", 20)
    val population = long("population")
    val elevation = integer("elevation")
    val dem = integer("dem")
    val timezone = varchar("timezone", 40)

    override val primaryKey = PrimaryKey(geonameId, name = "PK_GeonameID")

    override val latLngFilter: Op<Boolean> = (featureCode inList latLngFeatureCodes)

    override val searchColumn = alternateNames

    override val searchPriorityOrder = population to SortOrder.DESC

    override suspend fun mapToCity(row: ResultRow): City {
        return City(
            id = row[geonameId].toString(),
            country = row[countryCode],
            names = listOf(row[name]),
            lat = row[latitude],
            lng = row[longitude],
            elv = row[elevation].toFloat(),
            timezone = TimeZone.of(row[timezone]),
            provider = Provider.Calc
        )
    }

}