package io.eyram.speechsmith.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton // https://www.rockandnull.com/android-hilt-tutorial/ got the idea from this site.
class AppSettings @Inject constructor(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")
        private val audioSpellDifficultyKey = stringPreferencesKey("audio_spell_difficulty")
        private val totalAudioKey = intPreferencesKey("total_audio_exercises")
        private val audioWordGroupKey = stringPreferencesKey("audio_spell_word_group")
    }

    val getAudioSpellDifficulty = context.dataStore.data.map {
        it[audioSpellDifficultyKey] ?: "Easy"
    }

    suspend fun setAudioSpellDifficulty(value: String) = context.dataStore.edit {
        it[audioSpellDifficultyKey] = value
    }


    val getAudioWordGroup = context.dataStore.data.map {
        it[audioWordGroupKey] ?: "Animals - Domestic"
    }

    suspend fun setAudioWordGroup(value: String) = context.dataStore.edit {
        it[audioWordGroupKey] = value
    }


    val getTotalAudioQuestions = context.dataStore.data.map {
        it[totalAudioKey] ?: 10
    }

    suspend fun setTotalNumberOfAudioExercises(value: Int) = context.dataStore.edit {
        it[totalAudioKey] = value
    }
}

const val DIFFICULTY_EASY = "Easy"
const val DIFFICULTY_MEDIUM = "Medium"
const val DIFFICULTY_HARD = "Hard"