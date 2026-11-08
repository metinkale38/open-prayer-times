package dev.metinkale.openprayertimes.hijri

enum class HijriCalculation {
    Diyanet,
    UmmulQura
}

data class HijriConfiguration(
    val calculation: HijriCalculation = HijriCalculation.Diyanet,
    val dayOffset: Int = 0
)

object HijriConfig {
    @Volatile
    var current: HijriConfiguration = HijriConfiguration()
}
