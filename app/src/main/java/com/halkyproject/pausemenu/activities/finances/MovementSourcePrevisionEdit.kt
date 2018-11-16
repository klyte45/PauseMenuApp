package com.halkyproject.pausemenu.activities.finances

import android.graphics.Color
import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.halkyproject.lifehack.model.finances.MovementSourcePrevision
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.adapter.SpinnerTypeAdapter
import com.halkyproject.pausemenu.extensions.getOptions
import com.halkyproject.pausemenu.fragments.DatePickerFragment
import com.halkyproject.pausemenu.singletons.FormatSingleton
import com.halkyproject.pausemenu.singletons.finances.MovementSourcePrevisionService
import com.halkyproject.pausemenu.superclasses.BasicActivity
import com.halkyproject.pausemenu.superclasses.BasicFragment
import com.halkyproject.pausemenu.wrappers.DefaultI18nWrapper
import kotlinx.android.synthetic.main.activity_finances_movement_source_prevision_edit.*
import java.math.BigDecimal
import java.util.*
import kotlin.math.min

class MovementSourcePrevisionEdit : BasicActivity() {

    private var editingObject: MovementSourcePrevision? = null
    private var parentId: Int = -1

    private var dtInit: Calendar? = null
        set(value) {
            m_dtInit.text = if (value != null) String.format("%1\$td/%1\$Tb/%1\$tY", value) else "--/--/----"
            field = value
        }

