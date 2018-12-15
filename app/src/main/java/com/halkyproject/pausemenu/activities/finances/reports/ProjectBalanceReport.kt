package com.halkyproject.pausemenu.activities.finances.reports

import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.adapter.DefaultListPageAdapter
import com.halkyproject.pausemenu.auxobjs.finances.ExtractEntry
import com.halkyproject.pausemenu.fragments.BasicTableTitleFragment
import com.halkyproject.pausemenu.fragments.finances.report.ProjectionAccountItemFragment
import com.halkyproject.pausemenu.fragments.finances.report.ProjectionExtractItemFragment
import com.halkyproject.pausemenu.interfaces.OnFragmentInteractionListener
import com.halkyproject.pausemenu.singletons.finances.FinancesService
import com.halkyproject.pausemenu.superclasses.BasicActivity
import com.halkyproject.pausemenu.superclasses.BasicFragment
import kotlinx.android.synthetic.main.activity__basic_report.*
import java.math.BigDecimal
import java.text.DateFormat
import java.util.*

class ProjectBalanceReport : BasicActivity(), OnFragmentInteractionListener {

    companion object {
        const val KEY_MONTH_QUANTITY_PROJECTION = "MonthQuant"
    }

    private var monthsToProject: Int? = null

    override fun onFragmentInteraction(uri: Uri) {

    }


    lateinit var listBalances: LinearLayout
    lateinit var listExtract: LinearLayout

    private class GetData(val startDate: Date, val endDate: Date) : AsyncTask<ProjectBalanceReport, Void, ProjectBalanceReport>() {
        private lateinit var balances: Pair<Map<Int, FinancialAccount>, List<ExtractEntry>>

        override fun doInBackground(vararg params: ProjectBalanceReport): ProjectBalanceReport {
//            Log.w("XXX", "$startDate a $endDate 1")
            if (params.isEmpty()) throw IllegalStateException("Cadê a Activity?")
            try {
//                Log.w("XXX", "$startDate a $endDate")
                balances = FinancesService.doProjection(startDate, endDate)
                return params[0]
            } catch (e: Exception) {
                params[0].runOnUiThread { params[0].onErrorThrown(e) }
                throw e
            }
        }

        override fun onPostExecute(result: ProjectBalanceReport?) {
            super.onPostExecute(result)
            try {
                with(result!!) {
                    if (supportFragmentManager.fragments.isNotEmpty()) {
                        val trRemove = supportFragmentManager.beginTransaction()
                        for (fragment in supportFragmentManager.fragments) {
                            trRemove.remove(fragment)
                        }
                        trRemove.commit()
                    }
                    if (balances.first.isNotEmpty()) {
                        val accounts = balances.first.map { it.value }.sortedBy { it.type }
                        val trAdd = supportFragmentManager.beginTransaction()
                        val fragTtAccount = BasicFragment.newInstance(String.format(getString(R.string.finances_accountBalancesAt), DateFormat.getDateInstance().format(endDate)), BasicTableTitleFragment::class.java)
                        trAdd.add(listBalances.id, fragTtAccount, "titleAcc")
                        for (account in accounts) {
                            val fragIt = BasicFragment.newInstance(account, ProjectionAccountItemFragment::class.java)
                            trAdd.add(listBalances.id, fragIt, "accountId_" + account.id)
                        }
                        if (balances.second.isNotEmpty()) {
                            val entries = balances.second.map { it.getSingleEntries(balances.first) }.flatten().asSequence().filter { it.value != BigDecimal.ZERO }.sortedBy { it.date }.toList()
                            val fragTtExtract = BasicFragment.newInstance(getString(R.string.finances_extract), BasicTableTitleFragment::class.java)
                            trAdd.add(listExtract.id, fragTtExtract, "titleExt")
                            for (entry in entries) {
                                val fragIt = BasicFragment.newInstance(entry, ProjectionExtractItemFragment::class.java)
                                trAdd.add(listExtract.id, fragIt, "entry_" + entry.date + "_" + entry.value + "_" + entry.account.id)
                            }
                        }
                        trAdd.commit()
                    }
                    closeLoadingScreen()
                }

            } catch (e: Exception) {
                result!!.runOnUiThread { result.onErrorThrown(e) }
            } finally {
                result?.closeLoadingScreen()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) = safeExecute({}(), true) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__basic_report)
        Log.w("XXX", "ON CREATE!!!!")
        monthsToProject = intent?.extras?.getInt(KEY_MONTH_QUANTITY_PROJECTION) ?: throw IllegalStateException("Quantidade de meses deve ser informada!")
        tt_title.text = String.format(getString(R.string.finances_projectionMonths), monthsToProject)
        showLoadingScreen()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, monthsToProject!!)
        m_pager.adapter = DefaultListPageAdapter(this)
        listBalances = ((m_pager.adapter as DefaultListPageAdapter).instantiateItem(this.m_pager, 0) as ViewGroup).findViewById(R.id.m_listing)
        listExtract = ((m_pager.adapter as DefaultListPageAdapter).instantiateItem(this.m_pager, 0) as ViewGroup).findViewById(R.id.m_listing)
        GetData(Calendar.getInstance().time, endDate.time).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this)

    }

}