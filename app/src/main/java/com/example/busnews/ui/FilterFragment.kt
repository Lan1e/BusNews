package com.example.busnews.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.busnews.R

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
        filterOptionTown.summary = filterOptionTown.value
        filterOptionRoute.summary = filterOptionRoute.value
        filterOptionStop.summary = filterOptionStop.value
    }

    private fun bindPrefListener() {
        filterOptionTown.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                pref.summary = newValue.toString()
                true
            }
        filterOptionRoute.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                pref.summary = newValue.toString()
                true
            }
        filterOptionStop.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { pref, newValue ->
                pref.summary = newValue.toString()
                true
            }
    }

    private fun bindObservers() {
        (activity as? MainActivity)?.let { activity ->
            viewModel?.apply {
                downTown.observe(activity) {

                }
            }
        }
    }
}