package com.example.busnews.ui

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.busnews.BusDataManager
import com.example.busnews.R
import com.example.busnews.api.BusAPIHelper
import kotlinx.coroutines.GlobalScope

class FilterFragment : PreferenceFragmentCompat() {

    private val viewModel get() = (activity as? MainActivity)?.viewModel

    private val filterOptionTown by lazy {
        findPreference<ListPreference>("filter_option_town")!!
    }
    private val filterOptionRoute by lazy {
        findPreference<ListPreference>("filter_option_route")!!
    }
    private val filterOptionStop by lazy {
        findPreference<ListPreference>("filter_option_stop")!!
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_filter_fragment)

        updateView()
        bindPrefListener()
        bindObservers()
    }

    private fun updateView() {
        filterOptionTown.summary = filterOptionTown.entry
        filterOptionRoute.summary = filterOptionRoute.value
        filterOptionStop.summary = filterOptionStop.value
    }

    private fun bindPrefListener() {
        filterOptionTown.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                (pref as? ListPreference)?.let {
                    it.summary = it.entries[it.findIndexOfValue(newValue.toString())]
                }
                filterOptionRoute.disable()
                filterOptionStop.disable()
                BusDataManager.updateRoute()
                true
            }
        filterOptionRoute.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                (pref as? ListPreference)?.let {
                    it.summary = it.entries[it.findIndexOfValue(newValue.toString())]
                }
                filterOptionStop.disable()
                BusDataManager.updateStop()
                true
            }
        filterOptionStop.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                (pref as? ListPreference)?.let {
                    it.summary = it.entries[it.findIndexOfValue(newValue.toString())]
                }
                BusDataManager.updateResult()
                true
            }
    }

    private fun ListPreference.disable() =
        apply {
            isEnabled = false
            summary = ""
            value = ""
        }

    private fun bindObservers() {
        (activity as? MainActivity)?.let { activity ->
            viewModel?.apply {
                downTown.observe(activity) {
                    filterOptionTown.apply {
                        isEnabled = true
                    }
                    Log.i("lanie", "Downtown updated: ${it.size}")
                }

                route.observe(activity) {
                    filterOptionRoute.apply {
                        entries = it.toTypedArray()
                        entryValues = it.toTypedArray()
                        isEnabled = true
                    }
                    Log.i("lanie", "Route updated: ${it.size}")
                }

                stop.observe(activity) {
                    filterOptionStop.apply {
                        entries = it.toTypedArray()
                        entryValues = it.toTypedArray()
                        isEnabled = true
                    }
                    Log.i("lanie", "Stop updated: ${it.size}")
                }

                result.observe(activity) {
                    Log.i("lanie", "Result updated: ${it.size}")
                }
            }
        }
    }
}