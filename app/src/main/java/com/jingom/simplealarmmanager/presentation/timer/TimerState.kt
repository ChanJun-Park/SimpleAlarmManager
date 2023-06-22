package com.jingom.simplealarmmanager.presentation.timer

sealed class TimerState {

	data class ReadyToStart(
		val selectedTimeInMillis: Long
	): TimerState()

	data class OnGoing(
		val timeToLeftInMillis: Long
	): TimerState()

	data class Paused(
		val timeToLeftInMillis: Long
	): TimerState()
}