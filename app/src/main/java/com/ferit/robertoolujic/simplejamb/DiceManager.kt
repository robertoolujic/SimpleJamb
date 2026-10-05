package com.ferit.robertoolujic.simplejamb

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object DiceManager {
    private const val PREFS_NAME = "dice_prefs"
    private const val KEY_DICE = "selected_DICE"
    private val _currentDiceFlow = MutableStateFlow(DiceType.DIGITAL)
    val currentDiceFlow: StateFlow<DiceType> = _currentDiceFlow.asStateFlow()

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedDice = prefs.getString(KEY_DICE, DiceType.DIGITAL.name) ?: DiceType.DIGITAL.name
        _currentDiceFlow.value = DiceType.valueOf(savedDice)
    }

    fun changeDice(context: Context, dice : DiceType, isSystem: Boolean = false) {
        _currentDiceFlow.value = dice
        if (isSystem) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit { putString(KEY_DICE, dice.name) }
        }
    }
    fun getDice(context: Context, isSystem: Boolean = false): DiceType {
        if (!isSystem) return _currentDiceFlow.value
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedDice = prefs.getString(KEY_DICE, DiceType.DIGITAL.name) ?: DiceType.DIGITAL.name
        return DiceType.valueOf(savedDice)
    }
}