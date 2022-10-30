package com.metinkale.prayertimes.core


enum class Source(val tsv: String) {
    Diyanet("diyanet.tsv"),
    IGMG("igmg.tsv"),
    Semerkand("semerkand.tsv"),
    NVC("nvc.tsv"),
    London("london.tsv");


    val id = ordinal + 1
}