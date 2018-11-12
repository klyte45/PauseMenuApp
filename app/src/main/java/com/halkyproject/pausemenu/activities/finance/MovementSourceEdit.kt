package com.halkyproject.pausemenu.activities.finance

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.halkyproject.lifehack.interfaces.Colourizable
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.lifehack.model.finances.MovementSource
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.adapter.SpinnerTypeAdapter
import com.halkyproject.pausemenu.singletons.finances.AccountService
import com.halkyproject.pausemenu.singletons.finances.MovementSourceService
import com.halkyproject.pausemenu.superclasses.BasicFragment
import kotlinx.android.synthetic.main.activity_finances_movement_source_edit.*

class MovementSourceEdit : AppCompatActivity() {

    private var editingObject: MovementSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finances_movement_source_edit)

        val pkOut = AccountService.getPkForEntity(MovementSourceService.getEntityUri(), "outAccountId")
        val pkIn = AccountService.getPkForEntity(MovementSourceService.getEntityUri(), "inAccountId")


        val editId: Int = intent?.extras?.getInt(BasicFragment.KEY_EDIT_ID) ?: -1
        if (editId != -1) {
            editingObject = MovementSourceService.findById(editId)
            if (editingObject != null) {
                m_spinnerOut.adapter = SpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, pkOut, 14f)
                m_spinnerIn.adapter = SpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, pkIn, 14f)
                m_name.setText(editingObject!!.name, TextView.BufferType.EDITABLE)
                m_spinnerIn.setSelection(pkIn.indexOfFirst { x -> x.id == editingObject!!.inAccountId })
                m_spinnerOut.setSelection(pkOut.indexOfFirst { x -> x.id == editingObject!!.outAccountId })

                for (x in arrayOf(m_spinnerIn, m_spinnerOut)) {
                    x?.isEnabled = false
                }
            }
        }
        if (editingObject == null) {
            m_spinnerOut.adapter = SpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, pkOut.filter { it.active }, 14f)
            m_spinnerIn.adapter = SpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, pkIn.filter { it.active }, 14f)
        }
        m_loadingFrame.visibility = View.GONE
    }

    fun save(v: View) {
        val name = m_name.text.toString()
        if (m_spinnerIn.selectedItemPosition == -1) {
            Toast.makeText(applicationContext, "Selecione a conta de entrada!", Toast.LENGTH_SHORT).show()
            return
        }
        if (m_spinnerOut.selectedItemPosition == -1) {
            Toast.makeText(applicationContext, "Selecione a conta de saída!", Toast.LENGTH_SHORT).show()
            return
        }
        val inAccount = (m_spinnerIn.selectedItem as FinancialAccount).id
        val outAccount = (m_spinnerOut.selectedItem as FinancialAccount).id

        if (name.length < 2) {
            Toast.makeText(applicationContext, "Nome muito curto!", Toast.LENGTH_SHORT).show()
            return
        }
        m_loadingFrame.visibility = View.VISIBLE
        try {
            val obj: MovementSource = editingObject ?: MovementSource()
            with(obj) {
                if (editingObject == null) {
                    inAccountId = inAccount
                    outAccountId = outAccount
                }
                this.name = name

                if (id == null) {
                    if (!MovementSourceService.insert(this).get()) {
                        throw java.lang.Exception("Erro na persistência!")
                    }
                } else {
                    if (!MovementSourceService.update(this).get()) {
                        throw java.lang.Exception("Erro na persistência!")
                    }
                }
            }
            Toast.makeText(applicationContext, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Erro ao salvar: " + e.message, Toast.LENGTH_SHORT).show()
            Log.e("ERROR", "Erro salvando fonte de movimentação", e)
            m_loadingFrame.visibility = View.GONE
        }
    }
}
