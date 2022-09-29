package io.eyram.speechsmith.ui.screens.yesandno

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class YesAndNoViewModel @Inject constructor() : ViewModel() {
    val questions = listOf(
        YesOrNoQuestion("Are teachers disciplined by students?", answer = NO),
        YesOrNoQuestion("Are students disciplined by teachers?", answer = YES),
        YesOrNoQuestion("Are bananas fruits?", answer = YES),
        YesOrNoQuestion("Is exercise good for the body?", answer = YES),
        YesOrNoQuestion("Are doctors human beings?", answer = YES),
    )
}

data class YesOrNoQuestion(
    val question: String,
    val answer: String,
)

const val YES = "Yes"
const val NO = "No"