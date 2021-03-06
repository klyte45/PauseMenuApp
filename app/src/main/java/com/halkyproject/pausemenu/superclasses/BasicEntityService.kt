package com.halkyproject.pausemenu.superclasses

import android.os.AsyncTask
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.halkyproject.lifehack.interfaces.BasicEntityModel
import com.halkyproject.pausemenu.singletons.HttpService
import java.util.*


abstract class BasicEntityService<T, U> where T : BasicEntityModel<T> {

    abstract fun getBaseUrl(): String
    abstract fun getEntityUri(): String
    abstract fun getClassEntity(): Class<T>
    abstract fun getClassEntityArray(): Class<Array<T>>

    companion object {
        var gson: Gson? = null
            get() {
                if (field == null) {
                    val gsonBuilder = GsonBuilder()
                    gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                    return gsonBuilder.create()
                }
                return field
            }
            set(x) {}
    }

    private class Insert<T>(val baseUrl: String) : AsyncTask<T, Void, Boolean>() where T : BasicEntityModel<T> {
        override fun doInBackground(vararg arr: T): Boolean {

            if (arr.isNotEmpty()) {
                val type = object : TypeToken<T>() {}.type
                HttpService.doRequest(
                        baseUrl, Void::class.java, HttpService.HttpRequestMethod.POST, BasicEntityService.gson!!.toJson(arr[0], type)
                )
                return true
            }

            return false
        }
    }

    private class Update<T>(val baseUrl: String) : AsyncTask<T, Void, Boolean>() where T : BasicEntityModel<T> {
        override fun doInBackground(vararg arr: T): Boolean {
            if (arr.isNotEmpty()) {
                HttpService.doRequest(
                        baseUrl + "/" + arr[0].getIdentifier(), Void::class.java, HttpService.HttpRequestMethod.PUT, BasicEntityService.gson!!.toJson(arr[0], object : TypeToken<T>() {}.type)
                )
                return true
            }
            return false
        }
    }

    private class Delete<T>(val baseUrl: String) : AsyncTask<T, Void, Boolean>() where T : BasicEntityModel<T> {
        override fun doInBackground(vararg arr: T): Boolean {
            if (arr.isNotEmpty()) {
                HttpService.doRequest(baseUrl + "/" + arr[0].getIdentifier(), Void::class.java, HttpService.HttpRequestMethod.DELETE)
                return true
            }
            return false
        }
    }

    private class GetAll<T>(val baseUrl: String, val resultClass: Class<Array<T>>) : AsyncTask<Void, Void, Array<T>>() where T : BasicEntityModel<T> {
        override fun doInBackground(vararg arr: Void): Array<T>? {
            return HttpService.doRequest(
                    baseUrl, resultClass, HttpService.HttpRequestMethod.GET
            ).first
        }
    }

    private class SearchByFilter<T, U>(val baseUrl: String, val resultClass: Class<Array<T>>) : AsyncTask<U, Void, Array<T>>() where T : BasicEntityModel<T> {
        override fun doInBackground(vararg arr: U): Array<T>? {
            val gson = Gson()
            val filterMap: Map<String, Any?> = gson.fromJson(gson.toJson(arr[0]), object : TypeToken<Map<String, Any?>>() {}.type)
            return HttpService.doRequest(
                    "$baseUrl/search", resultClass, HttpService.HttpRequestMethod.POST, gson.toJson(filterMap.filterValues { x -> x != null })
            ).first
        }
    }

    private class GetPkOptions<T>(val entityUri: String, val entityField: String, val resultClass: Class<Array<T>>, val targetEntityUri: String) : AsyncTask<Void, Void, Array<T>>() where T : BasicEntityModel<T> {
        override fun doInBackground(vararg arr: Void): Array<T>? {
            return HttpService.doRequest("/api/pk/$entityUri/$entityField/$targetEntityUri", resultClass, HttpService.HttpRequestMethod.GET).first
        }
    }

    private class FindById<T>(val baseUrl: String, val resultClass: Class<T>) : AsyncTask<Int, Void, T>() where T : BasicEntityModel<T> {
        override fun doInBackground(vararg params: Int?): T? {
            if (params.isNotEmpty() && params[0] != null) {
                return HttpService.doRequest(
                        baseUrl + "/" + params[0]!!, resultClass, HttpService.HttpRequestMethod.GET
                ).first
            }
            return null
        }

    }

    fun insert(entity: T): AsyncTask<T, Void, Boolean> {
        return Insert<T>(getBaseUrl()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, entity)
    }

    fun update(entity: T): AsyncTask<T, Void, Boolean> {
        return Update<T>(getBaseUrl()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, entity)
    }

    fun delete(entity: T): AsyncTask<T, Void, Boolean> {
        return Delete<T>(getBaseUrl()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, entity)
    }

    fun findAll(): List<T> {
        val list = ArrayList<T>()
        val result = GetAll(getBaseUrl(), getClassEntityArray()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR).get()
        if (result != null) {
            list.addAll(result)
        }
        return list
    }

    fun search(filter: U): List<T> {
        val list = ArrayList<T>()
        val result = SearchByFilter<T, U>(getBaseUrl(), getClassEntityArray()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, filter).get()
        if (result != null) {
            list.addAll(result)
        }
        return list
    }

    fun findById(id: Int): T? {
        return FindById(getBaseUrl(), getClassEntity()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, id).get()
    }

    fun getPkForEntity(toEntity: String, toField: String): List<T> {
        val list = ArrayList<T>()
        val result = GetPkOptions(toEntity, toField, getClassEntityArray(), getEntityUri()).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR).get()
        if (result != null) {
            list.addAll(result)
        }
        return list
    }
}