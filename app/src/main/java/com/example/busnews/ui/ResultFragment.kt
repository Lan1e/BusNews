package com.example.busnews.ui

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.example.busnews.R
import com.example.busnews.data.BusInfoModel

class ResultFragment : PreferenceFragmentCompat() {
    private val viewModel get() = (activity as? MainActivity)?.viewModel
    private val categoryResult by lazy {
        findPreference<PreferenceCategory>("result_category")!!
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_result_fragment)

        bindObservers()
    }

    private fun bindObservers() {
        (activity as? MainActivity)?.let { activity ->
            viewModel?.apply {
                result.observe(activity) {
                    updateResults(it)
                }
            }
        }
    }

    private fun updateResults(results: List<BusInfoModel>) {
        clearPrefCategory()
        addPrefCategoryContents(results)
    }

    private fun clearPrefCategory() =
        categoryResult.removeAll()

    private fun addPrefCategoryContents(results: List<BusInfoModel>) {
        results.forEach {
            categoryResult.addPreference(Preference(activity).apply {
                title = it.estimateDelay
                summary = it.plateNumber
            })
        }
    }
}