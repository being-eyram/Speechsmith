package io.eyram.speechsmith.ui.screens.audioSpell

import android.net.ConnectivityManager
import android.net.Network
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem.fromUri
import androidx.media3.common.Player
import androidx.media3.datasource.RawResourceDataSource.buildRawResourceUri
import androidx.media3.exoplayer.ExoPlayer
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eyram.speechsmith.R
import io.eyram.speechsmith.data.preferences.AppSettings
import io.eyram.speechsmith.data.preferences.DIFFICULTY_EASY
import io.eyram.speechsmith.data.repository.SpeechSmithRepository
import io.eyram.speechsmith.ui.components.SpellFieldInputState
import io.eyram.speechsmith.ui.components.SpellFieldState
import io.eyram.speechsmith.ui.screens.pictureSpell.CORRECT
import io.eyram.speechsmith.ui.screens.pictureSpell.SpellInputVisualIndicatorState
import io.eyram.speechsmith.ui.screens.pictureSpell.WRONG
import io.eyram.speechsmith.util.defaultViState
import io.eyram.speechsmith.util.generateKeyboardLabels
import io.eyram.speechsmith.util.getVisualIndicatorState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AudioSpellViewModel @Inject constructor(
    private val repository: SpeechSmithRepository,
    private val appSettings: AppSettings,
    private val audioPlayer: ExoPlayer,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    var uiState by mutableStateOf(AudioSpellScreenModel())
        private set

    private var currentWordIndex = 0
    private var wordsToSpell: List<String>
    private var spellFieldState = SpellFieldState("")

    init {
        getUserPreferences()
        wordsToSpell = repository.getWordsToSpell(uiState.totalNumberOfQuestions)
        getWordAndUpdateUiState()
        initializeAudioPlayer()
    }

    private fun initializeAudioPlayer() = audioPlayer.apply {
        prepare()
        volume = 1F
        addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (!isPlaying) uiState = uiState.copy(isAudioPlaying = false)
            }
        })
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
            val word = wordToSpell.toLowerCase(Locale.current)
            val response = repository.getPronunciation(word)
            response.onSuccess {
                getOrNull()?.let {
                    uiState = uiState.copy(audioUrl = it[0].fileUrl)
                }
            }
        }
    }

    private fun getWordAndUpdateUiState() {
        wordsToSpell[currentWordIndex].apply {
            uiState = uiState.copy(wordToSpell = this)
            updateAudio(this)
            updateSpellField(this)
            updateKeyboard(this)
        }
    }

    //https://medium.com/androiddevelopers/datastore-and-synchronous-work-576f3869ec4c
    private fun getUserPreferences() {
        runBlocking {
            appSettings.getTotalAudioQuestions.first().apply {
                uiState = uiState.copy(totalNumberOfQuestions = this)
            }
            appSettings.getAudioSpellDifficulty.first().apply {
                uiState = uiState.copy(exerciseDifficulty = this)
            }
            appSettings.getAudioWordGroup.first().apply {
                uiState = uiState.copy(exerciseWordGroup = this)
            }
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

    fun onPlaySoundClick() = audioPlayer.apply {
        setMediaItem(fromUri(uiState.audioUrl))
        setPlaybackSpeed(0.5F)
        play()
        uiState = uiState.copy(isAudioPlaying = true)
    }

    fun onEnterPress() {
        spellFieldState.run {
            spellCheck()
            getSpellFieldInputState()
        }.also {
            val viState = getVisualIndicatorState(it)
            uiState = uiState.copy(visualIndicatorState = viState)
            showVisualIndicatorFor(1500)
            playRightOrWrongAudioFeedback()
            getNextWordOnSpellCorrect(it)
            //Check if this is the last exercise and do something.
        }
    }

    private fun getNextWordOnSpellCorrect(it: SpellFieldInputState) {
        viewModelScope.launch {
            delay(1000)
            if (it == SpellFieldInputState.Correct) onNextPress()
        }
    }

    private fun playRightOrWrongAudioFeedback() {
        if (uiState.visualIndicatorState.message == CORRECT) {
            audioPlayer.setMediaItem(
                fromUri(buildRawResourceUri(R.raw.right_ans_audio))
            )
        }

        if (uiState.visualIndicatorState.message == WRONG) {
            audioPlayer.setMediaItem(
                fromUri(buildRawResourceUri(R.raw.wrong_ans_audio))
            )
        }
        audioPlayer.apply {
            setPlaybackSpeed(1F)
            play()
        }
    }

    private fun showVisualIndicatorFor(duration: Long) {
        viewModelScope.launch {
            uiState = uiState.copy(showFieldStateIndicator = true)
            delay(duration)
            uiState = uiState.copy(showFieldStateIndicator = false)
        }
    }

// BottomSheet functions to persist user settings

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

    fun listenForConnectivity() = callbackFlow {
        val connectivityListener = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(ConnectivityStatus.Available)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(ConnectivityStatus.Unavailable)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(ConnectivityStatus.Unavailable)
            }
        }
        connectivityManager.registerDefaultNetworkCallback(connectivityListener)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(connectivityListener)
        }
    }
}

//Change the name of this class to Model And use state for handling error and loading states.
data class AudioSpellScreenModel(
    val audioUrl: String = "",
    val wordToSpell: String = "",
    val isAudioPlaying: Boolean = false,
    val currentExerciseNumber: Int = 0,
    val totalNumberOfQuestions: Int = 10,
    val keyboardLabels: List<String> = listOf(),
    val showFieldStateIndicator: Boolean = false,
    val exerciseDifficulty: String = DIFFICULTY_EASY,
    val exerciseWordGroup: String = "Animal - Domestic",
    val spellFieldState: SpellFieldState = SpellFieldState(""),
    val visualIndicatorState: SpellInputVisualIndicatorState = defaultViState
)

sealed class AudioSpellScreenState{
    object Loading : AudioSpellScreenState()
    data class Error(val message : String ) : AudioSpellScreenState()
    data class Success(val screenModel: AudioSpellScreenModel) : AudioSpellScreenState()
}

enum class ConnectivityStatus { Available, Unavailable }