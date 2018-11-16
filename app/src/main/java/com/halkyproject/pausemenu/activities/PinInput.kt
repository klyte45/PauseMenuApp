package com.halkyproject.pausemenu.activities

import android.os.Bundle
import android.view.View
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.singletons.ConfigSingleton
import com.halkyproject.pausemenu.superclasses.BasicActivity
import kotlinx.android.synthetic.main.activity_pin_input.*

class PinInput : BasicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) = safeExecute({}()) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_input)
        closeLoadingScreen()
    }

    fun validatePin(v: View) = safeExecute({}()) {
        showLoadingScreen()
        ConfigSingleton.getPin().observe(this, android.arch.lifecycle.Observer {
            if (it == insertPin.text.toString()) {
                setResult(RESULT_OK)
                finish()
            } else {
                onErrorThrown(Exception("Senha inválida!"))
            }
        })
    }
}
