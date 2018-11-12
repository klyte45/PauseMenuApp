package com.halkyproject.pausemenu.superclasses

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import com.halkyproject.lifehack.interfaces.BasicEntityModel
import com.halkyproject.pausemenu.interfaces.OnFragmentInteractionListener

abstract class BasicFragment<Entity> : Fragment() where Entity : BasicEntityModel<Entity> {

    protected lateinit var obj: Entity
    protected var listener: OnFragmentInteractionListener? = null

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            obj = it.getSerializable(SERIAL_FIELD_NAME) as Entity
        }
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

    companion object {
        const val KEY_EDIT_ID = "EditItemId"
        const val SERIAL_FIELD_NAME: String = "objSerial"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun <Fragment, Entity> newInstance(obj: Entity, clazz: Class<Fragment>): Fragment where Fragment : BasicFragment<Entity>, Entity : BasicEntityModel<Entity> =
                clazz.newInstance().apply {
                    arguments = Bundle().apply {
                        putSerializable(SERIAL_FIELD_NAME, obj)
                    }
                }
    }
}