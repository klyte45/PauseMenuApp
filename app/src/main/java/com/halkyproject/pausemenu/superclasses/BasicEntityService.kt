package com.halkyproject.pausemenu.superclasses

import android.os.AsyncTask
import com.halkyproject.lifehack.model.BasicEntityModel
import com.halkyproject.pausemenu.singletons.AccountService

interface BasicEntityService<T> where T : BasicEntityModel {
    fun insert(T: T): AsyncTask<T, Void, Boolean>

    fun update(T: T): AsyncTask<T, Void, Boolean>

    fun delete(T: T): AsyncTask<T, Void, Boolean>

    fun findAll(): List<T>

    fun search(accountFilter: AccountService.FinancialAccountFilter): List<T>

    fun findById(id: Int): T?
}