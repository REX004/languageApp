package ru.madfinal.launguageapp.data.datasource.locale

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )

    fun saveUserId(userId: String) {
        sharedPreferences.edit().putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): String {
        return sharedPreferences.getString(KEY_USER_ID, "") ?: ""
    }

    fun saveUserScore(score: Int) {
        sharedPreferences.edit().putInt(KEY_USER_SCORE, score).apply()
    }

    fun getUserScore(): Int {
        return sharedPreferences.getInt(KEY_USER_SCORE, 0)
    }

    companion object {
        private const val PREFS_NAME = "user_preferences"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_SCORE = "user_score"
    }
}