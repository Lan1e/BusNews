package com.example.busnews.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.example.busnews.R

class MyPreference(
    context: Context?,
    attrs: AttributeSet? = null
) : Preference(context, attrs) {
    init {
        layoutResource = R.layout.layout_my_pref
        summary = " - "
    }

//    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
//        super.onBindViewHolder(holder)
//
//        holder?.itemView?.findViewById<TextView>(R.id.direction)
//        holder?.itemView?.findViewById<TextView>(android.R.id.title)
//        holder?.itemView?.findViewById<TextView>(android.R.id.summary)
//    }
}