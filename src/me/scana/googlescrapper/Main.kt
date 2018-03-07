package me.scana.googlescrapper

import okhttp3.OkHttpClient

fun main(vararg args: String) {
    val okHttpClient = OkHttpClient.Builder().build()
    val scrapper = Scrapper(okHttpClient)

    val result = scrapper.scrap()
    result.forEach { println(it) }
}