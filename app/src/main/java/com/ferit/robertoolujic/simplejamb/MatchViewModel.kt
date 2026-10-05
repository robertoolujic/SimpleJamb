package com.ferit.robertoolujic.simplejamb

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ferit.robertoolujic.simplejamb.ui.theme.AppTheme

class MatchViewModel : ViewModel() {
    val sheets = mutableStateListOf<SheetState>()
    var currentSheetIndex by mutableIntStateOf(0)
    val currentSheet: SheetState
        get() = sheets[currentSheetIndex]

    init {
        addNewSheet()
    }

    fun addNewSheet(
        name: String = "Player ${sheets.size + 1}",
        language: String = LanguageManager.currentLanguageFlow.value,
        theme: AppTheme = ThemeManager.currentThemeFlow.value,
        accentColor: Float = ThemeManager.currentHueFlow.value,
        diceType : DiceType = DiceManager.currentDiceFlow.value
    ) {
        sheets.add(SheetState(name, language,theme,accentColor, diceType))
    }

    fun removeSheet(index: Int) {
        if (index !in sheets.indices) return
        if (sheets.size <= 1) {
            val current = sheets[index]
            current.matrix.forEach { row -> row.fill(-1) }
            current.activeCellId = -1
            current.undoBuffer.clear()
            current.rolls = 0
            current.name = "Player ${sheets.size}"
            return
        }
        sheets.removeAt(index)
        if (currentSheetIndex >= sheets.size) {
            currentSheetIndex = sheets.size - 1
        }
    }
}