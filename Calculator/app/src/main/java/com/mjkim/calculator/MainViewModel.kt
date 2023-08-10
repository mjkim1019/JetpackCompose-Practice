package com.mjkim.calculator

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _rankState: MutableState<Rank> = mutableStateOf(Rank.NONE)
    val rankSate: State<Rank> = _rankState

    private val _heightState: MutableState<Double?> = mutableStateOf(null)
    val heightState: State<Double?> = _heightState

    private val _weightState: MutableState<Double?> = mutableStateOf(null)
    val weightState: State<Double?> = _weightState


    fun calculate(height: Double, weight: Double) {
        val bmi: Double = weight / (height / 100 * height / 100)
        _rankState.value = when {
            bmi <= 18.5 -> Rank.LOW
            bmi <= 22.9 -> Rank.NORMAL
            bmi <= 24.9 -> Rank.OVER
            bmi >= 25 -> Rank.OBESITY
            else -> {
                Rank.NONE
            }
        }
        _heightState.value = height
        _weightState.value = weight
    }

}

enum class Rank(val kor: String) {
    LOW("저체중"), NORMAL("정상"), OVER("과체중"), OBESITY("비만"), NONE("측정이 잘못되었습니다")
}