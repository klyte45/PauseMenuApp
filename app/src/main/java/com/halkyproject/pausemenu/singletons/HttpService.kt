package com.halkyproject.pausemenu.singletons

import com.google.gson.Gson
import com.halkyproject.lifehack.model.entities.ErrorModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


object HttpService {
    private val READ_TIMEOUT = 15000
    private val CONNECTION_TIMEOUT = 15000


    fun <T> doRequest(stringUrl: String, resultClass: Class<T>, requestMethod: HttpRequestMethod, body: String? = null, contentType: String = "application/json"): Pair<T?, Int> {

        val reader: BufferedReader? = null
        var streamReader: InputStreamReader? = null
        var connection: HttpURLConnection? = null

        try {
            val pair = doConnection(stringUrl, requestMethod, body, contentType)
            connection = pair.first
            streamReader = pair.second

            return Pair(if (resultClass == Void::class.java || connection!!.responseCode == 204) null else Gson().fromJson<T>(streamReader, resultClass), connection!!.responseCode)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        } finally {
            connection?.disconnect()
            try {
                reader?.close()
            } catch (ignored: Exception) {
            }

            try {
                streamReader?.close()
            } catch (ignored: Exception) {
            }

        }
    }


    private fun doConnection(stringUrl: String, requestMethod: HttpRequestMethod, body: String?, contentType: String): Pair<HttpURLConnection?, InputStreamReader?> {
        //Create a URL object holding our url
        val connection1: HttpURLConnection
        val streamReader1: InputStreamReader
        val effectiveUrl = URL(ConfigSingleton.getServerUrlSync() + stringUrl)

        //Create a connection
        connection1 = effectiveUrl.openConnection() as HttpURLConnection

        //Set methods and timeouts
        connection1.requestMethod = requestMethod.value
        connection1.readTimeout = READ_TIMEOUT
        connection1.connectTimeout = CONNECTION_TIMEOUT
        val reqDate = Date()
        connection1.setRequestProperty("X-Welber2", reqDate.time.toString())
        connection1.setRequestProperty("X-Welber", SecuritySingleton.getHash(reqDate.time.toString()))

        if (body != null && requestMethod != HttpRequestMethod.GET) {
            connection1.setRequestProperty("Content-Type", contentType)
            val op = connection1.outputStream
            op.write(body.toByteArray())
            op.close()
        }

        if (connection1.responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
            streamReader1 = InputStreamReader(connection1.inputStream)
        } else {
            /* error from server */
            streamReader1 = InputStreamReader(connection1.errorStream)
            val model: ErrorModel? = Gson().fromJson(streamReader1, ErrorModel::class.java)
            throw Exception("$effectiveUrl => Status code ${connection1.responseCode}: ${model?.message}\nBODY: $body")
        }


        return Pair(connection1, streamReader1)
    }

    enum class HttpRequestMethod(val value: String) {
        GET("GET"),
        POST("POST"),
        DELETE("DELETE"),
        PUT("PUT");
    }

}