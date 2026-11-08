package com.vlu.newsreader

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object HttpClient {

    /** Safe entry point: always runs on the I/O thread. */
    suspend fun get(url: String): String = withContext(Dispatchers.IO) { getBlocking(url) }

    /** Raw blocking call. Never call from the main thread (Step 6 does it on purpose). */
    fun getBlocking(url: String): String {
        val conn = URL(url).openConnection() as HttpURLConnection
        try {
            conn.requestMethod = "GET"
            conn.connectTimeout = 10_000
            conn.readTimeout = 10_000
            val code = conn.responseCode
            if (code != HttpURLConnection.HTTP_OK) throw IOException("HTTP $code")
            return conn.inputStream.bufferedReader().use { it.readText() }
        } finally {
            conn.disconnect()
        }
    }
}
