package io.eyram.speechsmith.ui.screens.pictureSpell

import android.net.ConnectivityManager
import android.net.Network
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.skydoves.sandwich.getOrNull
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eyram.speechsmith.R
import io.eyram.speechsmith.data.repository.SpeechSmithRepository
import io.eyram.speechsmith.ui.components.SpellFieldInputState
import io.eyram.speechsmith.ui.components.SpellFieldState
import io.eyram.speechsmith.ui.screens.audioSpell.AudioPlayerState
import io.eyram.speechsmith.ui.screens.audioSpell.ConnectivityStatus
import io.eyram.speechsmith.ui.screens.audioSpell.FEEDBACK_AUDIO
import io.eyram.speechsmith.util.generateKeyboardLabels
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PictureSpellViewModel @Inject constructor(
    private val repository: SpeechSmithRepository,
    private val connectivityManager: ConnectivityManager,
    private val audioPlayer: ExoPlayer,
) : ViewModel() {

    var uiState by mutableStateOf(PictureSpellScreenState())
        private set

    private var spellFieldState by mutableStateOf(SpellFieldState(""))
    private var currentWordIndex = 0
    private var wordsToSpell: List<String>

    init {
        wordsToSpell = repository.getWordsToSpell(uiState.totalNumberOfQuestions)
        getWordAndUpdateUiState()
        initializeAudioPlayer()
    }

    private fun initializeAudioPlayer() = audioPlayer.apply {
        volume = 0.75F
        skipSilenceEnabled = true
        prepare()
        addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)

                fun onIsPlayingChanged() = events.contains(Player.EVENT_IS_PLAYING_CHANGED)
                fun onIsLoadingChanged() = events.contains(Player.EVENT_IS_LOADING_CHANGED)
                fun isFeedbackAudio() = player.currentMediaItem?.mediaId != FEEDBACK_AUDIO

                if (onIsPlayingChanged() && isFeedbackAudio()) {
                    uiState = if (player.isPlaying) {
                        uiState.copy(audioPlayerState = AudioPlayerState.Playing)
                    } else {
                        uiState.copy(audioPlayerState = AudioPlayerState.Idle)
                    }
                }

                if (onIsLoadingChanged() && isFeedbackAudio()) {
                    if (player.isLoading) {
                        uiState = uiState.copy(audioPlayerState = AudioPlayerState.Loading)
                    }
                }
            }
        })
    }

    private fun getWordAndUpdateUiState() {
        wordsToSpell[currentWordIndex].apply {
            uiState = uiState.copy(wordToSpell = this)
//            updateAudio(this)
            updateSpellField(this)
            updateKeyboard(this)
            getImage(this)
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

//    private fun updateAudio(wordToSpell: String) {
//        viewModelScope.launch {
//            val word = wordToSpell.toLowerCase(Locale.current)
//            val response = repository.getPronunciation(word)
//            response.onSuccess {
//                getOrNull()?.let {
//                    uiState = uiState.copy(hintAudioUrl = it[0].fileUrl)
//                }
//            }
//        }
//    }

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
        setMediaItem(MediaItem.fromUri(uiState.hintAudioUrl))
        setPlaybackSpeed(0.5F)
        play()
    }

    fun onEnterPress() {
        spellFieldState.run {
            spellCheck()
            getSpellFieldInputState()
        }.also {
            val viState = io.eyram.speechsmith.util.getVisualIndicatorState(it)
            uiState = uiState.copy(visualIndicatorState = viState)
            showVisualIndicatorFor(1500)
            playRightOrWrongAudioFeedback()
            getNextWordOnSpellCorrect(it)
        }
    }

    private fun showVisualIndicatorFor(duration: Long) {
        viewModelScope.launch {
            uiState = uiState.copy(showFieldStateVisualIndicator = true)
            delay(duration)
            uiState = uiState.copy(showFieldStateVisualIndicator = false)
        }
    }

    private fun playRightOrWrongAudioFeedback() {
        if (uiState.visualIndicatorState.message == CORRECT) {
            setFeedbackAudio(R.raw.right_ans_audio)
        }

        if (uiState.visualIndicatorState.message == WRONG) {
            setFeedbackAudio(R.raw.wrong_ans_audio)
        }
        audioPlayer.apply {
            setPlaybackSpeed(1F)
            play()
        }
    }

    private fun setFeedbackAudio(@RawRes audioResource: Int) {
        audioPlayer.setMediaItem(
            MediaItem.Builder()
                .setUri(RawResourceDataSource.buildRawResourceUri(audioResource))
                .setMediaId(FEEDBACK_AUDIO)
                .build()
        )
    }

    private fun getNextWordOnSpellCorrect(it: SpellFieldInputState) {
        viewModelScope.launch {
            delay(1000)
            if (it == SpellFieldInputState.Correct && currentWordIndex != wordsToSpell.lastIndex) {
                onNextPress()
            }
            if (it == SpellFieldInputState.Correct && currentWordIndex == wordsToSpell.lastIndex) {
                uiState = uiState.copy(isExerciseComplete = true)
            }
        }
    }

    private fun getImage(wordToSpell: String) {
        viewModelScope.launch {
            val response = repository.getImage(wordToSpell.toLowerCase(Locale.current))
            response.onSuccess {
                getOrNull()?.let { image ->
//                    val betterPhoto = image.results.maxBy { it.likes }
                    uiState = uiState.copy(imageUrl = image.photos.first().src.small)
                }
            }
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

data class PictureSpellScreenState(
    val imageUrl: String = "",
    val wordToSpell: String = "",
    val hintAudioUrl: String = "",
    val currentExerciseNumber: Int = 0,
    val isExerciseComplete: Boolean = false,
    val audioPlayerState: AudioPlayerState = AudioPlayerState.Idle,
    val totalNumberOfQuestions: Int = 10,
    val keyboardLabels: List<String> = listOf(),
    val showFieldStateVisualIndicator: Boolean = false,
    val spellFieldState: SpellFieldState = SpellFieldState(""),
    val visualIndicatorState: SpellInputVisualIndicatorState =
        SpellInputVisualIndicatorState(Color.Unspecified, INCOMPLETE, R.drawable.ic_incorrect)
)


data class SpellInputVisualIndicatorState(
    val color: Color,
    val message: String,
    @DrawableRes val icon: Int
)

const val CORRECT = "CORRECT"
const val WRONG = "WRONG"
const val INCOMPLETE = "INCOMPLETE"