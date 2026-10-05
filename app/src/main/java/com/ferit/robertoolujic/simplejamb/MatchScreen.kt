package com.ferit.robertoolujic.simplejamb

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchScreen(matchViewModel: MatchViewModel = viewModel(), padding: PaddingValues) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { matchViewModel.sheets.size })
    var showSettingsOverlay by remember { mutableStateOf(false) }
    var showDeleteWarning by remember { mutableStateOf(false) }
    var playerBarActive by remember { mutableStateOf(false) }
    LaunchedEffect(pagerState.currentPage) {
        matchViewModel.currentSheetIndex = pagerState.currentPage
        ThemeManager.changeTheme(context, matchViewModel.currentSheet.theme)
        ThemeManager.changeHue(context, matchViewModel.currentSheet.accentColor)
        LanguageManager.changeLanguage(context, matchViewModel.currentSheet.language)
        DiceManager.changeDice(context, matchViewModel.currentSheet.diceType)
    }
    Box(modifier = Modifier.padding(padding)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            if (playerBarActive)
                PlayerBar(
                    matchViewModel,
                    { showSettingsOverlay = true },
                    { showDeleteWarning = true }
                )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val sheetState = matchViewModel.sheets[page]
                val configuration = LocalConfiguration.current
                when(configuration.orientation){
                    Configuration.ORIENTATION_LANDSCAPE ->{
                        Row {
                            Sheet(
                                matrix = sheetState.matrix,
                                active = sheetState.activeCellId,
                                onCellSelected = { sheetState.activeCellId = it },
                                { playerBarActive = !playerBarActive },
                                modifier = Modifier.weight(4f)
                            )
                            if (sheetState.activeCellId != -1) {
                                if (matchViewModel.currentSheet.diceType == DiceType.DIGITAL) {
                                    DigitalDiceInput(
                                        sheetState,
                                        {
                                            sheetState.matrix[sheetState.activeCellId % 16][sheetState.activeCellId / 16] =
                                                it
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .weight(1f)
                                    )
                                } else
                                    PhysicalDiceInput(
                                        sheetState,
                                        {
                                            sheetState.matrix[sheetState.activeCellId % 16][sheetState.activeCellId / 16] =
                                                it
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .weight(1f)
                                    )
                            }
                        }
                    }
                    else ->{
                        Column {
                            Sheet(
                                matrix = sheetState.matrix,
                                active = sheetState.activeCellId,
                                onCellSelected = { sheetState.activeCellId = it },
                                { playerBarActive = !playerBarActive },
                                modifier = Modifier.weight(4f)
                            )
                            if (sheetState.activeCellId != -1) {
                                if (matchViewModel.currentSheet.diceType == DiceType.DIGITAL) {
                                    DigitalDiceInput(
                                        sheetState,
                                        {
                                            sheetState.matrix[sheetState.activeCellId % 16][sheetState.activeCellId / 16] =
                                                it
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .weight(1f)
                                    )
                                } else
                                    PhysicalDiceInput(
                                        sheetState,
                                        {
                                            sheetState.matrix[sheetState.activeCellId % 16][sheetState.activeCellId / 16] =
                                                it
                                        },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .weight(1f)
                                    )
                            }
                        }
                    }
                }

            }
        }
        if (showSettingsOverlay)
            Box {
                Settings(matchViewModel) { showSettingsOverlay = false }
            }
        if (showDeleteWarning) {
            Box {
                DeleteWarning(matchViewModel) { showDeleteWarning = false }
            }
        }
    }
}