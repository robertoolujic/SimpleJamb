package com.ferit.robertoolujic.simplejamb

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ferit.robertoolujic.simplejamb.ui.theme.AppTheme

@Composable
fun ThemeSelector(matchViewModel: MatchViewModel, onClick : (AppTheme) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val density = LocalDensity.current
    var buttonWidth by remember { mutableStateOf(Dp.Unspecified) }
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primary.copy(0.5f))
            .clickable{ expanded = !expanded }
            .onGloballyPositioned { coordinates ->
                buttonWidth = with(density) { coordinates.size.width.toDp() }
            }) {
        Text(stringResource(R.string.theme))
        DropdownMenu(expanded, { expanded = false },
            modifier = Modifier.then(
                if (buttonWidth != Dp.Unspecified) Modifier.width(buttonWidth) else Modifier
            )) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.systemTheme)) },
                onClick = { onClick(AppTheme.SYSTEM) })
            DropdownMenuItem(
                text = { Text(stringResource(R.string.lightTheme)) },
                onClick = { onClick(AppTheme.LIGHT) })
            DropdownMenuItem(
                text = { Text(stringResource(R.string.darkTheme)) },
                onClick = { onClick(AppTheme.DARK) })
        }
    }
}