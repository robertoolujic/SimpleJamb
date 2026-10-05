package com.ferit.robertoolujic.simplejamb

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object LanguageManager {
    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_LANGUAGE = "selected_language"
    private val _currentLanguageFlow = MutableStateFlow("en")
    val currentLanguageFlow: StateFlow<String> = _currentLanguageFlow.asStateFlow()
    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedLanguage = prefs.getString(KEY_LANGUAGE, "en") ?: "en"
        _currentLanguageFlow.value = savedLanguage
    }
    fun changeLanguage(context: Context, language: String, isSystem: Boolean = false) {
        _currentLanguageFlow.value = language
        if (isSystem) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit { putString(KEY_LANGUAGE, language) }
        }
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
    }
    fun getLanguage(context: Context, isSystem: Boolean = false): String {
        if (!isSystem) return _currentLanguageFlow.value
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedLanguage = prefs.getString(KEY_LANGUAGE, "en") ?: "en"
        return savedLanguage
    }
}