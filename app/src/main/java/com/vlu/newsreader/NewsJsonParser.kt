package com.vlu.newsreader

import com.google.gson.Gson
import com.vlu.newsreader.model.NewsItem
import org.json.JSONArray

object NewsJsonParser {

    /** Manual: JSONArray + getJSONObject(i). Throws JSONException on bad or missing fields. */
    fun parseManual(json: String): List<NewsItem> {
        val arr = JSONArray(json)
        return List(arr.length()) { i ->
            val o = arr.getJSONObject(i)
            NewsItem(o.getInt("id"), o.getInt("userId"), o.getString("title"), o.getString("body"))
        }
    }

    /** Automated: Gson maps JSON keys via @SerializedName. Throws JsonSyntaxException on bad JSON. */
    fun parseGson(json: String): List<NewsItem> =
        Gson().fromJson(json, Array<NewsItem>::class.java).toList()
}
