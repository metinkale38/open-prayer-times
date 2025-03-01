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
    var IGMG_API_KEY: String = System.getenv("IGMG_API_KEY") ?: ""

    /**
     * needed for london prayer times.
     * Can be obtained on their website
     */
    var LONDON_PRAYER_TIMES_API_KEY: String = System.getenv("LONDON_PRAYER_TIMES_API_KEY") ?: ""


    interface CacheProvider {
        suspend fun <T : Any> applyCache(key: String, action: suspend () -> T): T
    }


    var CACHE_PROVIDER: CacheProvider = object : CacheProvider {
        override suspend fun <T : Any> applyCache(key: String, action: suspend () -> T): T = action.invoke()
    }

}

suspend fun <T : Any> cached(key: String, action: suspend () -> T): T =
    Configuration.CACHE_PROVIDER.applyCache(key, action)