package com.halkyproject.pausemenu.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.NumberFormat
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.activities.finance.FinancesAccountEdit
import com.halkyproject.pausemenu.components.CustomTextView
import com.halkyproject.lifehack.model.finances.Currency
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.pausemenu.singletons.FormatSingleton


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_FIN_ACC = "finAccount"
val ACCOUNT_VALUES_TYPE_BANK = arrayOf(FinancialAccount.AccountType.CURRENT, FinancialAccount.AccountType.SAVINGS)

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CompanyCrudCardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CompanyCrudCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AccountCrudCardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var obj: FinancialAccount
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            obj = it.getSerializable(ARG_FIN_ACC) as FinancialAccount
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_fin_account_crud_card, container, false)

        v.findViewById<CustomTextView>(R.id.m_accountName).text = obj.name
        v.findViewById<CustomTextView>(R.id.accountInfo).text = if (ACCOUNT_VALUES_TYPE_BANK.contains(obj.type) && obj.currency == Currency.BRL) "${obj.bankNumber} - ${obj.branch} - ${FormatSingleton.mask(obj.number
                ?: "", FormatSingleton.FORMAT_FINANCIAL_ACCOUNT)}" else "${obj.type}"
        v.findViewById<CustomTextView>(R.id.accountType).text = getString(resources.getIdentifier(obj.type.localeEntry, "string", "com.halkyproject.pausemenu"))
        v.findViewById<CustomTextView>(R.id.accountBalance).text = NumberFormat.getCurrencyInstance(obj.currency.locale).format(obj.balance)
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(obj: FinancialAccount) =
                AccountCrudCardFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_FIN_ACC, obj)
                    }
                }
    }
}
