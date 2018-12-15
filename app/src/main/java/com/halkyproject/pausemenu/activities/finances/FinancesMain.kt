package com.halkyproject.pausemenu.activities.finances

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.activities.finances.reports.CurrentBalanceReport
import com.halkyproject.pausemenu.activities.finances.reports.ProjectBalanceReport
import com.halkyproject.pausemenu.superclasses.BasicActivity

class FinancesMain : BasicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finances__main)
    }

    fun openAccountScreen(v: View) {
        startActivity(Intent(this, FinancesAccountList::class.java))
    }

    fun openMovementSourcesScreen(v: View) {
        startActivity(Intent(this, MovementSourceList::class.java))
    }

    fun openCurrentBalanceRelatory(v: View) {
        startActivity(Intent(this, CurrentBalanceReport::class.java))
    }

    fun openProjection6m(v: View) {
        val intent = Intent(this, ProjectBalanceReport::class.java)
        val b = Bundle()
        b.putInt(ProjectBalanceReport.KEY_MONTH_QUANTITY_PROJECTION, 6)
        intent.putExtras(b)
        startActivity(intent)
    }

    fun openProjection12m(v: View) {
        val intent = Intent(this, ProjectBalanceReport::class.java)
        val b = Bundle()
        b.putInt(ProjectBalanceReport.KEY_MONTH_QUANTITY_PROJECTION, 12)
        intent.putExtras(b)
        startActivity(intent)
    }

    fun openProjection24m(v: View) {
        val intent = Intent(this, ProjectBalanceReport::class.java)
        val b = Bundle()
        b.putInt(ProjectBalanceReport.KEY_MONTH_QUANTITY_PROJECTION, 24)
        intent.putExtras(b)
        startActivity(intent)
    }

    fun openProjection36m(v: View) {
        val intent = Intent(this, ProjectBalanceReport::class.java)
        val b = Bundle()
        b.putInt(ProjectBalanceReport.KEY_MONTH_QUANTITY_PROJECTION, 36)
        intent.putExtras(b)
        startActivity(intent)
    }

    fun openProjection60m(v: View) {
        val intent = Intent(this, ProjectBalanceReport::class.java)
        val b = Bundle()
        b.putInt(ProjectBalanceReport.KEY_MONTH_QUANTITY_PROJECTION, 60)
        intent.putExtras(b)
        startActivity(intent)
    }

}
