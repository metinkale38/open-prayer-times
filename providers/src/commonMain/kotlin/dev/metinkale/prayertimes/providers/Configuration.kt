package dev.metinkale.prayertimes.providers

object Configuration {
    /**
     * list of preferred languages
     */
    var languages: List<String> = listOf("en")

    /**
     * needed for igmg prayer times
     * no public id - obtained through private contacts
     */
    var IGMG_API_KEY: String = Platform.getEnv("IGMG_API_KEY") ?: ""

    /**
     * needed for london prayer times.
     * Can be obtained on their website
     */
    var LONDON_PRAYER_TIMES_API_KEY: String = Platform.getEnv("LONDON_PRAYER_TIMES_API_KEY") ?: ""


}