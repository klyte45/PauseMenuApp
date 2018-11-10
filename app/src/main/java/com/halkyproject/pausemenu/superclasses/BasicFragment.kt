package com.halkyproject.pausemenu.superclasses

import android.os.Bundle
import android.support.v4.app.Fragment
import com.halkyproject.lifehack.model.BasicEntityModel

abstract class BasicFragment<Entity> : Fragment() where Entity : BasicEntityModel {
    companion object {

        const val SERIAL_FIELD_NAME: String = "objSerial"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun <Fragment, Entity> newInstance(obj: Entity, clazz: Class<Fragment>) where Fragment : BasicFragment<Entity>, Entity : BasicEntityModel =
                clazz.newInstance().apply {
                    arguments = Bundle().apply {
                        putSerializable(SERIAL_FIELD_NAME, obj)
                    }
                }
    }
}