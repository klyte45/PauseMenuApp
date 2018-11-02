package com.halkyproject.pausemenu.singletons

import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


public class HttpService {
    companion object {
        private var instance: HttpService? = null
        fun getInstance(): HttpService {
            if (instance == null) {
                instance = HttpService()
            }
            return instance!!
        }

        private val READ_TIMEOUT = 15000
        private val CONNECTION_TIMEOUT = 15000
        private const val URL_BASE: String = "http://10.0.2.2:8080/api";
    }


    public fun <T> doRequest(stringUrl: String, resultClass: Class<T>, requestMethod: HttpRequestMethod, body: String? = null, contentType: String = "application/json"): T? {
        val result: String
        var inputLine: String
        var reader: BufferedReader? = null
        var streamReader: InputStreamReader? = null
        var connection: HttpURLConnection? = null
        try {
            //Create a URL object holding our url
            val myUrl = URL(URL_BASE + stringUrl)

            //Create a connection
            connection = myUrl.openConnection() as HttpURLConnection

            //Set methods and timeouts
            connection.requestMethod = requestMethod.getValue()
            connection.readTimeout = READ_TIMEOUT
            connection.connectTimeout = CONNECTION_TIMEOUT
            val reqDate = Date()
            connection.setRequestProperty("X-Welber2", reqDate.toString())
            connection.setRequestProperty("X-Welber", SecuritySingleton.getInstance().getHash(reqDate.toString()))

            if (body != null && requestMethod != HttpRequestMethod.GET) {
                connection.setRequestProperty("Content-Type", contentType)
                val op = connection.outputStream
                op.write(body.toByteArray())
                op.close()
            }

            //Connect to our url
            connection.connect()

            //Create a new InputStreamReader
            streamReader = InputStreamReader(connection.inputStream)

            return if (resultClass == Void::class.java || connection.responseCode == 204) null else Gson().fromJson<T>(streamReader, resultClass)
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

    public enum class HttpRequestMethod(private var value: String) {
        GET("GET"),
        POST("POST"),
        DELETE("DELETE"),
        PUT("PUT");

        public fun getValue(): String {
            return value;
        }
    }

}