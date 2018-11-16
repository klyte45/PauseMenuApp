package com.halkyproject.pausemenu.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.components.CustomEditText
import com.halkyproject.pausemenu.components.CustomTextView
import com.halkyproject.pausemenu.fragments.DatePickerFragment
import com.halkyproject.pausemenu.fragments.TimePickerFragment
import com.halkyproject.pausemenu.singletons.ConfigSingleton
import com.halkyproject.pausemenu.superclasses.BasicActivity
import kotlinx.android.synthetic.main.activity_configurations.*
import java.util.*


class Configurations : BasicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) = safeExecute({}()) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configurations)
        ConfigSingleton.getBirthDate().observe(this, android.arch.lifecycle.Observer {
            findViewById<CustomTextView>(R.id.val_dtNasc).text = String.format("%1\$td/%1\$Tb/%1\$tY", it)
            findViewById<CustomTextView>(R.id.val_hrNasc).text = String.format("%1\$tR", it)
            findViewById<CustomTextView>(R.id.val_tzNasc).text = String.format("GMT %1\$tz", it)
        })
        ConfigSingleton.getServerUrl().observe(this, android.arch.lifecycle.Observer {
            val input = findViewById<CustomEditText>(R.id.val_serverUrl)
            input.setText(it, TextView.BufferType.EDITABLE)
        })
        closeLoadingScreen()
    }

    fun showPickerDtNasc(v: View) = safeExecute({}()) {

        val ll: LinearLayout = v as LinearLayout
        val ctv: CustomTextView = ll.findViewWithTag("value")
        val newFragment = DatePickerFragment()
        newFragment.setCallback {
            ctv.text = String.format("%1\$td/%1\$Tb/%1\$tY", it)
            ConfigSingleton.setBirthDate(it)
        }
        newFragment.currentValue = ConfigSingleton.getBirthDateCache()
        newFragment.show(supportFragmentManager, "nascDatePicker")
    }

    fun showPickerHrNasc(v: View) = safeExecute({}()) {
        val ll: LinearLayout = v as LinearLayout
        val ctv: CustomTextView = ll.findViewWithTag("value")
        val newFragment = TimePickerFragment()
        newFragment.setCallback {
            ctv.text = String.format("%1\$tR", it)
            ConfigSingleton.setBirthHour(it)
        }
        newFragment.currentValue = ConfigSingleton.getBirthDateCache()
        newFragment.show(supportFragmentManager, "nascTimePicker")
    }


    fun showPickerTzNasc(v: View) = safeExecute({}()) {
        val ll: LinearLayout = v as LinearLayout
        val ctv: CustomTextView = ll.findViewWithTag("value")

        val tzSpinner = AlertDialog.Builder(this)
        val types = TimeZone.getAvailableIDs()
        tzSpinner.setItems(types) { dialog, which ->
            dialog.dismiss()
            val tz: TimeZone = TimeZone.getTimeZone(types[which])
            val cal = Calendar.getInstance()
            cal.timeZone = tz
            ctv.text = String.format("GMT %1\$tz", cal)
            ConfigSingleton.setBirthTimeZone(tz)
        }
        tzSpinner.show()
    }

    fun savePin(v: View) = safeExecute({}()) {
        showLoadingScreen()

        ConfigSingleton.getPin().observe(this, android.arch.lifecycle.Observer {
            try {
                if (it == val_currentPin.text.toString()) {
                    if (val_newPin.text.toString().length >= 6) {
                        if (val_confirmPin.text.toString() == val_newPin.text.toString()) {
                            if (ConfigSingleton.setPin(val_confirmPin.text.toString()).get()) {
                                Toast.makeText(applicationContext, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                                val_newPin.setText("", TextView.BufferType.EDITABLE)
                                val_confirmPin.setText("", TextView.BufferType.EDITABLE)
                                val_currentPin.setText("", TextView.BufferType.EDITABLE)
                            } else {
                                Toast.makeText(applicationContext, "Erro ao salvar!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(applicationContext, "Senhas novas não conferem!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Senha nova muito curta!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Senha atual inválida!", Toast.LENGTH_SHORT).show()
                }
            } finally {
                closeLoadingScreen()
            }
        })
    }

    fun saveServerUrl(v: View) = safeExecute({}()) {
        showLoadingScreen()
        try {
            if (ConfigSingleton.setServerUrl(val_serverUrl.text.toString()).get()) {
                Toast.makeText(applicationContext, "Servidor salvo!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Erro ao salvar servidor!", Toast.LENGTH_SHORT).show()
            }
        } finally {
            closeLoadingScreen()
        }
    }

    fun goToCompanyCrud(v: View) = safeExecute({}()) {
        startActivity(Intent(this, CompanyCrud::class.java))
    }
}
