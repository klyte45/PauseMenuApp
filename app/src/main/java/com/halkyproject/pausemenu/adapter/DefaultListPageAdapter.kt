package com.halkyproject.pausemenu.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.halkyproject.pausemenu.R


class DefaultListPageAdapter(private val context: Context) : PagerAdapter() {

    private val pages: MutableList<ViewGroup> = mutableListOf()

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        pages.add(inflater.inflate(R.layout.layout_global_list, collection, false) as ViewGroup)
        notifyDataSetChanged()
        return pages.last()
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}