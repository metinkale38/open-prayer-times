package dev.metinkale.openprayertimes.sources

import dev.metinkale.openprayertimes.DayTimes
import dev.metinkale.openprayertimes.utils.now
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

    val ordinal get() = entries.indexOf(this)

    val supported: Boolean
        get() = true

    companion object {
        val Diyanet by lazy { DiyanetImpl() }
        val IGMG by lazy { IGMGImpl() }
        val London by lazy { LondonImpl() }
        val NVC by lazy { NVCImpl() }
        val Semerkand by lazy { SemerkandImpl() }
        val Calc by lazy { CalcImpl() }
        val CSV by lazy { CSVImpl() }

        val entries: List<Source> by lazy {
            listOf(
                Diyanet,
                IGMG,
                London,
                NVC,
                Semerkand,
                Calc,
                CSV
            )
        }


        @Deprecated(message = "Use entries instead", level = DeprecationLevel.WARNING)
        fun values() = entries
        fun valueOf(value: String) = entries.firstOrNull { it.name == value || it.fullName == value }
    }

}

object SourceSerializer : KSerializer<Source> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Source", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Source) = encoder.encodeString(value.name)
    override fun deserialize(decoder: Decoder): Source = Source.valueOf(decoder.decodeString())!!
}
