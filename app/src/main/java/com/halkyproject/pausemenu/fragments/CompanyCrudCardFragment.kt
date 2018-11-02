package com.halkyproject.pausemenu.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.halkyproject.pausemenu.R
import com.halkyproject.pausemenu.activities.CompanyEdit
import com.halkyproject.pausemenu.components.CustomTextView
import com.halkyproject.pausemenu.model.Company
import com.halkyproject.pausemenu.util.MaskEditUtil


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_COMPANY = "company"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CompanyCrudCardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CompanyCrudCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CompanyCrudCardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var company: Company
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            company = it.getSerializable(ARG_COMPANY) as Company
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_company_crud_card, container, false)

        v.findViewById<CustomTextView>(R.id.cityName).text = "${company.cityDisplayName} (${company.getCountryEnum().emoji})"
        v.findViewById<CustomTextView>(R.id.companyName).text = company.mainName
        v.findViewById<CustomTextView>(R.id.realName).text = company.realName
        v.findViewById<CustomTextView>(R.id.documentNumber).text = MaskEditUtil.mask(company.documentNumber, MaskEditUtil.FORMAT_CNPJ)
        v.isClickable = true
        v.isFocusable = true
        v.setOnClickListener {
            val intent = Intent(context, CompanyEdit::class.java)
            val b = Bundle()
            b.putInt(CompanyEdit.KEY_EDIT_ID, company.id ?: -1)
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
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CompanyCrudCardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(company: Company) =
                CompanyCrudCardFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_COMPANY, company)
                    }
                }
    }
}
