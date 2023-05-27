package com.jingom.simplealarmmanager.presentation.home.list

import com.jingom.simplealarmmanager.domain.model.alarm.Alarm

sealed class AlarmListState {
	object Loading: AlarmListState()

	data class Success(
		val alarmList: List<Alarm>
	): AlarmListState()
}
