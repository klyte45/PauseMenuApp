package com.halkyproject.pausemenu.superclasses

import android.net.Uri
import android.os.AsyncTask
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import com.halkyproject.lifehack.model.BasicEntityModel
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.pausemenu.interfaces.OnFragmentInteractionListener

abstract class BasicListingActivity<Entity, Activity, Fragment> : AppCompatActivity(), OnFragmentInteractionListener where Entity : BasicEntityModel, Activity : BasicListingActivity<Entity, Activity, Fragment>, Fragment : BasicFragment<Entity> {

    private class ReloadAsync<Entity, Activity, Fragment>(private val fragmentClass: Class<Fragment>) : AsyncTask<Activity, Void, Pair<Activity, List<Entity>>>() where Entity : BasicEntityModel, Activity : BasicListingActivity<Entity, Activity, Fragment>, Fragment : BasicFragment<Entity> {

        override fun doInBackground(vararg params: Activity?): Pair<Activity, List<Entity>> {
            val list = params[0]!!
            with(list) {
                return Pair(this, list.runOnBackground())
            }
        }

        override fun onPostExecute(result: Pair<Activity, List<Entity>>) {
            try {
                val items = result.second
                with(result.first) {
                    val trRemove = supportFragmentManager.beginTransaction()
                    for (fragment in supportFragmentManager.fragments) {
                        trRemove.remove(fragment)
                    }
                    trRemove.commit()

                    val trAdd = supportFragmentManager.beginTransaction()
                    for (comp in items) {
                        val frag = BasicFragment.newInstance(comp, fragmentClass)
                        trAdd.add(getScrollLayout().id, frag, "item" + comp.getIdentifier())
                    }
                    trAdd.commit()
                    getLoadingFrame().visibility = View.GONE
                }
            } finally {
                result.first.currentRunningAsyncTask = null
            }
        }
    }

    abstract fun runOnBackground(): List<Entity>
    abstract fun getScrollLayout(): LinearLayout
    abstract fun getLoadingFrame(): ConstraintLayout
    abstract fun getFragmentClass(): Class<Fragment>

    private var currentRunningAsyncTask: ReloadAsync<Entity, Activity, Fragment>? = null

    private var optionsAccountType: List<FinancialAccount.AccountType?> = ArrayList()
    protected val defaultSpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
            reload()
        }

        override fun onNothingSelected(parentView: AdapterView<*>) {
            reload()
        }
    }


    @Suppress("UNCHECKED_CAST")
    protected fun reload() {
        getLoadingFrame().visibility = View.VISIBLE
        synchronized(this) {
            if (currentRunningAsyncTask == null) {
                currentRunningAsyncTask = ReloadAsync(getFragmentClass())
                currentRunningAsyncTask!!.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this as Activity)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        reload()
    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}