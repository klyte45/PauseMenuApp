package com.halkyproject.pausemenu.activities

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.content.Intent
import android.graphics.Color
import android.icu.text.NumberFormat
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.TextView
import com.halkyproject.lifehack.model.enums.Currency
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.activities.finances.FinancesMain
import com.halkyproject.pausemenu.activities.player.LoggPlayer
import com.halkyproject.pausemenu.components.CustomTextView
import com.halkyproject.pausemenu.singletons.ConfigSingleton
import com.halkyproject.pausemenu.singletons.finances.FinancesService.getCurrentBalancePhysicalLocal
import com.halkyproject.pausemenu.superclasses.BasicActivity
import org.apache.commons.lang3.StringUtils
import java.util.*


class PauseMenuMain : BasicActivity() {

    companion object {
        private const val FINANCES_PIN_REQUEST_CODE = 1
        private val ZCA_MONTHS = arrayOf("DOM", "KLY", "NAT")
    }

    override fun onCreate(savedInstanceState: Bundle?) = safeExecute({}()) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pause_menu_main)
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0)
        val thread = object : Thread() {
            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        Thread.sleep(1000)
                        runOnUiThread {
                            getCurrentScore().observe(this@PauseMenuMain, Observer<Long> {
                                val currentScore = StringUtils.leftPad(it.toString(), 10, '0')
                                val spanScore = SpannableString("SCORE $currentScore")
                                spanScore.setSpan(ForegroundColorSpan(Color.YELLOW), 0, 5, 0);
                                findViewById<CustomTextView>(R.id.score).setText(spanScore, TextView.BufferType.SPANNABLE)
                            })
                            findViewById<CustomTextView>(R.id.dateZ).text = getCurrentDateFormat()
                        }
                    }
                } catch (e: InterruptedException) {
                }

            }
        }
        thread.start()

        val strPlanos = SpannableString("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
        for (i in 0..25) {
            if (i < PLN_STATUS.size) {
                strPlanos.setSpan(ForegroundColorSpan(PLN_STATUS[i].defColor), i, i + 1, 0);
            } else {
                strPlanos.setSpan(RelativeSizeSpan(0f), i, 26, 0);
                break
            }
        }
        findViewById<CustomTextView>(R.id.item_pessoasPlanosList).text = strPlanos;


    }

    private val PLN_STATUS = arrayOf(PlanStatus.ACTIVE, PlanStatus.WAITING, PlanStatus.DENIED, PlanStatus.WAITING)

    private fun getCurrentScore(): LiveData<Long> {
        return Transformations.map(ConfigSingleton.getBirthDate()) {
            (Date().time - it.time.time) / 1000L
        }
    }

    private fun getCurrentDateFormat(): SpannableString {
        val date = Date();
        val day = StringUtils.leftPad(date.date.toString(), 2, '0')
        val month = ZCA_MONTHS[date.month % 3];
        val year = (date.year * 4) - 250 + Math.floor(date.month / 4.0).toInt()
        val spanStr = SpannableString("$day$month$year")
        spanStr.setSpan(ForegroundColorSpan(Color.YELLOW), 2, 5, 0)

        return spanStr
    }

    private enum class PlanStatus(val defColor: Int) {
        NONE(Color.GRAY),
        ACTIVE(Color.WHITE),
        DELAYED(Color.YELLOW),
        WAITING(Color.GREEN),
        DENIED(Color.RED);
    }

    //ações
    fun goToConfig(v: View) = safeExecute({}()) {
        startActivity(Intent(this, Configurations::class.java))
    }


    fun goToLoggPlayer(v: View) = safeExecute({}()) {
        startActivity(Intent(this, LoggPlayer::class.java))
    }

    fun goToFinances(v: View) = safeExecute({}()) {
        startActivityForResult(Intent(this, PinInput::class.java), FINANCES_PIN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = safeExecute({}()) {
        if (requestCode == FINANCES_PIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                startActivity(Intent(this, FinancesMain::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val currencyFormatter = NumberFormat.getCurrencyInstance(Currency.BRL.locale)
        val currentSaldo = currencyFormatter.format(getCurrentBalancePhysicalLocal())
        val spanSaldo = SpannableString(currentSaldo);
        spanSaldo.setSpan(ForegroundColorSpan(Color.YELLOW), currentSaldo.indexOf(currencyFormatter.currency.symbol), currencyFormatter.currency.symbol.length, 0);
        findViewById<CustomTextView>(R.id.saldo).text = spanSaldo
    }
}
