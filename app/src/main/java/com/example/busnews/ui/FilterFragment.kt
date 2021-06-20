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

        bindPrefListener()
        bindObservers()
        updateView()

    }

    private fun updateView() {
        filterOptionTown.onPreferenceChangeListener.onPreferenceChange(
            filterOptionTown,
            filterOptionTown.value
        )
    }

    private fun bindPrefListener() {
        filterOptionTown.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                newValue ?: return@OnPreferenceChangeListener false
                (pref as? ListPreference)?.let {
                    it.summary = it.entries[it.findIndexOfValue(newValue.toString())]
                    BusDataManager.currentDownTown = newValue.toString()
                }
                Log.e("lanie", "disable")
                filterOptionRoute.disable()
                filterOptionStop.disable()
                BusDataManager.updateRoute()
                true
            }
        filterOptionRoute.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                newValue ?: return@OnPreferenceChangeListener false
                (pref as? ListPreference)?.let {
                    it.summary = it.entries[it.findIndexOfValue(newValue.toString())]
                    BusDataManager.currentSubRoute = newValue.toString()
                }
                filterOptionStop.disable()
                BusDataManager.updateStop()
                true
            }
        filterOptionStop.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                newValue ?: return@OnPreferenceChangeListener false
                (pref as? ListPreference)?.let {
                    it.summary = it.entries[it.findIndexOfValue(newValue.toString())]
                    BusDataManager.currentStop = newValue.toString()
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
                    it ?: return@observe
                    it.map {
                        it.subRoute
                    }.let {
                        filterOptionRoute.apply {
                            entries = it.toTypedArray()
                            entryValues = it.toTypedArray()
                            Log.i("lanie", "route_enable")
                            isEnabled = true
                            val currentRoute = BusDataManager.currentSubRoute
                            if (currentRoute.isNotEmpty() && entryValues.contains(currentRoute)) {
                                Log.i("lanie", "Auto update route = $currentRoute")
                                value = currentRoute
                                onPreferenceChangeListener.onPreferenceChange(this, currentRoute)
                            }
                        }
                    }
                    Log.i("lanie", "Route updated: ${it.size}")
                }

                stop.observe(activity) {
                    it ?: return@observe
                    filterOptionStop.apply {
                        entries = it.toTypedArray()
                        entryValues = it.toTypedArray()
                        Log.i("lanie", "stop_enable")
                        isEnabled = true
                        val currentStop = BusDataManager.currentStop
                        if (currentStop.isNotEmpty() && entryValues.contains(currentStop)) {
                            Log.i("lanie", "Auto update route = $currentStop")
                            value = currentStop
                            onPreferenceChangeListener.onPreferenceChange(this, currentStop)
                        }
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