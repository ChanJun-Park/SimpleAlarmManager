package com.jingom.simplealarmmanager.presentation.timer

sealed interface TimerState {

	val id: Long
	val selectedTimeInMillis: Long
	val leftTimeInMillis: Long

	data class ReadyToStart(
		override val id: Long,
		override val selectedTimeInMillis: Long
	): TimerState {
		override val leftTimeInMillis: Long
			get() = selectedTimeInMillis
	}

	data class OnGoing(
		override val id: Long,
		override val selectedTimeInMillis: Long,
		override val leftTimeInMillis: Long
	): TimerState

	data class Paused(
		override val id: Long,
		override val selectedTimeInMillis: Long,
		override val leftTimeInMillis: Long
	): TimerState
}