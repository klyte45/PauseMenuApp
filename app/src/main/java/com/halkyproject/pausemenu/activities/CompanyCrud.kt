package com.halkyproject.pausemenu.activities

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.fragments.CompanyCrudCardFragment
import com.halkyproject.pausemenu.singletons.CompanyService
import kotlinx.android.synthetic.main.activity_company_edit.*

class CompanyCrud : AppCompatActivity(), CompanyCrudCardFragment.OnFragmentInteractionListener {

    companion object {
        class ReloadAsync : AsyncTask<CompanyCrud, Void, Unit>() {
            override fun doInBackground(vararg params: CompanyCrud?) {
                params[0]?.loadCompanies()
            }
        }
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_crud)
        loadCompanies()
    }

    fun addNewCompany(v: View) {
        startActivityForResult(Intent(this, CompanyEdit::class.java), 0)
    }

    override fun onResume() {
        super.onResume()
        loadCompanies()
    }

    private fun loadCompanies() {
        val trRemove = supportFragmentManager.beginTransaction()
        for (fragment in supportFragmentManager.fragments) {
            trRemove.remove(fragment)
        }
        trRemove.commit()

        val companies = CompanyService.findAll()
        val trAdd = supportFragmentManager.beginTransaction()
        for (comp in companies) {
            val frag = CompanyCrudCardFragment.newInstance(comp)
            trAdd.add(scrollLayout.id, frag, "item" + comp.id)
        }
        trAdd.commit()
    }
}
