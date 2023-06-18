package com.jingom.simplealarmmanager.presentation.timer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Timer
import java.util.TimerTask
import java.util.UUID
import javax.inject.Inject
import kotlin.concurrent.timerTask

@HiltViewModel
class TimerViewModel @Inject constructor() : ViewModel() {

	private var sequence: Long = 0L
	private var timer = Timer()
	private var timerTask: TimerTask? = null
	private val _timerState = MutableStateFlow<TimerState>(
		TimerState.ReadyToStart(
			id = sequence++,
			selectedTimeInMillis = 10 * 1000L
		)
	)
	val timerState = _timerState.asStateFlow()

	fun startTimer() {
		val currentTimerState = timerState.value
		if (currentTimerState !is TimerState.ReadyToStart) {
			return
		}

		timerTask?.cancel()
		_timerState.update {
			TimerState.OnGoing(
				id = sequence++,
				selectedTimeInMillis = currentTimerState.selectedTimeInMillis,
				leftTimeInMillis = currentTimerState.selectedTimeInMillis
			)
		}

		val countDownTimerTask = createTimerTask()
		timerTask = countDownTimerTask
		timer.schedule(countDownTimerTask, 16, 16)
	}

	private fun createTimerTask() = timerTask {
		val currentTimerState = timerState.value
		if (currentTimerState !is TimerState.OnGoing) {
			stopTimer()
			return@timerTask
		}

		val newLeftTimerInMillis = currentTimerState.leftTimeInMillis - 16L
		if (newLeftTimerInMillis <= 0) {
			stopTimer()
			return@timerTask
		}

		_timerState.update {
			TimerState.OnGoing(
				id = currentTimerState.id,
				selectedTimeInMillis = currentTimerState.selectedTimeInMillis,
				leftTimeInMillis = newLeftTimerInMillis
			)
		}
	}

	fun stopTimer() {
		timerTask?.cancel()
		_timerState.update {
			TimerState.ReadyToStart(
				id = sequence++,
				selectedTimeInMillis = 10 * 1000L
			)
		}
	}

	override fun onCleared() {
		super.onCleared()
		runCatching {
			timer.cancel()
		}
	}
}