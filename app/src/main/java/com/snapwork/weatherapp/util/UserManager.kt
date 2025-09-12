package com.snapwork.weatherapp.util

import android.content.Context
import java.security.MessageDigest

class UserManager(private val context: Context) {

    companion object {
        private const val PREFS = "user_prefs"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASS_HASH = "password_hash"
        private const val KEY_SIGNED_IN = "signed_in"
    }

    private val prefs by lazy { context.getSharedPreferences(PREFS, Context.MODE_PRIVATE) }

    /** Registers a user. Returns true on success, false if an account already exists. */
    fun register(email: String, password: String): Boolean {
        val storedEmail = prefs.getString(KEY_EMAIL, null)
        if (storedEmail != null) {
            // single-user app: don't allow overwrite
            return false
        }
        prefs.edit()
            .putString(KEY_EMAIL, email)
            .putString(KEY_PASS_HASH, sha256(password))
            .apply()
        return true
    }

    /** Attempts login. If success, marks signed in and returns true. */
    fun login(email: String, password: String): Boolean {
        val storedEmail = prefs.getString(KEY_EMAIL, null) ?: return false
        val storedHash = prefs.getString(KEY_PASS_HASH, null) ?: return false
        if (storedEmail == email && storedHash == sha256(password)) {
            prefs.edit().putBoolean(KEY_SIGNED_IN, true).apply()
            return true
        }
        return false
    }

    fun isSignedIn(): Boolean = prefs.getBoolean(KEY_SIGNED_IN, false)


    private fun sha256(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
