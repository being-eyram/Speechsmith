package io.eyram.speechsmith

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import dagger.hilt.android.AndroidEntryPoint
import io.eyram.speechsmith.data.preferences.AppSettings
import io.eyram.speechsmith.ui.screens.audioSpell.AudioSpellScreen
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appSettings: AppSettings

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpeechsmithTheme {
                AudioSpellScreen(onHomeClick = {})
            }
        }
    }
}