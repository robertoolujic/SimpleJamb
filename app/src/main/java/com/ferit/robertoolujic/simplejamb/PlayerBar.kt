package com.ferit.robertoolujic.simplejamb

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlayerBar(
    matchViewModel: MatchViewModel,
    openSettings: () -> Unit,
    openDelete: () -> Unit
) {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp)
                .clickable {
                    openDelete()
                }) {
            Image(
                painterResource(R.drawable.trash), contentDescription = "trash",
                modifier = Modifier.fillMaxSize(),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )
        }
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp)
                .clickable {
                    val active = matchViewModel.currentSheet.activeCellId
                    val undoBuffer = matchViewModel.currentSheet.undoBuffer
                    if (active != -1)
                        matchViewModel.currentSheet.matrix[active % 16][active / 16] = -1
                    val lastAction = undoBuffer.removeLastOrNull()
                    matchViewModel.currentSheet.activeCellId = lastAction?.index ?: 0
                    matchViewModel.currentSheet.activeDice.indices.forEach {
                        matchViewModel.currentSheet.activeDice[it] = lastAction?.diceRolls[it] ?: 0
                    }
                    matchViewModel.currentSheet.rolls = lastAction?.roll ?: 0
                }) {
            Image(
                painterResource(R.drawable.undo), contentDescription = "undo",
                modifier = Modifier.fillMaxSize(),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )
        }
        val focusManager = LocalFocusManager.current
        val isKeyboardVisible = androidx.compose.foundation.layout.WindowInsets.ime
            .getBottom(androidx.compose.ui.platform.LocalDensity.current) > 0
        var isFocused by remember { mutableStateOf(false) }
        LaunchedEffect(isKeyboardVisible) {
            if (!isKeyboardVisible && isFocused) {
                focusManager.clearFocus()
            }
        }
        BasicTextField(
            matchViewModel.currentSheet.name,
            onValueChange = { matchViewModel.currentSheet.name = it },
            textStyle = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }),
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { state ->
                    isFocused = state.isFocused
                }
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Back && isFocused) {
                        focusManager.clearFocus()
                        true
                    } else {
                        false
                    }
                },
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp)
                .clickable {
                    openSettings()
                }) {
            Image(
                painterResource(R.drawable.settings), contentDescription = "settings",
                modifier = Modifier.fillMaxSize(),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )
        }
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(15.dp)
                .clickable {
                    matchViewModel.addNewSheet(
                        theme = ThemeManager.getTheme(context, true),
                        accentColor = ThemeManager.getHue(context, true),
                        language = LanguageManager.getLanguage(context, true),
                        diceType = DiceManager.getDice(context, true)
                    )
                }) {
            Image(
                painterResource(R.drawable.plus), contentDescription = "plus",
                modifier = Modifier.fillMaxSize(),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )
        }
    }
}
