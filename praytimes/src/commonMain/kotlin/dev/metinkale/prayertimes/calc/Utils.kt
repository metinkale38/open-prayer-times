package dev.metinkale.prayertimes.calc

import kotlin.math.roundToInt

internal object Utils {
    /**
     * convert double time to HH:MM
     *
     * @param time time in double
     * @return HH:MM
     */
    fun toString(time: Double): String {
        val t = (time * 60).roundToInt()
        return az(t / 60) + ":" + az(t % 60)
    }

    /**
     * return a two digit String of number
     *
     * @param i number
     * @return two digit number
     */
    private fun az(i: Int): String {
        return if (i >= 10) "" + i else "0$i"
    }

    /**
     * compute the difference between two times
     *
     * @param time1 Time 1
     * @param time2 Time 2
     * @return timediff
     */
    fun timeDiff(time1: Double, time2: Double): Double {
        return fixHour(time2 - time1)
    }
}
