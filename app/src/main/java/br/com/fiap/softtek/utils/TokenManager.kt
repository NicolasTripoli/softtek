package br.com.fiap.softtek.utils

import android.content.Context

object TokenManager {

    private const val PREFS_NAME = "auth_prefs"
    private const val TOKEN_KEY = "token"

    fun saveToken(context: Context, token: String) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun getToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(TOKEN_KEY, null)
    }
}