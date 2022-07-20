package com.wluo.flickrsearch.storage

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

private const val PREF_SEARCH_QUERY = "searchQuery"

object QueryPreferences {

    // function to get query stored in our sharedPreferences
    fun getStoredQuery(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_SEARCH_QUERY, "")!!
    }

    // function to store the query in our sharedPreferences
    fun setStoredQuery(context: Context, query: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit {
                putString(PREF_SEARCH_QUERY, query)
            }
    }
}