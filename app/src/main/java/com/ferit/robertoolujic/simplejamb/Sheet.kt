package com.ferit.robertoolujic.simplejamb

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val labelText = listOf(
    R.string.bonus,
    R.string.max,
    R.string.min,
    R.string.minMax,
    R.string.straight,
    R.string.full,
    R.string.poker,
    R.string.yamb
)
val specialRows = setOf(0, 7, 10, 15)

val arrowDrawables =
    listOf(R.drawable.arrow_down, R.drawable.arrow_middle, R.drawable.arrow_up)

@Composable
fun Sheet(
    matrix: List<SnapshotStateList<Int>>,
    active: Int,
    onCellSelected: (Int) -> Unit,
    togglePlayerBar: () -> Unit,
    modifier : Modifier
) {
    val colConditions: Map<Int, (Int) -> Boolean> = mapOf(
        1 to { cell ->
            ((cell == 1) || matrix[cell - if (cell in setOf(
                    8,
                    11
                )
            ) 2 else 1][1] >= 0) && matrix[cell][1] == -1
        },
        2 to { cell -> matrix[cell][2] == -1 },
        3 to { cell ->
            (cell == 14 || matrix[cell + if (cell in setOf(
                    6,
                    9
                )
            ) 2 else 1][3] >= 0) && matrix[cell][3] == -1
        },
        4 to { cell -> matrix[cell][4] == -1 },
        0 to { false }
    )

    fun getColumnScore(col: Int): Int {
        val topSum = (1..6).sumOf { matrix[it][col].let { v -> if (v >= 0) v else 0 } }
        val displayTop = if (topSum >= 60) topSum + 30 else topSum

        val aces = matrix[1][col]
        val max = matrix[8][col]
        val min = matrix[9][col]
        val displayMid = if (aces == -1 || max == -1 || min == -1) 0 else (max - min) * aces

        val bottomSum = (11..14).sumOf { matrix[it][col].let { v -> if (v >= 0) v else 0 } }

        return displayTop + displayMid + bottomSum
    }
    Column (modifier = modifier){
        repeat(16) { rowIndex ->
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                repeat(5) { columnIndex ->
                    val cellId = columnIndex * 16 + rowIndex
                    val cellValue = matrix[rowIndex][columnIndex]
                    val isSpecial = columnIndex == 0 || rowIndex in specialRows
                    val topSum = (1..6).sumOf { matrix[it][columnIndex].coerceAtLeast(0) }
                    val topAlpha =
                        if ((1..6).any { matrix[it][columnIndex] == -1 }) 0.5f else 1f
                    val midValues = listOf(
                        matrix[1][columnIndex],
                        matrix[8][columnIndex],
                        matrix[9][columnIndex]
                    )
                    val midAlpha = if (-1 in midValues) 0.5f else 1f
                    val bottomSum = (11..14).sumOf { matrix[it][columnIndex].coerceAtLeast(0) }
                    val bottomAlpha =
                        if ((11..14).any { matrix[it][columnIndex] == -1 }) 0.5f else 1f
                    val displayTop = if (topSum >= 60) topSum + 30 else topSum
                    val displayMid =
                        if (midValues.contains(-1)) 0 else (midValues[1] - midValues[2]) * midValues[0]

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .clickable {
                                if (rowIndex == 0 && columnIndex == 0) togglePlayerBar()
                                else if (active != cellId) {
                                    if (active != -1)
                                        matrix[active % 16][active / 16] =
                                            -1
                                    if (!isSpecial) {
                                        if (colConditions[columnIndex]?.invoke(rowIndex)
                                                ?: false
                                        ) {
                                            onCellSelected(cellId)
                                        } else onCellSelected(-1)
                                    }
                                    else onCellSelected(-1)
                                }
                            }
                            .border(
                                if (active == cellId) 2.dp else 1.dp,
                                color = when {
                                    isSpecial -> MaterialTheme.colorScheme.primary
                                    active == cellId -> MaterialTheme.colorScheme.secondary
                                    else -> MaterialTheme.colorScheme.tertiary
                                }
                            )
                            .background(
                                color = if (isSpecial) {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f).copy(
                                        alpha = when (rowIndex) {
                                            7 -> topAlpha / 4; 10 -> midAlpha / 4; 15 -> bottomAlpha / 4; else -> 0.1f
                                        }
                                    )
                                } else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                            )
                    ) {
                        if (columnIndex == 0) {
                            when (rowIndex) {
                                0 -> Text(
                                    stringResource(R.string.game_text),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                in 1..6 -> Image(
                                    painterResource(diceDrawables[rowIndex]),
                                    null
                                )

                                in 7..14 -> Text(
                                    stringResource(labelText[rowIndex - 7]),
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.SemiBold
                                )

                                else -> Text(
                                    (1..4).sumOf { col -> getColumnScore(col) }.toString(),
                                    fontSize = 18.sp, fontWeight = FontWeight.Bold
                                )
                            }
                        } else if (rowIndex == 0) {
                            if (columnIndex in 1..3) Image(
                                painterResource(arrowDrawables[columnIndex - 1]),
                                null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                            )
                            else Text(
                                stringResource(R.string.call),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else Text(
                            text = when (rowIndex) {
                                7 -> if (topSum <= 0) "" else "$displayTop"
                                10 -> if (midValues.contains(-1)) "" else "$displayMid"
                                15 -> if (bottomSum <= 0) "" else "$bottomSum"
                                else -> if (cellValue == -1) "" else "$cellValue"
                            },
                            fontSize = 20.sp, textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}