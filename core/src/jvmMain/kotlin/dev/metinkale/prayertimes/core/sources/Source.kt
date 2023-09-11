package dev.metinkale.prayertimes.core.sources

import dev.metinkale.prayertimes.core.Entry
import dev.metinkale.prayertimes.core.geo.Geocoder
import dev.metinkale.prayertimes.core.geo.Geolocation
import dev.metinkale.prayertimes.core.router.parallelMap
import dev.metinkale.prayertimes.core.sources.features.ByLocationFeature
import dev.metinkale.prayertimes.core.sources.features.CityListFeature
import dev.metinkale.prayertimes.core.sources.features.DayTimesFeature
import dev.metinkale.prayertimes.core.sources.features.SearchFeature
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

        val Diyanet: Source = dev.metinkale.prayertimes.core.sources.Diyanet
        val IGMG: Source = dev.metinkale.prayertimes.core.sources.IGMG
        val London: Source = dev.metinkale.prayertimes.core.sources.London
        val NVC: Source = dev.metinkale.prayertimes.core.sources.NVC
        val Semerkand: Source = dev.metinkale.prayertimes.core.sources.Semerkand
        val Calc: Source = dev.metinkale.prayertimes.core.sources.Calc
        val CSV: Source = dev.metinkale.prayertimes.core.sources.CSV
    }


}

object SourceSerializer : KSerializer<Source> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Source", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Source) = encoder.encodeString(value.name)
    override fun deserialize(decoder: Decoder): Source = Source.valueOf(decoder.decodeString())!!
}
