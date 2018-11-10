package com.halkyproject.pausemenu.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.icu.text.NumberFormat
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.text.Html.FROM_HTML_OPTION_USE_CSS_COLORS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.halkyproject.lifehack.model.finances.Currency
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.activities.finance.FinancesAccountEdit
import com.halkyproject.pausemenu.components.CustomTextView
import com.halkyproject.pausemenu.singletons.FormatSingleton
import com.halkyproject.pausemenu.superclasses.BasicFragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
val ACCOUNT_VALUES_TYPE_BANK = arrayOf(FinancialAccount.AccountType.CURRENT, FinancialAccount.AccountType.SAVINGS)
val ACCOUNT_VALUES_TYPE_NO_BALANCE = arrayOf(FinancialAccount.AccountType.INFINITE_EARNING, FinancialAccount.AccountType.INFINITE_EXPENSE)

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CompanyCrudCardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CompanyCrudCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AccountCrudCardFragment : BasicFragment<FinancialAccount>() {
    // TODO: Rename and change types of parameters
    private lateinit var obj: FinancialAccount
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            obj = it.getSerializable(SERIAL_FIELD_NAME) as FinancialAccount
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_fin_account_crud_card, container, false)

        v.findViewById<CustomTextView>(R.id.m_accountName).text = obj.name
        v.findViewById<CustomTextView>(R.id.accountInfo).text =
                if (ACCOUNT_VALUES_TYPE_BANK.contains(obj.type) && obj.currency == Currency.BRL) {
                    "${obj.bankNumber} - ${obj.branch} - ${FormatSingleton.mask(obj.number
                            ?: "", FormatSingleton.FORMAT_FINANCIAL_ACCOUNT)}"
                } else {
                    "N/A"
                }
        v.findViewById<CustomTextView>(R.id.accountType).text = getString(resources.getIdentifier(obj.type.localeEntry, "string", "com.halkyproject.pausemenu"))
        v.findViewById<CustomTextView>(R.id.accountBalance).text = when {
            obj.type == FinancialAccount.AccountType.INFINITE_EXPENSE -> Html.fromHtml("<font color=\"#FF0000\">${NumberFormat.getCurrencyInstance(obj.currency.locale).currency.symbol} ∞</font>", FROM_HTML_OPTION_USE_CSS_COLORS)
            obj.type == FinancialAccount.AccountType.INFINITE_EARNING -> Html.fromHtml("<font color=\"#00FF00\">${NumberFormat.getCurrencyInstance(obj.currency.locale).currency.symbol} ∞</font>", FROM_HTML_OPTION_USE_CSS_COLORS)
            else -> NumberFormat.getCurrencyInstance(obj.currency.locale).format(obj.balance)
        }

        if (!obj.active) {
            val csl = ColorStateList(arrayOf(IntArray(0)), intArrayOf(resources.getColor(R.color.inactiveBg, null)))
            with(v.findViewById<FrameLayout>(R.id.m_bgLayer)) {
                backgroundTintList = csl
            }
        }

        v.isClickable = true
        v.isFocusable = true
        v.setOnClickListener {
            val intent = Intent(context, FinancesAccountEdit::class.java)
            val b = Bundle()
            b.putInt(FinancesAccountEdit.KEY_EDIT_ID, obj.id ?: -1)
            intent.putExtras(b)
            startActivityForResult(intent, 0)
        }
        return v
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }
}
