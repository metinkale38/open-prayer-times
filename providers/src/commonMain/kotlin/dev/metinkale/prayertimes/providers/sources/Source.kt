package dev.metinkale.prayertimes.providers.sources

import dev.metinkale.prayertimes.providers.DayTimes
import dev.metinkale.prayertimes.providers.utils.now
import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable(with = SourceSerializer::class)
interface Source {
    val name: String
    val fullName: String
        get() = name


    suspend fun getDayTimes(key: String): List<DayTimes>
    suspend fun getDayTime(key: String, day: LocalDate = LocalDate.now()): DayTimes? =
        getDayTimes(key).firstOrNull { it.date == day }

    val ordinal get() = values().indexOf(this)

    val supported: Boolean
        get() = true

    companion object {
        val entries: List<Source> = listOf(Diyanet, IGMG, London, NVC, Semerkand, Calc, CSV).filter(Source::supported)

        @Deprecated(
            message = "Use entries instead",
            level = DeprecationLevel.WARNING
        )
        fun values() = entries
        fun valueOf(value: String) = entries.firstOrNull { it.name == value || it.fullName == value }
    }

}

object SourceSerializer : KSerializer<Source> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Source", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Source) = encoder.encodeString(value.name)
    override fun deserialize(decoder: Decoder): Source = Source.valueOf(decoder.decodeString())!!
}
