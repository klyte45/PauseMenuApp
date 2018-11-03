package com.halkyproject.pausemenu.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.singletons.ConfigSingleton
import kotlinx.android.synthetic.main.activity_pin_input.*

class PinInput : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_input)
        m_loadingFrame.visibility = View.GONE
    }

    fun validatePin(v: View) {
        m_loadingFrame.visibility = View.VISIBLE

        ConfigSingleton.getPin().observe(this, android.arch.lifecycle.Observer {
            try {
                if (it == insertPin.text.toString()) {
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Senha inválida!", Toast.LENGTH_SHORT).show()
                }
            } finally {
                m_loadingFrame.visibility = View.GONE
            }
        })
    }
}
