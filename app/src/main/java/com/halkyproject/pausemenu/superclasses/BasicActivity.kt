package com.halkyproject.pausemenu.superclasses

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.interfaces.BasicActivityInterface
import kotlinx.android.synthetic.main.layout_global_loading.*
import kotlinx.android.synthetic.main.layout_global_popup.*


abstract class BasicActivity : AppCompatActivity(), BasicActivityInterface {
    fun showLoadingScreen() {
        ensureLoadingCreated()
        lf_root.visibility = View.VISIBLE
    }

    private fun showError() {
        ensureLoadingCreated()
        lf_root.visibility = View.VISIBLE
    }


    private fun ensureLoadingCreated() {
        if (lf_root == null) {
            View.inflate(this, R.layout.layout_global_loading, findViewById(android.R.id.content))
        }
        if (pm_root == null) {
            View.inflate(this, R.layout.layout_global_popup, findViewById(android.R.id.content))
        }
    }

    fun closeLoadingScreen() {
        ensureLoadingCreated()
        lf_root.visibility = View.GONE
    }

    override fun onErrorThrown(exception: Throwable) {
        ensureLoadingCreated()
        Log.e("ERR!", "Tratamento genérico", exception)
        try {
            var effException = exception
            if (exception is RuntimeException) {
                effException = exception.cause ?: exception
            }
            pm_title.text = effException::class.java.simpleName
            pm_text.text = effException.message
            pm_root.visibility = View.VISIBLE
            closeLoadingScreen()
        } catch (t: Throwable) {
            Log.e("XXX", "QUÊ? ${t.message}")
        }
    }

    fun closeErrorThrown(v: View) {
        pm_root.visibility = View.GONE
    }

    private val defaultRunner: Thread.UncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, paramThrowable ->
        Handler(Looper.getMainLooper()).post {
            onErrorThrown(paramThrowable)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(defaultRunner)
    }

    override fun onResume() {
        super.onResume()
        Thread.setDefaultUncaughtExceptionHandler(defaultRunner)
    }

    inline fun <R> safeExecute(default: R, finalizeOnError: Boolean = false, block: () -> R): R {
        return try {
            block()
        } catch (e: Exception) {
            onErrorThrown(e)
            if (finalizeOnError) {
                pm_okButton.setOnClickListener {
                    closeErrorThrown(it)
                    finish()
                }
            }
            default
        }
    }


}