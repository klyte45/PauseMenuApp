package com.halkyproject.pausemenu.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.components.CustomTextView
import com.halkyproject.pausemenu.fragments.DatePickerFragment
import com.halkyproject.pausemenu.fragments.TimePickerFragment
import com.halkyproject.pausemenu.singletons.ConfigSingleton
import java.util.*


class Configurations : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configurations)
        ConfigSingleton.getInstance().getBirthDate().observe(this, android.arch.lifecycle.Observer {
            findViewById<CustomTextView>(R.id.val_dtNasc).text = String.format("%1\$td/%1\$Tb/%1\$tY", it)
            findViewById<CustomTextView>(R.id.val_hrNasc).text = String.format("%1\$tR", it)
            findViewById<CustomTextView>(R.id.val_tzNasc).text = String.format("GMT %1\$tz", it)
        })
    }

    fun showPickerDtNasc(v: View) {

        val ll: LinearLayout = v as LinearLayout
        val ctv: CustomTextView = ll.findViewWithTag("value")
        val newFragment = DatePickerFragment()
        newFragment.setCallback {
            ctv.text = String.format("%1\$td/%1\$Tb/%1\$tY", it)
            ConfigSingleton.getInstance().setBirthDate(it)
        }
        newFragment.currentValue = ConfigSingleton.getInstance().getBirthDateCache();
        newFragment.show(supportFragmentManager, "nascDatePicker")
    }

    fun showPickerHrNasc(v: View) {
        val ll: LinearLayout = v as LinearLayout
        val ctv: CustomTextView = ll.findViewWithTag("value")
        val newFragment = TimePickerFragment()
        newFragment.setCallback {
            ctv.text = String.format("%1\$tR", it)
            ConfigSingleton.getInstance().setBirthHour(it)
        }
        newFragment.currentValue = ConfigSingleton.getInstance().getBirthDateCache();
        newFragment.show(supportFragmentManager, "nascTimePicker")
    }


    fun showPickerTzNasc(v: View) {
        val ll: LinearLayout = v as LinearLayout
        val ctv: CustomTextView = ll.findViewWithTag("value")

        val tzSpinner = AlertDialog.Builder(this)
        val types = TimeZone.getAvailableIDs()
        tzSpinner.setItems(types) { dialog, which ->
            dialog.dismiss()
            val tz: TimeZone = TimeZone.getTimeZone(types[which])
            val cal = Calendar.getInstance()
            cal.timeZone = tz
            ctv.text = String.format("GMT %1\$tz", cal);
            ConfigSingleton.getInstance().setBirthTimeZone(tz)
        }
        tzSpinner.show()
    }

    fun goToCompanyCrud(v: View) {
        startActivity(Intent(this, CompanyCrud::class.java))
    }
}
