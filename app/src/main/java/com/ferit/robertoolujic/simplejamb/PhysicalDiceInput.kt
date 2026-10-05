package com.ferit.robertoolujic.simplejamb

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PhysicalDiceInput(
    sheetState: SheetState,
    returnValue: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val cell = sheetState.activeCellId % 16
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row(modifier) {
                Row {
                    when (cell) {
                        in 1..6 -> {
                            var active by remember { mutableIntStateOf(-1) }
                            if (active != -1) returnValue(cell * active)
                            Column {
                                repeat(6) { columnIndex ->
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .wrapContentHeight(Alignment.CenterVertically)
                                            .fillMaxSize()
                                            .clickable {
                                                active = columnIndex
                                            }
                                            .border(
                                                width = if (active == columnIndex) 2.dp
                                                else 1.dp,
                                                color = (if (active == columnIndex)
                                                    MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.tertiary)
                                            )
                                    ) {
                                        Text(
                                            columnIndex.toString(),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                        in setOf(8, 9) -> {
                            val sum = remember {
                                mutableStateListOf<Int>().apply { addAll(List(5) { -1 }) }
                            }
                            Column {
                                repeat(5) { columnIndex ->
                                    NumberPicker(Modifier.weight(1f)) { sum[columnIndex] = it }
                                }

                                if (!sum.contains(-1)) returnValue(sum.sum())
                            }
                        }

                        11 -> {
                            var active by remember { mutableIntStateOf(-1) }
                            if (active != -1)
                                returnValue(76 * (if (active == 0) 0 else 1) - active * 10)
                            Column {
                                repeat(4) { columnIndex ->
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .weight(1f)
                                            .wrapContentHeight(Alignment.CenterVertically)
                                            .aspectRatio(1f)
                                            .clickable {
                                                active = columnIndex
                                            }
                                            .border(
                                                width = if (active == columnIndex) 2.dp
                                                else 1.dp,
                                                color = if (active == columnIndex) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.tertiary
                                            )) {
                                        Text(
                                            columnIndex.toString(),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                        12 -> {
                            val fullSum = remember {
                                mutableStateListOf<Int>().apply { addAll(List(2) { -1 }) }
                            }
                            if (fullSum.contains(0) || (fullSum[0] == fullSum[1]) && !fullSum.contains(-1)) returnValue(
                                0
                            )
                            else if (!fullSum.contains(-1)) returnValue(fullSum[0] * 3 + fullSum[1] * 2 + 30)
                            repeat(2) { rowIndex ->
                                var active by remember { mutableIntStateOf(-1) }
                                Column{
                                    repeat(7) { columnIndex ->
                                        Image(
                                            painterResource(
                                                id = diceDrawables[columnIndex]
                                            ),
                                            contentDescription = "$columnIndex",
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .clip(RoundedCornerShape(15.dp))
                                                .clickable {
                                                    active = columnIndex
                                                    fullSum[rowIndex] = columnIndex
                                                }
                                                .border(
                                                    width = 4.dp,
                                                    color = if (active == columnIndex)
                                                        MaterialTheme.colorScheme.primary
                                                    else Color.Transparent,
                                                    shape = RoundedCornerShape(15.dp)
                                                )
                                        )
                                    }
                                }
                            }
                        }

                        in setOf(13, 14) -> {
                            Column {
                                var active by remember { mutableIntStateOf(-1) }
                                if (active != -1) returnValue(
                                    if (active == 0) 0 else
                                        if (cell == 13)
                                            active * 4 + 40
                                        else active * 5 + 50
                                )
                                repeat(7) { columnIndex ->
                                    Image(
                                        painterResource(
                                            id = diceDrawables[columnIndex]
                                        ),
                                        contentDescription = "$columnIndex",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .weight(1f)
                                            .wrapContentHeight(Alignment.CenterVertically)
                                            .aspectRatio(1f)
                                            .clickable {
                                                active = columnIndex
                                            }
                                            .border(
                                                width = 4.dp,
                                                color = if (active == columnIndex)
                                                    MaterialTheme.colorScheme.primary
                                                else Color.Transparent,
                                                shape = RoundedCornerShape(15.dp)
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
                if (sheetState.activeCellId != -1) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier
                            .fillMaxSize()
                            .clickable {
                                if (sheetState.matrix[sheetState.activeCellId % 16][sheetState.activeCellId / 16] != -1) {
                                    sheetState.undoBuffer.add(
                                        UndoBuffer(
                                            sheetState.activeCellId,
                                            List(6) { 0 },
                                            0
                                        )
                                    )
                                    sheetState.activeCellId = -1
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
        else ->{
            Column(modifier) {
                Column {
                    when (cell) {
                        in 1..6 -> {
                            var active by remember { mutableIntStateOf(-1) }
                            if (active != -1) returnValue(cell * active)
                            Row {
                                repeat(6) { columnIndex ->
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .wrapContentHeight(Alignment.CenterVertically)
                                            .fillMaxSize()
                                            .clickable {
                                                active = columnIndex
                                            }
                                            .border(
                                                width = if (active == columnIndex) 2.dp
                                                else 1.dp,
                                                color = (if (active == columnIndex)
                                                    MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.tertiary)
                                            )
                                    ) {
                                        Text(
                                            columnIndex.toString(),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                        in setOf(8, 9) -> {
                            val sum = remember {
                                mutableStateListOf<Int>().apply { addAll(List(5) { -1 }) }
                            }
                            Row {
                                repeat(5) { columnIndex ->
                                    NumberPicker(Modifier.weight(1f)) { sum[columnIndex] = it }
                                }

                                if (!sum.contains(-1)) returnValue(sum.sum())
                            }
                        }

                        11 -> {
                            var active by remember { mutableIntStateOf(-1) }
                            if (active != -1)
                                returnValue(76 * (if (active == 0) 0 else 1) - active * 10)
                            Row {
                                repeat(4) { columnIndex ->
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .weight(1f)
                                            .wrapContentHeight(Alignment.CenterVertically)
                                            .aspectRatio(1f)
                                            .clickable {
                                                active = columnIndex
                                            }
                                            .border(
                                                width = if (active == columnIndex) 2.dp
                                                else 1.dp,
                                                color = if (active == columnIndex) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.tertiary
                                            )) {
                                        Text(
                                            columnIndex.toString(),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                        12 -> {
                            val fullSum = remember {
                                mutableStateListOf<Int>().apply { addAll(List(2) { -1 }) }
                            }
                            if (fullSum.contains(0) || (fullSum[0] == fullSum[1]) && !fullSum.contains(-1)) returnValue(
                                0
                            )
                            else if (!fullSum.contains(-1)) returnValue(fullSum[0] * 3 + fullSum[1] * 2 + 30)
                            repeat(2) { rowIndex ->
                                var active by remember { mutableIntStateOf(-1) }
                                Row{
                                    repeat(7) { columnIndex ->
                                        Image(
                                            painterResource(
                                                id = diceDrawables[columnIndex]
                                            ),
                                            contentDescription = "$columnIndex",
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .clip(RoundedCornerShape(15.dp))
                                                .clickable {
                                                    active = columnIndex
                                                    fullSum[rowIndex] = columnIndex
                                                }
                                                .border(
                                                    width = 4.dp,
                                                    color = if (active == columnIndex)
                                                        MaterialTheme.colorScheme.primary
                                                    else Color.Transparent,
                                                    shape = RoundedCornerShape(15.dp)
                                                )
                                        )
                                    }
                                }
                            }
                        }

                        in setOf(13, 14) -> {
                            Row {
                                var active by remember { mutableIntStateOf(-1) }
                                if (active != -1) returnValue(
                                    if (active == 0) 0 else
                                        if (cell == 13)
                                            active * 4 + 40
                                        else active * 5 + 50
                                )
                                repeat(7) { columnIndex ->
                                    Image(
                                        painterResource(
                                            id = diceDrawables[columnIndex]
                                        ),
                                        contentDescription = "$columnIndex",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .weight(1f)
                                            .wrapContentHeight(Alignment.CenterVertically)
                                            .aspectRatio(1f)
                                            .clickable {
                                                active = columnIndex
                                            }
                                            .border(
                                                width = 4.dp,
                                                color = if (active == columnIndex)
                                                    MaterialTheme.colorScheme.primary
                                                else Color.Transparent,
                                                shape = RoundedCornerShape(15.dp)
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
                if (sheetState.activeCellId != -1) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier
                            .fillMaxSize()
                            .clickable {
                                if (sheetState.matrix[sheetState.activeCellId % 16][sheetState.activeCellId / 16] != -1) {
                                    sheetState.undoBuffer.add(
                                        UndoBuffer(
                                            sheetState.activeCellId,
                                            List(6) { 0 },
                                            0
                                        )
                                    )
                                    sheetState.activeCellId = -1
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