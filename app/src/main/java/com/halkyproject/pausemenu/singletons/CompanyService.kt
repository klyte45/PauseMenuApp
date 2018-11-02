package com.halkyproject.pausemenu.singletons

import android.os.AsyncTask
import com.google.gson.Gson
import com.halkyproject.pausemenu.model.Company


public class CompanyService {
    companion object {
        private var instance: CompanyService? = null
        fun getInstance(): CompanyService {
            if (instance == null) {
                instance = CompanyService()
            }
            return instance!!
        }

        private const val BASE_URL: String = "/company";

        private class InsertCompany : AsyncTask<Company, Void, Void>() {
            override fun doInBackground(vararg arr: Company): Void? {
                if (arr.isNotEmpty()) {
                    HttpService.getInstance().doRequest(
                            BASE_URL, Void::class.java, HttpService.HttpRequestMethod.POST, Gson().toJson(arr[0], Company::class.java)
                    )
                }
                return null
            }
        }

        private class UpdateCompany : AsyncTask<Company, Void, Void>() {
            override fun doInBackground(vararg arr: Company): Void? {
                if (arr.isNotEmpty()) {
                    HttpService.getInstance().doRequest(
                            BASE_URL + "/" + arr[0].id, Void::class.java, HttpService.HttpRequestMethod.PUT, Gson().toJson(arr[0], Company::class.java)
                    )
                }
                return null
            }
        }

        private class DeleteCompany : AsyncTask<Company, Void, Void>() {
            override fun doInBackground(vararg arr: Company): Void? {
                if (arr.isNotEmpty()) {
                    HttpService.getInstance().doRequest(BASE_URL + "/" + arr[0].id, Void::class.java, HttpService.HttpRequestMethod.DELETE)
                }
                return null
            }
        }

        private class GetAllCompanies : AsyncTask<Void, Void, Array<Company>>() {
            override fun doInBackground(vararg arr: Void): Array<Company>? {
                return HttpService.getInstance().doRequest(
                        BASE_URL, Array<Company>::class.java, HttpService.HttpRequestMethod.GET
                )
            }
        }

        private class FindById : AsyncTask<Int, Void, Company>() {
            override fun doInBackground(vararg params: Int?): Company? {
                if (params.isNotEmpty() && params[0] != null) {
                    return HttpService.getInstance().doRequest(
                            BASE_URL + "/" + params[0]!!, Company::class.java, HttpService.HttpRequestMethod.GET
                    )
                }
                return null
            }
        }
    }

    fun insertCompany(company: Company) {
        InsertCompany().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, company)
    }

    fun updateCompany(company: Company) {
        UpdateCompany().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, company)
    }

    fun deleteCompany(company: Company) {
        DeleteCompany().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, company)
    }

    fun findAll(): List<Company> {
        val list = ArrayList<Company>()
        val result = GetAllCompanies().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR).get();
        if (result != null) {
            list.addAll(result)
        }
        return list
    }

    fun findById(id: Int): Company? {
        return FindById().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, id).get()
    }
}