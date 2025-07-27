package dev.metinkale.praytimes

import kotlin.math.*


/**
 * Degree-Based Math Functions *
 */
internal object DMath {
    fun dtr(d: Double): Double {
        return d * PI / 180.0
    }

    fun rtd(r: Double): Double {
        return r * 180.0 / PI
    }

    fun sin(d: Double): Double {
        return kotlin.math.sin(dtr(d))
    }

    fun cos(d: Double): Double {
        return kotlin.math.cos(dtr(d))
    }

    fun tan(d: Double): Double {
        return kotlin.math.tan(dtr(d))
    }

    fun arcsin(d: Double): Double {
        return rtd(asin(d))
    }

    fun arccos(d: Double): Double {
        return rtd(acos(d))
    }

    fun arctan(d: Double): Double {
        return rtd(atan(d))
    }

    fun arccot(x: Double): Double {
        return rtd(atan(1 / x))
    }

    fun arctan2(y: Double, x: Double): Double {
        return rtd(atan2(y, x))
    }

    fun fixAngle(a: Double): Double {
        return fix(a, 360.0)
    }

    fun fixHour(a: Double): Double {
        return fix(a, 24.0)
    }

    fun fix(a: Double, b: Double): Double {
        val a2 = a - b * floor(a / b)
        return if (a2 < 0) a2 + b else a2
    }
}