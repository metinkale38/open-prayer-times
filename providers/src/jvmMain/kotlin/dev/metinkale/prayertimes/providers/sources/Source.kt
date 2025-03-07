package dev.metinkale.prayertimes.providers.sources

import dev.metinkale.prayertimes.providers.sources.features.DayTimesFeature
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable(with = SourceSerializer::class)
interface Source : DayTimesFeature {
    val name: String
    val fullName: String
        get() = name

    fun ordinal() = values().indexOf(this)

    companion object {
        fun values(): List<Source> = listOf(Diyanet, IGMG, London, NVC, Semerkand, Calc, CSV)
        fun valueOf(value: String) = values().firstOrNull { it.name.equals(value) || it.fullName.equals(value) }

        val Diyanet: Source = dev.metinkale.prayertimes.providers.sources.Diyanet
        val IGMG: Source = dev.metinkale.prayertimes.providers.sources.IGMG
        val London: Source = dev.metinkale.prayertimes.providers.sources.London
        val NVC: Source = dev.metinkale.prayertimes.providers.sources.NVC
        val Semerkand: Source = dev.metinkale.prayertimes.providers.sources.Semerkand
        val Calc: Source = dev.metinkale.prayertimes.providers.sources.Calc
        val CSV: Source = dev.metinkale.prayertimes.providers.sources.CSV
    }


}

object SourceSerializer : KSerializer<Source> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Source", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Source) = encoder.encodeString(value.name)
    override fun deserialize(decoder: Decoder): Source = Source.valueOf(decoder.decodeString())!!
}
