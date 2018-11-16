package com.halkyproject.pausemenu.superclasses

import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import com.halkyproject.lifehack.interfaces.BasicEntityModel
import com.halkyproject.lifehack.model.finances.FinancialAccount
import com.halkyproject.pausemenu.interfaces.OnFragmentInteractionListener

abstract class BasicListingActivity<Entity, Activity, Fragment> : BasicActivity(), OnFragmentInteractionListener where Entity : BasicEntityModel<Entity>, Activity : BasicListingActivity<Entity, Activity, Fragment>, Fragment : BasicFragment<Entity> {

    private class ReloadAsync<Entity, Activity, Fragment>(private val fragmentClass: Class<Fragment>) : AsyncTask<Activity, Void, Pair<Activity, List<Entity>>>() where Entity : BasicEntityModel<Entity>, Activity : BasicListingActivity<Entity, Activity, Fragment>, Fragment : BasicFragment<Entity> {

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
                    if (supportFragmentManager.fragments.isNotEmpty()) {
                        val trRemove = supportFragmentManager.beginTransaction()
                        for (fragment in supportFragmentManager.fragments) {
                            trRemove.remove(fragment)
                        }
                        trRemove.commit()
                    }
                    if (items.isNotEmpty()) {
                        val trAdd = supportFragmentManager.beginTransaction()
                        for (comp in items) {
                            val frag = BasicFragment.newInstance(comp, fragmentClass)
                            trAdd.add(getScrollLayout().id, frag, "item" + comp.getIdentifier())
                        }
                        trAdd.commit()
                    }
                    closeLoadingScreen()
                }
            } catch (e: Exception) {
                Log.e("ERR!", "Erro post Execute: ${fragmentClass.name}", e)
            } finally {
                result.first.currentRunningAsyncTask = null
            }
        }
    }

    abstract fun runOnBackground(): List<Entity>
    abstract fun getScrollLayout(): LinearLayout
    abstract fun getFragmentClass(): Class<Fragment>

    private var currentRunningAsyncTask: ReloadAsync<Entity, Activity, Fragment>? = null

    private var optionsAccountType: List<FinancialAccount.AccountType?> = ArrayList()
    protected val defaultSpinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) = safeExecute({}()) {
            reload()
        }

        override fun onNothingSelected(parentView: AdapterView<*>?) = safeExecute({}()) {
            reload()
        }
    }


    @Suppress("UNCHECKED_CAST")
    protected fun reload() = safeExecute({}()) {
        showLoadingScreen()
        synchronized(this) {
            if (currentRunningAsyncTask == null) {
                currentRunningAsyncTask = ReloadAsync(getFragmentClass())
                currentRunningAsyncTask!!.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this as Activity)
            }
        }
    }

    override fun onResume() = safeExecute({}()) {
        super.onResume()
        reload()
    }

    override fun onFragmentInteraction(uri: Uri) = safeExecute({}()) {

    }
}