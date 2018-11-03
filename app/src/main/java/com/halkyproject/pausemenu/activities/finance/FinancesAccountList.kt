package com.halkyproject.pausemenu.activities.finance

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.fragments.AccountCrudCardFragment
import com.halkyproject.pausemenu.singletons.AccountService
import kotlinx.android.synthetic.main.activity_finances_account_list.*

class FinancesAccountList : AppCompatActivity(), AccountCrudCardFragment.OnFragmentInteractionListener {

    companion object {
        class ReloadAsync : AsyncTask<FinancesAccountList, Void, Unit>() {
            override fun doInBackground(vararg params: FinancesAccountList?) {
                params[0]?.reload()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finances_account_list)
        reload()
    }

    private fun reload() {
        val trRemove = supportFragmentManager.beginTransaction();
        for (fragment in supportFragmentManager.fragments) {
            trRemove.remove(fragment)
        }
        trRemove.commit()

        val companies = AccountService.findAll()
        val trAdd = supportFragmentManager.beginTransaction()
        for (comp in companies) {
            val frag = AccountCrudCardFragment.newInstance(comp);
            trAdd.add(scrollLayout.id, frag, "item" + comp.id)
        }
        trAdd.commit()
    }

    fun addNew(v: View) {
        startActivityForResult(Intent(this, FinancesAccountEdit::class.java), 0)
    }

    override fun onResume() {
        super.onResume()
        reload()
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
