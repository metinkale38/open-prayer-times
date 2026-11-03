package dev.metinkale.openprayertimes.server.db.table

import dev.metinkale.openprayertimes.DayTimes
import dev.metinkale.openprayertimes.Provider
import kotlinx.datetime.*
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.between
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.javatime.date
import org.jetbrains.exposed.v1.javatime.time
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

object TimesTable : Table("times") {
    val provider = text("provider")
    val key = text("key")
    val date = date("date")
    val fajr = time("fajr")
    val sun = time("sun")
    val dhuhr = time("dhuhr")
    val asr = time("asr")
    val maghrib = time("maghrib")
    val ishaa = time("ishaa")
    val asrHanafi = time("asr_hanafi").nullable()
    val sabah = time("sabah").nullable()
    val fajrEstimated = bool("fajr_estimated")
    val sabahEstimated = bool("sabah_estimated")
    val ishaaEstimated = bool("ishaa_estimated")

    override val primaryKey = PrimaryKey(provider, key, date, name = "PK_Times_ProviderKeyDate")

    suspend fun get(provider: Provider, key: String, from: LocalDate, to: LocalDate) = suspendTransaction {
        TimesTable.selectAll().where {
            (TimesTable.provider eq provider.name) and (TimesTable.key eq key) and
                    (date.between(from.toJavaLocalDate(), to.toJavaLocalDate()))
        }.map {
            DayTimes(
                date = it[date].toKotlinLocalDate(),
                fajr = it[fajr].toKotlinLocalTime(),
                sun = it[sun].toKotlinLocalTime(),
                dhuhr = it[dhuhr].toKotlinLocalTime(),
                asr = it[asr].toKotlinLocalTime(),
                maghrib = it[maghrib].toKotlinLocalTime(),
                ishaa = it[ishaa].toKotlinLocalTime(),
                asrHanafi = it[asrHanafi]?.toKotlinLocalTime(),
                sabah = it[sabah]?.toKotlinLocalTime(),
                isFajrEstimated = it[fajrEstimated],
                isSabahEstimated = it[sabahEstimated],
                isIshaaEstimated = it[ishaaEstimated]
            )
        }
    }

    suspend fun save(provider: Provider, key: String, times: List<DayTimes>) = suspendTransaction {
        TimesTable.batchUpsert(times) {
            this[TimesTable.provider] = provider.name
            this[TimesTable.key] = key
            this[date] = it.date.toJavaLocalDate()
            this[fajr] = it.fajr.toJavaLocalTime()
            this[sun] = it.sun.toJavaLocalTime()
            this[dhuhr] = it.dhuhr.toJavaLocalTime()
            this[asr] = it.asr.toJavaLocalTime()
            this[maghrib] = it.maghrib.toJavaLocalTime()
            this[ishaa] = it.ishaa.toJavaLocalTime()
            this[asrHanafi] = it.asrHanafi?.toJavaLocalTime()
            this[sabah] = it.sabah?.toJavaLocalTime()
            this[fajrEstimated] = it.isFajrEstimated
            this[sabahEstimated] = it.isSabahEstimated
            this[ishaaEstimated] = it.isIshaaEstimated
        }
    }
}