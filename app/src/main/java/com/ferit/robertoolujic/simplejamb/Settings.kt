package com.ferit.robertoolujic.simplejamb

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toColorLong
import androidx.compose.ui.text.style.TextAlign

enum class SettingsType { DEFAULT, PLAYER }
@Composable
fun Settings(
    matchViewModel: MatchViewModel,
    onClose: () -> Unit
) {
    val settingsType = matchViewModel.currentSheet.settingsType
    val context = LocalContext.current
    LaunchedEffect(settingsType) {
        println(matchViewModel.currentSheet.theme)
        ThemeManager.changeHue(
            context, if (settingsType == SettingsType.DEFAULT) ThemeManager.getHue(context, true)
            else matchViewModel.currentSheet.accentColor, settingsType == SettingsType.DEFAULT
        )
        ThemeManager.changeTheme(
            context, if (settingsType == SettingsType.DEFAULT) ThemeManager.getTheme(context, true)
            else matchViewModel.currentSheet.theme
        )
        LanguageManager.changeLanguage(context, if (settingsType == SettingsType.DEFAULT) LanguageManager.getLanguage(context, true)
        else matchViewModel.currentSheet.language
        )
        DiceManager.changeDice(context, if(settingsType == SettingsType.DEFAULT) DiceManager.getDice(context, true)
            else matchViewModel.currentSheet.diceType
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(0.5f))
            .clickable(enabled = true, onClick = onClose),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight()
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(24.dp)
                .clickable(enabled = false) { }
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.settings),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clickable { onClose() }
                        .padding(4.dp)) {
                    Text("❌", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .alpha(if (settingsType == SettingsType.DEFAULT) 1f else 0.5f)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(
                            MaterialTheme.colorScheme.primary.copy(
                                if (settingsType == SettingsType.DEFAULT) 0.5f else 0f
                            )
                        )
                        .padding(5.dp)
                        .clickable { matchViewModel.currentSheet.settingsType = SettingsType.DEFAULT }) {
                    Text(stringResource(R.string.defaultSettings), textAlign = TextAlign.Center)
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .alpha(if (settingsType == SettingsType.PLAYER) 1f else 0.5f)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(
                            MaterialTheme.colorScheme.primary.copy(
                                if (settingsType == SettingsType.PLAYER) 0.5f else 0f
                            )
                        )
                        .padding(5.dp)
                        .clickable { matchViewModel.currentSheet.settingsType = SettingsType.PLAYER }) {
                    Text(stringResource(R.string.playerSettings), textAlign = TextAlign.Center)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(R.drawable.ln_hr),
                    contentDescription = "ln_hr",
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .weight(1f)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(
                                if (LanguageManager.currentLanguageFlow.collectAsState().value == "hr") 0.5f else 0f
                            )
                        )
                        .clickable {
                        matchViewModel.currentSheet.language = "hr"
                        LanguageManager.changeLanguage(context,"hr", settingsType == SettingsType.DEFAULT)
                    })
                Image(
                    painterResource(R.drawable.ln_gb),
                    contentDescription = "ln_gb",
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .weight(1f)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(
                                if (LanguageManager.currentLanguageFlow.collectAsState().value == "en") 0.5f else 0f
                            )
                        )
                        .clickable {
                        matchViewModel.currentSheet.language = "en"
                        LanguageManager.changeLanguage(context,"en", settingsType == SettingsType.DEFAULT)
                    })
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(stringResource(R.string.mode), textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(8.dp))
            Row{
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .alpha(if (DiceManager.currentDiceFlow.collectAsState().value == DiceType.DIGITAL) 1f else 0.5f)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(
                            MaterialTheme.colorScheme.primary.copy(
                                if (DiceManager.currentDiceFlow.collectAsState().value == DiceType.DIGITAL) 0.5f else 0f
                            )
                        )
                        .padding(5.dp)
                        .clickable {
                            matchViewModel.currentSheet.diceType = DiceType.DIGITAL
                            DiceManager.changeDice(context, DiceType.DIGITAL, (settingsType == SettingsType.DEFAULT))
                        }   ){
                    Text(stringResource(R.string.digital), textAlign = TextAlign.Center)
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .alpha(if (DiceManager.currentDiceFlow.collectAsState().value == DiceType.PHYSICAL) 1f else 0.5f)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(
                            MaterialTheme.colorScheme.primary.copy(
                                if (DiceManager.currentDiceFlow.collectAsState().value == DiceType.PHYSICAL) 0.5f else 0f
                            )
                        )
                        .padding(5.dp)
                        .clickable {
                            matchViewModel.currentSheet.diceType = DiceType.PHYSICAL
                            DiceManager.changeDice(context, DiceType.PHYSICAL, (settingsType == SettingsType.DEFAULT))
                        }){
                    Text(stringResource(R.string.physical), textAlign = TextAlign.Center)
                    println(MaterialTheme.colorScheme.primary.toColorLong())

                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
            ) {
                ThemeSelector(matchViewModel) {
                    ThemeManager.changeTheme(
                        context,
                        it,
                        (settingsType == SettingsType.DEFAULT)
                    )
                    matchViewModel.currentSheet.theme = it

                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                val context = LocalContext.current
                ColorSlider(
                    initialColorHue = ThemeManager.currentHueFlow.collectAsState().value,
                    onColorChanged = {
                        matchViewModel.currentSheet.accentColor = it
                        ThemeManager.changeHue(context, it, (settingsType == SettingsType.DEFAULT))
                    }
                )
            }
        }
    }
}