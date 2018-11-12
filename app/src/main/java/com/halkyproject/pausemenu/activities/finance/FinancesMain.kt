package com.halkyproject.pausemenu.activities.finance

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.halkyproject.pausemenu.R

class FinancesMain : AppCompatActivity() {

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

}
