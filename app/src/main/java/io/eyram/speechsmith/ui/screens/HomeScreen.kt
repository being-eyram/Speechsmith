package io.eyram.speechsmith.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.eyram.speechsmith.ui.components.ExerciseCard
import io.eyram.speechsmith.ui.components.SpeechSmithAppBar
import io.eyram.speechsmith.ui.components.exerciseCardData
import io.eyram.speechsmith.ui.theme.SpeechsmithTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold() { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = exerciseCardData) { data ->

                ExerciseCard(
                    modifier = Modifier,
                    onStartButtonClick = {},
                    data = data
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