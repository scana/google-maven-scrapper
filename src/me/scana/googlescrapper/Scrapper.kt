package me.scana.googlescrapper

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.xml.namespace.QName
import javax.xml.stream.XMLInputFactory

val GOOGLE_MAVEN_URL = HttpUrl.parse("https://dl.google.com/dl/android/maven2/")!!
val MASTER_INDEX_URL = HttpUrl.parse("https://dl.google.com/dl/android/maven2/master-index.xml")!!
const val GROUP_INDEX = "group-index.xml"

val XML_INPUT_FACTORY: XMLInputFactory = XMLInputFactory.newFactory()

class Scrapper(private val okHttpClient: OkHttpClient) {

    fun scrap(): List<String> {
        val groups = fetchGroups()
        return groups.flatMap { fetchArtifacts(it) }
    }

    private fun fetchGroups(): List<String> {
        val request = Request.Builder()
                .url(MASTER_INDEX_URL)
                .build()
        val response = okHttpClient.newCall(request).execute()
        return response.use {
            val result = mutableListOf<String>()
            val reader = XML_INPUT_FACTORY.createXMLEventReader(response.body()!!.charStream())
            while (reader.hasNext()) {
                val event = reader.nextEvent()
                if (event.isStartElement && event.asStartElement().name.localPart.contains(".")) {
                    result.add(event.asStartElement().name.localPart)
                }
            }
            return@use result
        }
    }

    private fun fetchArtifacts(group: String): List<String> {
        val url = GOOGLE_MAVEN_URL.newBuilder()
                .addPathSegments(pathForGroup(group))
                .addPathSegment(GROUP_INDEX)
                .build()
        val request = Request.Builder()
                .url(url)
                .build()
        val response = okHttpClient.newCall(request).execute()
        return response.use {
            val result = mutableListOf<String>()
            val reader = XML_INPUT_FACTORY.createXMLEventReader(response.body()!!.charStream())
            while (reader.hasNext()) {
                val event = reader.nextEvent()
                if (event.isStartElement && event.asStartElement().getAttributeByName(QName("versions")) != null) {
                    result.add(event.asStartElement().name.localPart)
                }
            }
            return@use result.map { "$group:$it" }
        }
    }

    private fun pathForGroup(group: String): String {
        return group.replace(".", "/")
    }

}
