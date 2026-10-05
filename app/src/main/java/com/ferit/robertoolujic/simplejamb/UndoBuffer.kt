package com.ferit.robertoolujic.simplejamb

data class UndoBuffer(
    val index: Int,
    val diceRolls: List<Int>,
    val roll: Int
) {
}