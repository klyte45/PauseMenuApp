//com.halkyproject.pausemenu.singletons=com.halkyproject.lifehack.model.finances.FinancialAccount=AccountService;
package com.halkyproject.pausemenu.singletons

import android.os.AsyncTask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.pausemenu.superclasses.BasicEntityService

typealias T = com.halkyproject.lifehack.model.finances.FinancialAccount


object AccountService : BasicEntityService<T> {
    private const val BASE_URL: String = T.BASE_URL

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

    private class SearchByFilter : AsyncTask<FinancialAccountFilter, Void, Array<T>>() {
        override fun doInBackground(vararg arr: FinancialAccountFilter): Array<T>? {
            val gson = Gson()
            val filterMap: Map<String, Any?> = gson.fromJson(gson.toJson(arr[0]), object : TypeToken<Map<String, Any?>>() {}.type)
            return HttpService.doRequest(
                    "$BASE_URL/search", Array<T>::class.java, HttpService.HttpRequestMethod.POST, gson.toJson(filterMap.filterValues { x -> x != null })
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

    override fun insert(T: T): AsyncTask<T, Void, Boolean> {
        return Insert().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, T)
    }

    override fun update(T: T): AsyncTask<T, Void, Boolean> {
        return Update().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, T)
    }

    override fun delete(T: T): AsyncTask<T, Void, Boolean> {
        return Delete().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, T)
    }

    override fun findAll(): List<T> {
        val list = ArrayList<T>()
        val result = GetAll().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR).get()
        if (result != null) {
            list.addAll(result)
        }
        return list
    }

    override fun search(accountFilter: FinancialAccountFilter): List<T> {
        val list = ArrayList<T>()
        val result = SearchByFilter().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, accountFilter).get()
        if (result != null) {
            list.addAll(result)
        }
        return list
    }

    override fun findById(id: Int): T? {
        return FindById().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, id).get()
    }

    class FinancialAccountFilter(val type: FinancialAccount.AccountType?, val active: Boolean?) {}
}