package com.jingom.simplealarmmanager.presentation.timealarm.list

import com.jingom.simplealarmmanager.domain.model.alarm.Alarm

sealed class TimeAlarmListState {
	object Loading: TimeAlarmListState()

	data class Success(
		val alarmList: List<Alarm>
	): TimeAlarmListState()
}
