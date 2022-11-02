package dev.metinkale.prayertimes.calc


/**
 * Degree-Based Math Functions *
 */


fun dtr(d: Double): Double {
    return d * Math.PI / 180.0
}

fun rtd(r: Double): Double {
    return r * 180.0 / Math.PI
}

fun sin(d: Double): Double {
    return Math.sin(dtr(d))
}

fun cos(d: Double): Double {
    return Math.cos(dtr(d))
}

fun tan(d: Double): Double {
    return Math.tan(dtr(d))
}

fun arcsin(d: Double): Double {
    return rtd(Math.asin(d))
}

fun arccos(d: Double): Double {
    return rtd(Math.acos(d))
}

fun arctan(d: Double): Double {
    return rtd(Math.atan(d))
}

fun arccot(x: Double): Double {
    return rtd(Math.atan(1 / x))
}

fun arctan2(y: Double, x: Double): Double {
    return rtd(Math.atan2(y, x))
}

fun fixAngle(a: Double): Double {
    return fix(a, 360.0)
}

fun fixHour(a: Double): Double {
    return fix(a, 24.0)
}

fun fix(a: Double, b: Double): Double {
    var a = a
    a = a - b * Math.floor(a / b)
    return if (a < 0) a + b else a
}