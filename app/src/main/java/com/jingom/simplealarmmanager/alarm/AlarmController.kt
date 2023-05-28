package com.jingom.simplealarmmanager.alarm

import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import com.jingom.simplealarmmanager.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

interface AlarmController {
	suspend fun insert(alarm: Alarm)
	suspend fun delete(alarm: Alarm)
	suspend fun get(id: Long): Alarm?
	fun getAllAlarmFlow(): Flow<List<Alarm>>
}

class DefaultAlarmController(
	private val alarmRepository: AlarmRepository,
	private val appAlarmManager: AppAlarmManager
) : AlarmController {

	override suspend fun insert(alarm: Alarm) {
		val prevAlarm = alarmRepository.get(alarm.id)
		if (prevAlarm != null) {
			appAlarmManager.cancelAlarm(prevAlarm)
		}

		appAlarmManager.registerAlarm(alarm)

		alarmRepository.insert(alarm)
	}

	override suspend fun delete(alarm: Alarm) {
		val prevAlarm = alarmRepository.get(alarm.id)
		if (prevAlarm != null) {
			appAlarmManager.cancelAlarm(prevAlarm)
		}

		alarmRepository.delete(alarm)
	}

	override suspend fun get(id: Long): Alarm? {
		// DB 에서 알림 로드
		return alarmRepository.get(id)
	}

	override fun getAllAlarmFlow(): Flow<List<Alarm>> {
		// DB 에서 알림 로드
		return alarmRepository.getAllAlarmFlow()
	}
}