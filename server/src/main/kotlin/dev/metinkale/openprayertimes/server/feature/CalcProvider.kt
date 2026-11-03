package dev.metinkale.openprayertimes.server.feature

import dev.metinkale.calctimes.CalcTimes
import dev.metinkale.calctimes.Method
import dev.metinkale.openprayertimes.City
import dev.metinkale.openprayertimes.server.db.table.GeoNamesTable
import dev.metinkale.openprayertimes.CalcTimesSerializer
import kotlinx.datetime.TimeZone

object CalcProvider : SearchableProvider {

    override suspend fun findNearestCity(
        userLat: Float, userLng: Float
    ): City? {
        return GeoNamesTable.findNearestCity(userLat, userLng)?.applyCalcTimes()
    }

    override suspend fun search(
        query: String,
        nearbyLat: Float?,
        nearbyLng: Float?
    ): City? {
        return GeoNamesTable.search(query)?.applyCalcTimes()
    }


    private fun City.applyCalcTimes(): City? {
        val calcTimes =
            CalcTimes(
                lat!!.toDouble(),
                lng!!.toDouble(),
                elv?.toDouble() ?: 0.0,
                timezone ?: TimeZone.UTC,
                Method.MWL
            )
        return copy(id = CalcTimesSerializer.serializeCalcTimes(calcTimes))
    }
}

