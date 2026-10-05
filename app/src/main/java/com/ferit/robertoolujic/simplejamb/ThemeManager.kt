package com.ferit.robertoolujic.simplejamb

import android.content.Context
import androidx.core.content.edit
import com.ferit.robertoolujic.simplejamb.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ThemeManager {
    private const val PREFS_NAME = "theme_prefs"
    private const val KEY_HUE = "selected_hue"
    private const val KEY_THEME = "selected_theme"

    private const val DEFAULT_HUE = 300f
    private val _currentHueFlow = MutableStateFlow(DEFAULT_HUE)
    val currentHueFlow: StateFlow<Float> = _currentHueFlow.asStateFlow()
    private val _currentThemeFlow = MutableStateFlow(AppTheme.SYSTEM)
    val currentThemeFlow: StateFlow<AppTheme> = _currentThemeFlow.asStateFlow()
    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedHue = prefs.getFloat(KEY_HUE, DEFAULT_HUE)
        _currentHueFlow.value = savedHue
        val savedTheme = prefs.getString(KEY_THEME, AppTheme.SYSTEM.name) ?: AppTheme.SYSTEM.name
        _currentThemeFlow.value = AppTheme.valueOf(savedTheme)
    }

    fun changeTheme(context: Context, theme: AppTheme, isSystem: Boolean = false) {
        _currentThemeFlow.value = theme
        if (isSystem) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit { putString(KEY_THEME, theme.name) }
        }
    }

    fun changeHue(context: Context, newHue: Float, isSystem: Boolean = false) {
        _currentHueFlow.value = newHue
        if (isSystem) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit { putFloat(KEY_HUE, newHue) }
        }
    }
    fun getTheme(context: Context, isSystem : Boolean = false) : AppTheme {
        if(!isSystem) return _currentThemeFlow.value
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedTheme = prefs.getString(KEY_THEME, AppTheme.SYSTEM.name) ?: AppTheme.SYSTEM.name
        return AppTheme.valueOf(savedTheme)
    }
    fun getHue(context: Context, isSystem : Boolean = false) : Float{
        if(!isSystem) return _currentHueFlow.value
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedHue = prefs.getFloat(KEY_HUE, DEFAULT_HUE)
        return savedHue
    }
}
