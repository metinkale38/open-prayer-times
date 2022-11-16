package dev.metinkale.prayertimes.core

object Configuration {
    /**
     * needed to search cities by geolocation
     */
    var GOOGLE_API_KEY: String = ""

    /**
     * needed for igmg prayer times
     * no public id - obtained through private contacts
     */
    var IGMG_API_KEY: String = ""

    /**
     * needed for london prayer times.
     * Can be obtained on their website
     */
    var LONDON_PRAYER_TIMES_API_KEY: String = ""

    /**
     *  if true Entries will be evaluated on the fly to reduce memory usage
     *  if false Entries will be loaded into memory once to speedup following requests
     *
     *  use true e.g. for Mobile or Web applications with limited resources
     *  use false e.g. for Server oder Desktop applications
     */
    var HOT_ENTRIES: Boolean = false
}