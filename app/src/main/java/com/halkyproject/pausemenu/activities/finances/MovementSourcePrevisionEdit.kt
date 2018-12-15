package com.halkyproject.pausemenu.activities.finances

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.halkyproject.lifehack.model.finances.MovementSource
import com.halkyproject.lifehack.model.finances.MovementSourcePrevision
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.adapter.SimpleSpinnerTypeAdapter
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
    private var parentObj: MovementSource? = null

    private var dtInit: Calendar? = null
        set(value) {
            m_dtInit.text = if (value != null) String.format("%1\$td/%1\$Tb/%1\$tY", value) else "--/--/----"
            field = value
        }

    private val defaultListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) = safeExecute({}()) {
            redrawForm()
        }

        override fun onNothingSelected(parentView: AdapterView<*>?) = safeExecute({}()) {
            redrawForm()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) = safeExecute({}(), true) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finances_movement_source_prevision_edit)

        m_spinnerFreq.adapter = SimpleSpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, MovementSourcePrevision.Frequency.values().map { DefaultI18nWrapper(this, it) }, 14f)
        m_spinnerFreq.onItemSelectedListener = defaultListener
        parentObj = intent?.extras?.getSerializable(MovementSourcePrevisionList.KEY_PARENT) as MovementSource?
        m_spinnerValueType.adapter = SpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, MovementSourcePrevision.ValueType.values().map { DefaultI18nWrapper(this, it) }, 14f, R.color.defaultMenuItemColor, { parentObj }, MovementSourcePrevision.ValueType::class.java)
        m_spinnerValueType.onItemSelectedListener = defaultListener
        m_value.addTextChangedListener(FormatSingleton.maskNumberInput(m_value, resources.configuration.locale))
        val editId: Int = intent?.extras?.getInt(BasicFragment.KEY_EDIT_ID) ?: -1
        if (editId != -1) {
            editingObject = MovementSourcePrevisionService.findById(editId)
        }
        when {
            editingObject != null -> {
                with(editingObject!!) {
                    dtInit = Calendar.getInstance()
                    dtInit!!.time = startDate
                    m_spinnerFreq.setSelection(frequency.ordinal)
                    m_spinnerValueType.setSelection(valueType.ordinal)
                    m_utilDays.isChecked = refDayUtil
                    m_value.setText(value?.setScale(2)?.toString())
                    redrawForm()
                }
            }
            parentObj != null -> {
                dtInit = null
                m_value.setText("0")
            }
            else -> finish()
        }

        m_title.text = String.format(getString(R.string.finances_previsionOf), parentObj!!.name)
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

    private fun redrawForm() = safeExecute({}()) {
        val frequencySelected = (m_spinnerFreq.selectedItem as DefaultI18nWrapper<MovementSourcePrevision.Frequency>).item
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
            MovementSourcePrevision.Frequency.DAILY,
            MovementSourcePrevision.Frequency.ONCE,
            MovementSourcePrevision.Frequency.DISABLED -> {
                m_layoutDayFreq.visibility = View.GONE
                m_layoutMonthFreq.visibility = View.GONE
            }
        }
        if (frequencySelected == MovementSourcePrevision.Frequency.DISABLED) {
            m_valueLayout.visibility = View.GONE
        } else {
            m_valueLayout.visibility = View.VISIBLE
        }
        m_utilDays.visibility =
                if (frequencySelected == MovementSourcePrevision.Frequency.MONTHLY) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        val newOptions = frequencySelected.getOptions()
        m_spinnerFrequencyDay.adapter = SimpleSpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, newOptions.first
                ?: arrayListOf(), 14f)
        m_spinnerFrequencyMonth.adapter = SimpleSpinnerTypeAdapter(this, android.R.layout.simple_spinner_item, newOptions.second
                ?: arrayListOf(), 14f)

        m_spinnerFrequencyDay.setSelection(min(selValDay, (newOptions.first?.size ?: 0) - 1))
        m_spinnerFrequencyMonth.setSelection(min(selValMonth, (newOptions.second?.size ?: 0) - 1))
        when ((m_spinnerValueType.selectedItem as DefaultI18nWrapper<MovementSourcePrevision.ValueType>).item) {
            MovementSourcePrevision.ValueType.ABSOLUTE_IN,
            MovementSourcePrevision.ValueType.ABSOLUTE_OUT,
            MovementSourcePrevision.ValueType.PERCENTAGE_IN,
            MovementSourcePrevision.ValueType.PERCENTAGE_OUT -> m_value.visibility = View.VISIBLE
            MovementSourcePrevision.ValueType.RESET_IN,
            MovementSourcePrevision.ValueType.RESET_OUT -> m_value.visibility = View.GONE
        }
    }

    fun save(v: View) = safeExecute({}()) {
        if (editingObject != null) finish()

        if (dtInit == null) throw IllegalStateException("Data inicial deve ser preenchida!")


        val nf = NumberFormat.getInstance() as DecimalFormat
        val decSep = nf.decimalFormatSymbols.decimalSeparator
        val tsdSep = nf.decimalFormatSymbols.groupingSeparator

        val currentValue: BigDecimal?

        val freq = (m_spinnerFreq.selectedItem as DefaultI18nWrapper<MovementSourcePrevision.Frequency>).item
        val valType = (m_spinnerValueType.selectedItem as DefaultI18nWrapper<MovementSourcePrevision.ValueType>).item
        var dayVal: Int? = null
        var monthVal: Int? = null
        var utilDays = false
        when (freq) {
            MovementSourcePrevision.Frequency.WEEKLY -> dayVal = m_spinnerFrequencyDay.selectedItemPosition
            MovementSourcePrevision.Frequency.FIFTHLY -> dayVal = m_spinnerFrequencyDay.selectedItemPosition + 1
            MovementSourcePrevision.Frequency.MONTHLY -> {
                dayVal = m_spinnerFrequencyDay.selectedItemPosition + 1
                utilDays = m_utilDays.isChecked
            }
            MovementSourcePrevision.Frequency.YEARLY -> {
                dayVal = m_spinnerFrequencyDay.selectedItemPosition + 1
                monthVal = m_spinnerFrequencyMonth.selectedItemPosition
            }
            MovementSourcePrevision.Frequency.DAILY,
            MovementSourcePrevision.Frequency.ONCE,
            MovementSourcePrevision.Frequency.DISABLED -> {
            }
        }

        when (valType) {

            MovementSourcePrevision.ValueType.ABSOLUTE_IN,
            MovementSourcePrevision.ValueType.ABSOLUTE_OUT,
            MovementSourcePrevision.ValueType.PERCENTAGE_IN,
            MovementSourcePrevision.ValueType.PERCENTAGE_OUT -> {
                currentValue = FormatSingleton.toBigDecimal(m_value.text.toString(), decSep, tsdSep)
                if (currentValue.toFloat() < 0.01f) throw IllegalStateException("Valor não pode ser zero!")
            }
            MovementSourcePrevision.ValueType.RESET_IN,
            MovementSourcePrevision.ValueType.RESET_OUT -> currentValue = null

        }

        showLoadingScreen()

        val obj = editingObject ?: MovementSourcePrevision()
        with(obj) {
            startDate = dtInit!!.time

            frequency = freq
            refDay = dayVal
            refMonth = monthVal
            movSourceId = parentObj!!.id
            refDayUtil = utilDays
            value = currentValue
            valueType = valType

            if (id != null) {
                if (!MovementSourcePrevisionService.update(this).get()) {
                    return@safeExecute
                }
            } else {
                if (!MovementSourcePrevisionService.insert(this).get()) {
                    return@safeExecute
                }
            }
        }
        Toast.makeText(applicationContext, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
        setResult(RESULT_OK)
        finish()
    }

}
