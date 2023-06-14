package com.jingom.simplealarmmanager.presentation.timer

sealed interface TimerState {

	val selectedTimeInMillis: Long
	val leftTimeInMillis: Long

	data class ReadyToStart(
		override val selectedTimeInMillis: Long
	): TimerState {
		override val leftTimeInMillis: Long
			get() = selectedTimeInMillis
	}

	data class OnGoing(
		override val selectedTimeInMillis: Long,
		override val leftTimeInMillis: Long
	): TimerState

	data class Paused(
		override val selectedTimeInMillis: Long,
		override val leftTimeInMillis: Long
	): TimerState
}