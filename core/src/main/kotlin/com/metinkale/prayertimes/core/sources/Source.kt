package com.metinkale.prayertimes.core.sources

import com.metinkale.prayertimes.core.DayTimes
import com.metinkale.prayertimes.core.utils.now
import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = SourceSerializer::class)
sealed class Source(val name: String) {

    abstract suspend fun getDayTimes(id: String): List<DayTimes>
    open suspend fun getDayTime(id: String, day: LocalDate = LocalDate.now()): DayTimes? =
        getDayTimes(id).firstOrNull { it.date == day }


    companion object {
        fun values(): List<Source> = listOf(Diyanet, IGMG, London, NVC, Semerkand)
        fun valueOf(value: String) = values().firstOrNull { it.name.equals(value) }
    }
}


object SourceSerializer : KSerializer<Source> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Source", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Source) = encoder.encodeString(value.name)
    override fun deserialize(decoder: Decoder): Source = Source.valueOf(decoder.decodeString())!!
}

