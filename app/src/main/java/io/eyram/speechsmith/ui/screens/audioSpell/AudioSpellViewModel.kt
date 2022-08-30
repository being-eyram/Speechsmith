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
import io.eyram.speechsmith.ui.screens.pictureSpell.SpellInputVisualIndicatorState
import io.eyram.speechsmith.ui.screens.pictureSpell.WRONG
import io.eyram.speechsmith.util.generateKeyboardLabels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioSpellViewModel @Inject constructor(
    private val repository: SpeechSmithRepository,
    private val appSettings: AppSettings,
    private val audioPlayer: ExoPlayer
) : ViewModel() {

    var uiState by mutableStateOf(AudioSpellScreenState())
        private set

    private var currentWordIndex = 0
    private var spellFieldState = SpellFieldState("")
    private val wordsToSpell = repository.getWordsToSpell(10)

    init {
        getWordAndUpdateUiState()
        getAppSettings()
        audioPlayer.apply {
            volume = 1F
            setPlaybackSpeed(0.75F)
            prepare()
        }
    }

    private fun updateKeyboard(wordToSpell: String) {
        generateKeyboardLabels(wordToSpell).apply {
            uiState = uiState.copy(keyboardLabels = this)
        }
    }

    private fun updateSpellField(wordToSpell: String) {
        SpellFieldState(wordToSpell).apply {
            spellFieldState = this
            uiState = uiState.copy(spellFieldState = this)
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

    fun getAppSettings() {
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

    private fun getWordAndUpdateUiState() {
        wordsToSpell[currentWordIndex].apply {
            updateAudio(this)
            updateSpellField(this)
            updateKeyboard(this)
        }
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

    fun onEnterPress() = spellFieldState.run {
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

    fun onDifficultyDropDownClick(difficulty: String) {
        uiState = uiState.copy(exerciseDifficulty = difficulty)
    }

    fun onWordGroupDropDownClick(wordGroup: String) {
        uiState = uiState.copy(exerciseWordGroup = wordGroup)
    }

    fun onAddQuestionsClick() {
        uiState = uiState.totalNumberOfQuestions.run {
            if (this < 20) {
                uiState.copy(totalNumberOfQuestions = this + 5)
            } else uiState
        }
    }

    fun onSubtractQuestionsClick() {
        uiState = uiState.totalNumberOfQuestions.run {
            if (this > 10) {
                uiState.copy(totalNumberOfQuestions = this - 5)
            } else uiState
        }
    }

    fun onSaveChangesClick() {
        viewModelScope.launch(Dispatchers.IO) {
            appSettings.setAudioSpellDifficulty(uiState.exerciseDifficulty)
        }
        viewModelScope.launch(Dispatchers.IO) {
            appSettings.setAudioWordGroup(uiState.exerciseWordGroup)
        }
        viewModelScope.launch(Dispatchers.IO) {
            appSettings.setTotalNumberOfAudioExercises(uiState.totalNumberOfQuestions)
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
    val visualIndicatorState: SpellInputVisualIndicatorState =
        SpellInputVisualIndicatorState(Color.Unspecified, INCOMPLETE, R.drawable.ic_incorrect)
)

private fun getVisualIndicatorState(state: SpellFieldInputState): SpellInputVisualIndicatorState {
    return when (state) {

        SpellFieldInputState.Correct -> SpellInputVisualIndicatorState(
            color = Color(0xFF538D4E),
            message = CORRECT,
            icon = R.drawable.ic_correct
        )
        SpellFieldInputState.Incorrect -> SpellInputVisualIndicatorState(
            color = Color(0xFFBF4040),
            message = WRONG,
            icon = R.drawable.ic_incorrect
        )
        SpellFieldInputState.InComplete -> SpellInputVisualIndicatorState(
            color = Color(0xFF3A3A3C),
            message = INCOMPLETE,
            icon = R.drawable.ic_incomplete
        )
    }
}