package com.example.tanahore.preference

import android.content.Context

class UserManager (context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setToken(token: String?) {
        preferences.edit().putString(TOKEN, token).apply()
    }

    fun getToken(): String? {
        return preferences.getString(TOKEN, null)
    }

    fun setTokenCreated(tokenCreated: Long) {
        preferences.edit().putLong(TOKEN_CREATED, tokenCreated).apply()
    }

    fun setEmail(email : String?) {
        preferences.edit().putString(EMAIL, email).apply()
    }

    fun setPassword(password : String) {
        preferences.edit().putString(PASSWORD, password).apply()
    }

    fun setWelcome(isWelcome: Boolean) {
        preferences.edit().putBoolean("isWelcome", isWelcome).apply()
    }

    fun getWelcome(): Boolean {
        return preferences.getBoolean("isWelcome", false)
    }

    companion object {
        private const val PREFS_NAME = "pref"
        private const val TOKEN = "token"
        private const val TOKEN_CREATED = "token_created"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}