package com.jingom.simplealarmmanager.presentation.timealarm.detail

import com.jingom.simplealarmmanager.domain.model.alarm.Alarm

sealed class TimeAlarmDetailState {
	object Loading: TimeAlarmDetailState()
	object Fail: TimeAlarmDetailState()
	data class Success(
		val alarm: Alarm,
		val editState: TimeAlarmDetailEditState
	): TimeAlarmDetailState() {
		val isSavedAlarm: Boolean
			get() = alarm.id != Alarm.INITIAL_ALARM_ID
	}
}

sealed class TimeAlarmDetailEditState {
	object Initialized: TimeAlarmDetailEditState()
	object Saved: TimeAlarmDetailEditState()
	object Deleted: TimeAlarmDetailEditState()
	object Edited: TimeAlarmDetailEditState()

	companion object {
		fun TimeAlarmDetailEditState.canSave(): Boolean {
			return (this == Initialized || this == Edited)
		}

		fun TimeAlarmDetailEditState.canEdit(): Boolean {
			return (this == Initialized || this == Edited)
		}
	}
}
