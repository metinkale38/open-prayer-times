package dev.metinkale.prayertimes.core.geo

import dev.metinkale.prayertimes.core.utils.normalize
import kotlinx.datetime.TimeZone

data class Geolocation(
    /** integer id of record in geonames database */
    val geonameid: String,
    /** name of geographical point (utf8) varchar(200) */
    val name: String,
    /** name of geographical point in plain ascii characters, varchar(200) */
    val asciiname: String,
    /** alternatenames, comma separated, ascii names automatically transliterated, convenience attribute from alternatename table, varchar(10000) */
    val alternatenames: String,
    /** latitude in decimal degrees (wgs84) */
    val latStr: String,
    /** longitude in decimal degrees (wgs84) */
    val lngStr: String,
    /** see http://www.geonames.org/export/codes.html, char(1) */
    val feature_class: String,
    /** see http://www.geonames.org/export/codes.html, varchar(10) */
    val feature_code: String,
    /** ISO-3166 2-letter country code, 2 characters */
    val country_code: String,
    /** alternate country codes, comma separated, ISO-3166 2-letter country code, 200 characters */
    val cc2: String,
    /** fipscode (subject to change to iso code), see exceptions below, see file admin1Codes.txt for display names of this code; varchar(20) */
    val admin1_code: String,
    /** code for the second administrative division, a county in the US, see file admin2Codes.txt; varchar(80) */
    val admin2_code: String,
    /** code for third level administrative division, varchar(20) */
    val admin3_code: String,
    /** code for fourth level administrative division, varchar(20) */
    val admin4_code: String,
    /** bigint (8 byte int) */
    val populationStr: String,
    /** in meters, integer */
    val elvStr: String,
    /** digital elevation model, srtm3 or gtopo30, average elevation of 3''x3'' (ca 90mx90m) or 30''x30'' (ca 900mx900m) area in meters, integer. srtm processed by cgiar/ciat. */
    val dem: String,
    /** the iana timezone id (see file timeZone.txt) varchar(40) */
    val zoneId: String,
    /** date of last modification in yyyy-MM-dd format */
    val modification_date: String
) {
    constructor(params: List<String>) : this(
        params[0],
        params[1],
        params[2],
        params[3],
        params[4],
        params[5],
        params[6],
        params[7],
        params[8],
        params[9],
        params[10],
        params[11],
        params[12],
        params[13],
        params[14],
        params[15],
        params[16],
        params[17],
        params[18]
    )

    val lat by lazy { latStr.toDouble() }
    val lng by lazy { lngStr.toDouble() }
    val elv by lazy { elvStr.toDouble() }
    val population by lazy { populationStr.toDouble() }
    val timezone by lazy { TimeZone.of(zoneId) }
    internal val normalizedNames by lazy {
        listOf(
            name.normalize(),
            asciiname.normalize(),
            *alternatenames.split(",").map { it.normalize() }.toTypedArray()
        )
    }
}