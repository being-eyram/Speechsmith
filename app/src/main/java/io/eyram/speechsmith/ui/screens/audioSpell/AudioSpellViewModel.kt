package io.eyram.speechsmith.ui.screens.audioSpell

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eyram.speechsmith.R
import io.eyram.speechsmith.data.model.AppSettings
import io.eyram.speechsmith.data.model.DIFFICULTY_EASY
import io.eyram.speechsmith.data.repository.SpeechSmithRepository
import io.eyram.speechsmith.ui.components.SpellFieldInputState
import io.eyram.speechsmith.ui.components.SpellFieldState
import io.eyram.speechsmith.ui.screens.pictureSpell.CORRECT
import io.eyram.speechsmith.ui.screens.pictureSpell.INCOMPLETE
import io.eyram.speechsmith.ui.screens.pictureSpell.SpellInputStateVisualIndicatorState
import io.eyram.speechsmith.ui.screens.pictureSpell.WRONG
import io.eyram.speechsmith.util.generateKeyboardLabels
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioSpellViewModel @Inject constructor(
    private val repository: SpeechSmithRepository,
    private val appSettings: AppSettings,
    private val audioPlayer: ExoPlayer
) :
    ViewModel() {

    var uiState by mutableStateOf(AudioSpellScreenState())
        private set

    private var spellFieldState by mutableStateOf(SpellFieldState(""))
    private var keyboardLabels by mutableStateOf(listOf(""))
    private val wordsToSpell = repository.getWordsToSpell(10)
    private var currentWordIndex = 0

    init {
        getWordAndUpdateUiState()
        audioPlayer.apply {
            volume = 1F
            setPlaybackSpeed(0.75F)
            prepare()
        }
    }

    private fun updateAudio(wordToSpell: String) {
        viewModelScope.launch {
            try {
                val word = wordToSpell.toLowerCase(Locale.current)
                repository.getPronunciation(word).apply {
                    if (isSuccessful) {
                        body()?.let {
                            uiState = uiState.copy(audioUrl = it[0].fileUrl)
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    private fun updateKeyboard(wordToSpell: String) {
        keyboardLabels = generateKeyboardLabels(wordToSpell)
        uiState = uiState.copy(keyboardLabels = keyboardLabels)
    }

    private fun updateSpellField(wordToSpell: String) {
        spellFieldState = SpellFieldState(wordToSpell)
        uiState = uiState.copy(spellFieldState = spellFieldState)
    }

    private fun updateSettings() {
        viewModelScope.launch {
            appSettings.getAudioSpellDifficulty.collect {
                uiState = uiState.copy(exerciseDifficulty = it)
            }
        }
        viewModelScope.launch {
            appSettings.getTotalAudioQuestions.collect {
                uiState = uiState.copy(totalNumberOfQuestions = it)
            }
        }
        viewModelScope.launch {
            appSettings.getAudioWordGroup.collect {
                uiState = uiState.copy(exerciseWordGroup = it)
            }
        }
    }


    private fun getWordAndUpdateUiState() = wordsToSpell[currentWordIndex].apply {
        updateAudio(this)
        updateSpellField(this)
        updateKeyboard(this)
        updateSettings()
    }

    fun onNextPress() {
        if (currentWordIndex < wordsToSpell.lastIndex) {
            currentWordIndex += 1
            uiState = uiState.copy(currentExerciseNumber = currentWordIndex)
            getWordAndUpdateUiState()
        }
    }

    fun onPrevPress() {
        if (currentWordIndex > 0) {
            currentWordIndex -= 1
            uiState = uiState.copy(currentExerciseNumber = currentWordIndex)
            getWordAndUpdateUiState()
        }
    }

    fun onPlaySoundClick() {
        audioPlayer.apply {
            setMediaItem(MediaItem.fromUri(uiState.audioUrl))
            println(uiState.audioUrl)
            play()
        }
    }

    fun onEnterPress() {
        spellFieldState.run {
            spellCheck()
            getSpellFieldInputState()
        }.also {
            val visualIndicatorState = getVisualIndicatorState(it)
            uiState = uiState.copy(visualIndicatorState = visualIndicatorState)

            viewModelScope.launch {
                uiState = uiState.copy(showFieldStateIndicator = true)
                delay(1500)
                uiState = uiState.copy(showFieldStateIndicator = false)
            }

            if (uiState.visualIndicatorState.message == CORRECT) {
                audioPlayer.setMediaItem(
                    MediaItem.fromUri(
                        RawResourceDataSource.buildRawResourceUri(R.raw.right_ans_audio)
                    )
                )

                audioPlayer.play()
            }
            if (uiState.visualIndicatorState.message == WRONG) {
                audioPlayer.setMediaItem(
                    MediaItem.fromUri(
                        RawResourceDataSource.buildRawResourceUri(R.raw.wrong_ans_audio)
                    )
                )
                audioPlayer.play()
            }

            viewModelScope.launch {
                delay(1000)
                if (it == SpellFieldInputState.Correct) onNextPress()
            }
        }
    }

    fun onDifficultyDropDownClick(difficulty: String) = viewModelScope.launch {
        appSettings.setAudioSpellDifficulty(difficulty)
    }

    fun onWordGroupDropDownClick(wordGroup: String) = viewModelScope.launch {
        appSettings.setAudioWordGroup(wordGroup)
    }

    fun onAddQuestionsClick() {
        val totalNumberOfQuestions = uiState.totalNumberOfQuestions
        if (totalNumberOfQuestions < 20) {
            val num = totalNumberOfQuestions + 5
            viewModelScope.launch {
                appSettings.setTotalNumberOfAudioExercises(num)
            }
        }
    }

    fun onSubtractQuestionsClick() {
        val totalNumberOfQuestions = uiState.totalNumberOfQuestions
        if (totalNumberOfQuestions > 10) {
            val num = totalNumberOfQuestions - 5
            viewModelScope.launch {
                appSettings.setTotalNumberOfAudioExercises(num)
            }
        }
    }


}

data class AudioSpellScreenState(
    val audioUrl: String = "",
    val currentExerciseNumber: Int = 0,
    val keyboardLabels: List<String> = listOf(),
    val showFieldStateIndicator: Boolean = false,
    val spellFieldState: SpellFieldState = SpellFieldState(""),
    val exerciseDifficulty: String = DIFFICULTY_EASY,
    val totalNumberOfQuestions: Int = 10,
    val exerciseWordGroup: String = "Animal - Domestic",
    val visualIndicatorState: SpellInputStateVisualIndicatorState =
        SpellInputStateVisualIndicatorState(Color.Unspecified, INCOMPLETE, R.drawable.ic_incorrect)
)

private fun getVisualIndicatorState(state: SpellFieldInputState): SpellInputStateVisualIndicatorState {
    return when (state) {

        SpellFieldInputState.Correct -> SpellInputStateVisualIndicatorState(
            color = Color(0xFF538D4E),
            message = CORRECT,
            icon = R.drawable.ic_correct
        )
        SpellFieldInputState.Incorrect -> SpellInputStateVisualIndicatorState(
            color = Color(0xFFBF4040),
            message = WRONG,
            icon = R.drawable.ic_incorrect
        )
        SpellFieldInputState.InComplete -> SpellInputStateVisualIndicatorState(
            color = Color(0xFF3A3A3C),
            message = INCOMPLETE,
            icon = R.drawable.ic_incomplete
        )
    }
}