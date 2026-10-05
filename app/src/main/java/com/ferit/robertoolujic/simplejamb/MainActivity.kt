package com.ferit.robertoolujic.simplejamb

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ferit.robertoolujic.simplejamb.ui.theme.SimpleJambTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.init(this)
        DiceManager.init(this)
        LanguageManager.init(this)
        enableEdgeToEdge()
        setContent {
            val currentHue by ThemeManager.currentHueFlow.collectAsStateWithLifecycle()
            val currentTheme by ThemeManager.currentThemeFlow.collectAsStateWithLifecycle()

            SimpleJambTheme(currentTheme, primaryColor = currentHue) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MatchScreen(padding = innerPadding)
                }
            }
        }
    }
}