package dev.metinkale.openprayertimes.server.db.provider

import dev.metinkale.openprayertimes.City
import dev.metinkale.openprayertimes.server.feature.SearchableProvider
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

interface SearchFromTable : SearchableProvider, FieldSet {
    val latitude: Column<Float?>
    val longitude: Column<Float?>

    suspend fun mapToCity(row: ResultRow): City

    val searchColumn: Expression<String>
    val latLngFilter: Op<Boolean>
        get() = Op.TRUE

    val searchPriorityOrder: Pair<Expression<*>, SortOrder>? get() = null

    override suspend fun search(query: String, nearbyLat: Float?, nearbyLng: Float?): City? =
        suspendTransaction {
            selectAll()
                .where {
                    query.trim()
                        .split(Regex("\\s+"))
                        .filter { it.isNotBlank() }.fold(Op.TRUE as Op<Boolean>) { prev, word ->
                            val pattern = "%$word%"
                            val matchExpr: Op<Boolean> = unaccent(searchColumn).ilike(unaccent(stringParam(pattern)))
                            prev.and(matchExpr)
                        }
                }.run {
                    orderBy(
                        *listOfNotNull(
                            searchPriorityOrder?.let {
                                it.first to it.second
                            },
                            nearbyLat?.let { lat ->
                                nearbyLng?.let { lng ->
                                    Distance(this@SearchFromTable, lat, lng) to SortOrder.ASC
                                }
                            }
                        ).toTypedArray()
                    )
                }
                .limit(1)
                .singleOrNull()
                ?.let { mapToCity(it) }

        }


    override suspend fun findNearestCity(
        userLat: Float,
        userLng: Float
    ): City? {
        return suspendTransaction {
            selectAll()
                .where { latitude.isNotNull() and longitude.isNotNull() and latLngFilter }
                .orderBy(Distance(this@SearchFromTable, userLat, userLng), SortOrder.ASC)
                .limit(1)
                .singleOrNull()
                ?.let { mapToCity(it) }
        }
    }


}

private class Distance(val table: SearchFromTable, val lat: Float, val lng: Float) :
    CustomFunction<Float>("ST_Distance", FloatColumnType(), table.latitude) {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder.append("ST_SetSRID(ST_MakePoint(${table.longitude.name}, ${table.latitude.name}), 4326) <-> ST_SetSRID(ST_MakePoint($lng, $lat), 4326)")
    }
}

private class Unaccent(val expr: Expression<*>) : Expression<String>() {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder.append("unaccent(")
        expr.toQueryBuilder(queryBuilder)
        queryBuilder.append(")")
    }
}

private fun unaccent(expr: Expression<*>) = Unaccent(expr)

private class ILike(val expr: Expression<*>, val pattern: Expression<*>) : Op<Boolean>() {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        expr.toQueryBuilder(queryBuilder)
        queryBuilder.append(" ILIKE ")
        pattern.toQueryBuilder(queryBuilder)
    }
}

private fun Expression<*>.ilike(pattern: Expression<*>) = ILike(this, pattern)
