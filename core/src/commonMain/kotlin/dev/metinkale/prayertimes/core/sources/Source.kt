package dev.metinkale.prayertimes.core.sources

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

    companion object {
        fun values(): List<Source> = listOf(Diyanet, IGMG, London, NVC, Semerkand, Calc)
        fun valueOf(value: String) = values().firstOrNull { it.name.equals(value) }
    }
}


object SourceSerializer : KSerializer<Source> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Source", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Source) = encoder.encodeString(value.name)
    override fun deserialize(decoder: Decoder): Source = Source.valueOf(decoder.decodeString())!!
}
