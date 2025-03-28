package ru.madfinal.launguageapp.data.datasource.locale

import android.content.Context
import android.content.SharedPreferences
import ru.madfinal.launguageapp.presentation.main.selector.AppLanguage

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

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setSelectedLanguage(language: AppLanguage) {
        sharedPreferences.edit().putString(KEY_SELECTED_LANGUAGE, language.name).apply()
    }

    fun getSelectedLanguage(): AppLanguage {
        val name = sharedPreferences.getString(KEY_SELECTED_LANGUAGE, AppLanguage.ENGLISH.name)
        return AppLanguage.valueOf(name ?: AppLanguage.ENGLISH.name)
    }

    companion object {
        private const val PREFS_NAME = "user_preferences"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_SCORE = "user_score"
        private const val KEY_SELECTED_LANGUAGE = "selected_language"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }


}