package com.jingom.simplealarmmanager.domain.model.alarm

import java.time.LocalTime

data class Alarm(
	val id: Long = INITIAL_ALARM_ID,
	val name: String = "",
	val time: LocalTime = LocalTime.now(),
	val alarmOn: Boolean = true
) {
	companion object {
		const val INITIAL_ALARM_ID = 0L
	}
}
