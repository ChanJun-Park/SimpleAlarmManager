package com.jingom.simplealarmmanager.presentation.home.detail

import com.jingom.simplealarmmanager.domain.model.alarm.Alarm

sealed class AlarmDetailState {
	object Loading: AlarmDetailState()
	object Fail: AlarmDetailState()
	data class Success(
		val alarm: Alarm,
		val editState: AlarmDetailEditState
	): AlarmDetailState()
}

sealed class AlarmDetailEditState {
	object Initialized: AlarmDetailEditState()
	object Saved: AlarmDetailEditState()
	object Deleted: AlarmDetailEditState()
	object Edited: AlarmDetailEditState()

	companion object {
		fun AlarmDetailEditState.canSave(): Boolean {
			return (this == Initialized || this == Edited)
		}

		fun AlarmDetailEditState.canEdit(): Boolean {
			return (this == Initialized || this == Edited)
		}
	}
}
