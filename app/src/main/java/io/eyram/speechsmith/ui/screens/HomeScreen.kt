package io.eyram.speechsmith.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.eyram.speechsmith.ui.components.ExerciseCard
import io.eyram.speechsmith.ui.components.SpeechsmithAppBar
import io.eyram.speechsmith.ui.components.exerciseMap
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = { SpeechsmithAppBar() }
    ) { innerPadding ->

        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            exerciseMap.forEach {
                Spacer(modifier = Modifier.height(20.dp))

                ExerciseCard(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = Color(0xFF263238),
                    onStartButtonClick = {},
                    exerciseType = it.key,
                    illustration = it.value
                )
            }
        }
    }
}

@Preview(device = Devices.NEXUS_5)
@Composable
fun HomeScreenPreview() {
    SpeechsmithTheme {
        HomeScreen()
    }
}