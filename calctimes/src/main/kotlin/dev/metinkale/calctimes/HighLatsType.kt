package dev.metinkale.calctimes

enum class HighLatsType {
    /**
     * High Latitude Adjustment is used as maximum for ishaa calculation.
     * It will use the adjustment even if it is possible to calculate a time for ishaa/fajr
     */
    Max,

    /**
     * High Latitude Adjustment will only be used, if Ishaa can not be calculated with the method
     * Note: Might lead to very short ishaa timing
     */
    Strict
}
