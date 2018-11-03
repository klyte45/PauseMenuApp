package com.halkyproject.pausemenu.singletons

import android.os.AsyncTask
import com.google.gson.Gson
import com.halkyproject.pausemenu.model.finances.FinancialAccount

typealias T = FinancialAccount


object AccountService {
    private const val BASE_URL: String = "/fin/accounts"

    private class Insert : AsyncTask<T, Void, Boolean>() {
        override fun doInBackground(vararg arr: T): Boolean {
            try {
                if (arr.isNotEmpty()) {
                    HttpService.doRequest(
                            BASE_URL, Void::class.java, HttpService.HttpRequestMethod.POST, Gson().toJson(arr[0], T::class.java)
                    )
                    return true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
    }

    private class Update : AsyncTask<T, Void, Boolean>() {
        override fun doInBackground(vararg arr: T): Boolean {
            try {
                if (arr.isNotEmpty()) {
                    HttpService.doRequest(
                            BASE_URL + "/" + arr[0].id, Void::class.java, HttpService.HttpRequestMethod.PUT, Gson().toJson(arr[0], T::class.java)
                    )
                    return true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
    }

    private class Delete : AsyncTask<T, Void, Boolean>() {
        override fun doInBackground(vararg arr: T): Boolean {
            try {
                if (arr.isNotEmpty()) {
                    HttpService.doRequest(BASE_URL + "/" + arr[0].id, Void::class.java, HttpService.HttpRequestMethod.DELETE)
                    return true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }
    }

    private class GetAll : AsyncTask<Void, Void, Array<T>>() {
        override fun doInBackground(vararg arr: Void): Array<T>? {
            return HttpService.doRequest(
                    BASE_URL, Array<T>::class.java, HttpService.HttpRequestMethod.GET
            )
        }
    }

    private class FindById : AsyncTask<Int, Void, T>() {
        override fun doInBackground(vararg params: Int?): T? {
            if (params.isNotEmpty() && params[0] != null) {
                return HttpService.doRequest(
                        BASE_URL + "/" + params[0]!!, T::class.java, HttpService.HttpRequestMethod.GET
                )
            }
            return null
        }

    }

    fun insert(T: T): AsyncTask<T, Void, Boolean> {
        return Insert().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, T)
    }

    fun update(T: T): AsyncTask<T, Void, Boolean> {
        return Update().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, T)
    }

    fun delete(T: T): AsyncTask<T, Void, Boolean> {
        return Delete().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, T)
    }

    fun findAll(): List<T> {
        val list = ArrayList<T>()
        val result = GetAll().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR).get()
        if (result != null) {
            list.addAll(result)
        }
        return list
    }

    fun findById(id: Int): T? {
        return FindById().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, id).get()
    }

}