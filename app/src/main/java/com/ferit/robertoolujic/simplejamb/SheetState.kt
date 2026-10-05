package com.ferit.robertoolujic.simplejamb

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ferit.robertoolujic.simplejamb.ui.theme.AppTheme

class SheetState(
    name: String = "Player 1",
    language: String = LanguageManager.currentLanguageFlow.value,
    theme: AppTheme = ThemeManager.currentThemeFlow.value,
    accentColor: Float = ThemeManager.currentHueFlow.value,
    diceType: DiceType = DiceManager.currentDiceFlow.value
){
    var name by mutableStateOf(name)
    var language by mutableStateOf(language)
    var theme by mutableStateOf(theme)
    var accentColor by mutableFloatStateOf(accentColor)
    var activeCellId by mutableIntStateOf(-1)
    var activeDice = mutableStateListOf(*Array(6) { (1..6).random() })
    var diceType by mutableStateOf(diceType)
    var rolls by mutableIntStateOf(0)
    var matrix = List(16) { mutableStateListOf<Int>().apply { addAll(List(5) { -1 }) } }
    var undoBuffer: MutableList<UndoBuffer> = mutableListOf()
    var settingsType by mutableStateOf(SettingsType.DEFAULT)
}