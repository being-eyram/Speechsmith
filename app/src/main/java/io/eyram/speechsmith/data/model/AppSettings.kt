package io.eyram.speechsmith.data.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppSettings(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")
        private val audioSpellDifficultyKey = stringPreferencesKey("audio_spell_difficulty")
        private val totalAudioKey = intPreferencesKey("total_audio_exercises")
        private val audioWordGroupKey = stringPreferencesKey("audio_spell_word_group")
    }

    val getAudioSpellDifficulty: Flow<String>
        get() = context.dataStore.data.map {
            it[audioSpellDifficultyKey] ?: "Easy"
        }

    suspend fun setAudioSpellDifficulty(value: String) {
        context.dataStore.edit {
            it[audioSpellDifficultyKey] = value
        }
    }

    val getAudioWordGroup: Flow<String>
        get() = context.dataStore.data.map {
            it[audioWordGroupKey] ?: "Animals - Domestic"
        }

    suspend fun setAudioWordGroup(value: String) {
        context.dataStore.edit {
            it[audioWordGroupKey] = value
        }
    }

    val getTotalAudioQuestions: Flow<Int>
        get() = context.dataStore.data.map {
            it[totalAudioKey] ?: 10
        }

    suspend fun setTotalNumberOfAudioExercises(value: Int) {
        context.dataStore.edit {
            it[totalAudioKey] = value
        }
    }
}

const val DIFFICULTY_EASY = "Easy"
const val DIFFICULTY_MEDIUM = "Medium"
const val DIFFICULTY_HARD = "Hard"