package com.ferit.robertoolujic.simplejamb

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeleteWarning(
    matchViewModel: MatchViewModel,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(0.5f))
            .clickable(enabled = true, onClick = onClose),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
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
            Text(
                stringResource(R.string.delete),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clickable { onClose() }
                        .padding(4.dp)) {
                    Text("❌", fontSize = 20.sp)
                }
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clickable { matchViewModel.removeSheet(matchViewModel.currentSheetIndex)
                            ThemeManager.changeTheme(context, matchViewModel.currentSheet.theme)
                            ThemeManager.changeHue(context, matchViewModel.currentSheet.accentColor)
                            onClose()
                        }
                        .padding(4.dp)) {
                    Text("✔", fontSize = 20.sp)
                }
            }
        }
    }
}