    private val defaultListenerFrequency = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) = safeExecute({}()) {
            redrawForm((m_spinnerFreq.selectedItem as DefaultI18nWrapper<MovementSourcePrevision.Frequency>).item)
        }

        override fun onNothingSelected(parentView: AdapterView<*>?) = safeExecute({}()) {
            redrawForm(MovementSourcePrevision.Frequency.NONE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) = safeExecute({}(), true) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finances_movement_source_prevision_edit)

        m_spinnerFreq.adapter = SpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, MovementSourcePrevision.Frequency.values().map { DefaultI18nWrapper(this, it) }, 14f)
        m_spinnerFreq.onItemSelectedListener = defaultListenerFrequency
        m_value.addTextChangedListener(FormatSingleton.maskNumberInput(m_value, resources.configuration.locale))
        val editId: Int = intent?.extras?.getInt(BasicFragment.KEY_EDIT_ID) ?: -1
        parentId = intent?.extras?.getInt(MovementSourcePrevisionList.KEY_PARENT_ID) ?: -1
        val parentName = intent?.extras?.getString(MovementSourcePrevisionList.KEY_PARENT_NAME)
        if (editId != -1) {
            editingObject = MovementSourcePrevisionService.findById(editId)
        }
        when {
            editingObject != null -> {
                with(editingObject!!) {
                    dtInit = Calendar.getInstance()
                    dtInit!!.time = startDate
                    m_spinnerFreq.setSelection(frequency.ordinal)
                    redrawForm(frequency)
                    m_utilDays.isChecked = refDayUtil
                    m_value.setText(if ((percValue?.toFloat()
                                    ?: 0f) <= 0f) value!!.setScale(2).toString() else percValue!!.setScale(2).toString())
                    if (percValue != null) {
                        op_percentage.isChecked = true
                    } else {
                        op_valueAbsolute.isChecked = true
                    }
                }
                for (x in arrayOf(m_layoutDate, m_dtInit, m_utilDays, m_value, m_spinnerFrequencyDay, m_spinnerFrequencyMonth, m_spinnerFreq, op_valueAbsolute, op_percentage)) {
                    x.isEnabled = false
                }
                m_dtInit.setTextColor(Color.GRAY)
                m_saveButton.visibility = View.GONE
            }
            parentId != -1 -> {
                dtInit = null
                m_value.setText("0")
            }
            else -> finish()
        }

        m_title.text = String.format(getString(R.string.finances_previsionOf), parentName)
        closeLoadingScreen()
    }

    override fun onStart() {
        super.onStart()
        if (editingObject != null) {
            with(editingObject!!) {
                m_spinnerFrequencyDay.setSelection((refDay
                        ?: 0) + (if (frequency in arrayOf(MovementSourcePrevision.Frequency.MONTHLY, MovementSourcePrevision.Frequency.YEARLY, MovementSourcePrevision.Frequency.FIFTHLY)) -1 else 0))
                m_spinnerFrequencyMonth.setSelection(refMonth ?: 0)
            }
        }
    }

    fun showPickerDtInicio(v: View) {
        val newFragment = DatePickerFragment()
        newFragment.setCallback {
            dtInit = it
        }
        newFragment.currentValue = dtInit ?: Calendar.getInstance()
        newFragment.show(supportFragmentManager, "dtInit")
    }

    private fun redrawForm(frequencySelected: MovementSourcePrevision.Frequency) = safeExecute({}()) {
        val selValDay = m_spinnerFrequencyDay.selectedItemPosition
        val selValMonth = m_spinnerFrequencyMonth.selectedItemPosition
        when (frequencySelected) {
            MovementSourcePrevision.Frequency.WEEKLY,
            MovementSourcePrevision.Frequency.FIFTHLY,
            MovementSourcePrevision.Frequency.MONTHLY -> {
                m_layoutDayFreq.visibility = View.VISIBLE
                m_layoutMonthFreq.visibility = View.GONE

            }
            MovementSourcePrevision.Frequency.YEARLY -> {
                m_layoutDayFreq.visibility = View.VISIBLE
                m_layoutMonthFreq.visibility = View.VISIBLE

            }
            MovementSourcePrevision.Frequency.DAILY, MovementSourcePrevision.Frequency.NONE -> {
                m_layoutDayFreq.visibility = View.GONE
                m_layoutMonthFreq.visibility = View.GONE
            }
        }
        m_utilDays.visibility =
                if (frequencySelected == MovementSourcePrevision.Frequency.MONTHLY) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        val newOptions = frequencySelected.getOptions()
        m_spinnerFrequencyDay.adapter = SpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, newOptions.first
                ?: arrayListOf(), 14f)
        m_spinnerFrequencyMonth.adapter = SpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, newOptions.second
                ?: arrayListOf(), 14f)

        m_spinnerFrequencyDay.setSelection(min(selValDay, (newOptions.first?.size ?: 0) - 1))
        m_spinnerFrequencyMonth.setSelection(min(selValMonth, (newOptions.second?.size ?: 0) - 1))
    }

    fun save(v: View) = safeExecute({}()) {
        if (editingObject != null) finish()

        if (dtInit == null) throw IllegalStateException("Data inicial deve ser preenchida!")


        val nf = NumberFormat.getInstance() as DecimalFormat
        val decSep = nf.decimalFormatSymbols.decimalSeparator
        val tsdSep = nf.decimalFormatSymbols.groupingSeparator

        val currentValue: BigDecimal = FormatSingleton.toBigDecimal(m_value.text.toString(), decSep, tsdSep)

        if (currentValue.toFloat() < 0.01f) throw IllegalStateException("Valor não pode ser zero!")

        val freq = (m_spinnerFreq.selectedItem as DefaultI18nWrapper<MovementSourcePrevision.Frequency>).item
        var dayVal: Int? = null
        var monthVal: Int? = null
        var utilDays = false
        when (freq) {
            MovementSourcePrevision.Frequency.WEEKLY -> dayVal = m_spinnerFrequencyDay.selectedItemPosition
            MovementSourcePrevision.Frequency.FIFTHLY,
            MovementSourcePrevision.Frequency.MONTHLY -> {
                dayVal = m_spinnerFrequencyDay.selectedItemPosition + 1
                utilDays = m_utilDays.isChecked
            }
            MovementSourcePrevision.Frequency.YEARLY -> {
                dayVal = m_spinnerFrequencyDay.selectedItemPosition + 1
                monthVal = m_spinnerFrequencyMonth.selectedItemPosition
            }
            else -> {
            }
        }

        showLoadingScreen()

        val obj = MovementSourcePrevision()
        with(obj) {
            startDate = dtInit!!.time

            frequency = freq
            refDay = dayVal
            refMonth = monthVal
            movSourceId = parentId
            refDayUtil = utilDays
            when (m_valueConfig.checkedRadioButtonId) {
                op_valueAbsolute.id -> value = currentValue
                op_percentage.id -> percValue = currentValue
            }
            if (!MovementSourcePrevisionService.insert(this).get()) {

            }
        }
        Toast.makeText(applicationContext, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
        setResult(RESULT_OK)
        finish()
    }

}
