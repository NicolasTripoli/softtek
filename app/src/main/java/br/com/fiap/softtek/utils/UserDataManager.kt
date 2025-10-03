package br.com.fiap.softtek.utils

import android.content.Context

object UserDataManager {

    private const val PREFS_NAME = "user_data"
    private const val KEY = "cpf"

    fun saveCpf(context: Context, token: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(KEY, token)
            apply()
        }
    }

    fun getCpf(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY, null)
    }
}