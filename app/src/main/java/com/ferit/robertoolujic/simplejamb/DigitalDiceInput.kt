package com.ferit.robertoolujic.simplejamb

import android.content.res.Configuration
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DigitalDiceInput(
    sheetState: SheetState,
    returnValue: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val cell = sheetState.activeCellId % 16
    val diceRolls = sheetState.activeDice
    val lockedDice = remember { mutableStateListOf(*Array(6) { false }) }
    val rolls = sheetState.rolls
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row(modifier.fillMaxHeight()) {
                Column {
                    repeat(6) { diceIndex ->
                        Image(
                            painterResource(diceDrawables[if (rolls > 0) diceRolls[diceIndex] else 0]),
                            contentDescription = "$diceIndex",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable {
                                    if (rolls > 0)
                                        lockedDice[diceIndex] = !lockedDice[diceIndex]
                                }
                                .border(
                                    width = 4.dp,
                                    color = if (lockedDice[diceIndex]) MaterialTheme.colorScheme.primary
                                    else Color.Transparent,
                                    shape = RoundedCornerShape(15.dp)
                                )
                        )
                    }
                }
                Column {
                    if (rolls < 3)
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .clickable
                                {
                                    sheetState.undoBuffer.add(
                                        UndoBuffer(
                                            sheetState.activeCellId,
                                            diceRolls.toList(),
                                            rolls
                                        )
                                    )
                                    sheetState.rolls++
                                    diceRolls.indices.forEach {
                                        if (!lockedDice[it])
                                            diceRolls[it] = (1..6).random()
                                    }
                                }
                                .border(
                                    1.dp,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                        ) {
                            Text(stringResource(R.string.roll))
                        }
                    if (rolls > 0) {
                        if (sheetState.activeCellId != -1) {
                            when (cell % 16) {
                                in 1..6 -> returnValue(diceRolls.filter { it == cell }.sum())
                                8 -> returnValue(diceRolls.sortedDescending().take(5).sum())
                                9 -> returnValue(diceRolls.sorted().take(5).sum())
                                11 -> returnValue(
                                    if (diceRolls.containsAll(listOf(1, 2, 3, 4, 5)) ||
                                        diceRolls.containsAll(listOf(2, 3, 4, 5, 6))
                                    )
                                        76 - rolls * 10
                                    else 0
                                )

                                12 -> {
                                    val groups =
                                        diceRolls.groupBy { it }.entries.sortedByDescending { it.key }
                                    val threeOfAKindValue =
                                        groups.find { it.value.size >= 3 }?.key ?: 0
                                    val pairValue =
                                        groups.find { it.value.size >= 2 && it.key != threeOfAKindValue }?.key
                                            ?: 0
                                    println("$threeOfAKindValue $pairValue")
                                    if (threeOfAKindValue != 0 && pairValue != 0 && threeOfAKindValue != pairValue) returnValue(
                                        30 + 3 * threeOfAKindValue + 2 * pairValue
                                    )
                                    else returnValue(0)
                                }

                                13 -> {
                                    val poker =
                                        diceRolls.groupBy { it }.entries.find { it.value.size >= 4 }?.key
                                            ?: 0
                                    if (poker == 0) returnValue(0) else returnValue(poker * 4 + 40)
                                }

                                14 -> {
                                    val yamb =
                                        diceRolls.groupBy { it }.entries.find { it.value.size >= 5 }?.key
                                            ?: 0
                                    if (yamb == 0) returnValue(0) else returnValue(yamb * 5 + 50)
                                }
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                                    .clickable {
                                        if (sheetState.matrix[sheetState.activeCellId % 16][sheetState.activeCellId / 16] != -1) {
                                            sheetState.undoBuffer.add(
                                                UndoBuffer(
                                                    sheetState.activeCellId,
                                                    diceRolls,
                                                    rolls
                                                )
                                            )
                                            sheetState.activeCellId = -1
                                            sheetState.rolls = 0
                                            lockedDice.fill(false)
                                        }
                                    }
                                    .border(
                                        if (sheetState.matrix[sheetState.activeCellId % 16][sheetState.activeCellId / 16] == -1)
                                            1.dp
                                        else 2.dp,
                                        color =
                                            if (sheetState.matrix[sheetState.activeCellId % 16][sheetState.activeCellId / 16] == -1)
                                                MaterialTheme.colorScheme.tertiary
                                            else MaterialTheme.colorScheme.primary
                                    )
                            ) {
                                Text(
                                    stringResource(R.string.nextTurn),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        else -> {
            Column(modifier.fillMaxWidth()) {
                Row {
                    repeat(6) { diceIndex ->
                        Image(
                            painterResource(diceDrawables[if (rolls > 0) diceRolls[diceIndex] else 0]),
                            contentDescription = "$diceIndex",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable {
                                    if (rolls > 0)
                                        lockedDice[diceIndex] = !lockedDice[diceIndex]
                                }
                                .border(
                                    width = 4.dp,
                                    color = if (lockedDice[diceIndex]) MaterialTheme.colorScheme.primary
                                    else Color.Transparent,
                                    shape = RoundedCornerShape(15.dp)
                                )
                        )
                    }
                }
                Row {
                    if (rolls < 3)
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .clickable
                                {
                                    sheetState.undoBuffer.add(
                                        UndoBuffer(
                                            sheetState.activeCellId,
                                            diceRolls.toList(),
                                            rolls
                                        )
                                    )
                                    sheetState.rolls++
                                    diceRolls.indices.forEach {
                                        if (!lockedDice[it])
                                            diceRolls[it] = (1..6).random()
                                    }
                                }
                                .border(
                                    1.dp,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                        ) {
                            Text(stringResource(R.string.roll))
                        }
                    if (rolls > 0) {
                        if (sheetState.activeCellId != -1) {
                            when (cell % 16) {
                                in 1..6 -> returnValue(diceRolls.filter { it == cell }.sum())
                                8 -> returnValue(diceRolls.sortedDescending().take(5).sum())
                                9 -> returnValue(diceRolls.sorted().take(5).sum())
                                11 -> returnValue(
                                    if (diceRolls.containsAll(listOf(1, 2, 3, 4, 5)) ||
                                        diceRolls.containsAll(listOf(2, 3, 4, 5, 6))
                                    )
                                        76 - rolls * 10
                                    else 0
                                )

                                12 -> {
                                    val groups =
                                        diceRolls.groupBy { it }.entries.sortedByDescending { it.key }
                                    val threeOfAKindValue =
                                        groups.find { it.value.size >= 3 }?.key ?: 0
                                    val pairValue =
                                        groups.find { it.value.size >= 2 && it.key != threeOfAKindValue }?.key
                                            ?: 0
                                    println("$threeOfAKindValue $pairValue")
                                    if (threeOfAKindValue != 0 && pairValue != 0 && threeOfAKindValue != pairValue) returnValue(
                                        30 + 3 * threeOfAKindValue + 2 * pairValue
                                    )
                                    else returnValue(0)
                                }

                                13 -> {
                                    val poker =
                                        diceRolls.groupBy { it }.entries.find { it.value.size >= 4 }?.key
                                            ?: 0
                                    if (poker == 0) returnValue(0) else returnValue(poker * 4 + 40)
                                }

                                14 -> {
                                    val yamb =
                                        diceRolls.groupBy { it }.entries.find { it.value.size >= 5 }?.key
                                            ?: 0
                                    if (yamb == 0) returnValue(0) else returnValue(yamb * 5 + 50)
                                }
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                                    .clickable {
                                        if (sheetState.matrix[sheetState.activeCellId % 16][sheetState.activeCellId / 16] != -1) {
                                            sheetState.undoBuffer.add(
                                                UndoBuffer(
                                                    sheetState.activeCellId,
                                                    diceRolls,
                                                    rolls
                                                )
                                            )
                                            sheetState.activeCellId = -1
                                            sheetState.rolls = 0
                                            lockedDice.fill(false)
                                        }
                                    }
                                    .border(
                                        if (sheetState.matrix[sheetState.activeCellId % 16][sheetState.activeCellId / 16] == -1)
                                            1.dp
                                        else 2.dp,
                                        color =
                                            if (sheetState.matrix[sheetState.activeCellId % 16][sheetState.activeCellId / 16] == -1)
                                                MaterialTheme.colorScheme.tertiary
                                            else MaterialTheme.colorScheme.primary
                                    )
                            ) {
                                Text(
                                    stringResource(R.string.nextTurn),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}