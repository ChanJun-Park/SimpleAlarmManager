package com.jingom.simplealarmmanager.alarm

import com.jingom.simplealarmmanager.common.BootReceiverManager
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import com.jingom.simplealarmmanager.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

interface AlarmController {
	suspend fun insert(alarm: Alarm)
	suspend fun delete(alarm: Alarm)
	suspend fun get(id: Long): Alarm?
	suspend fun recoverAllAlarm()
	fun getAllAlarmFlow(): Flow<List<Alarm>>
}

class DefaultAlarmController(
	private val alarmRepository: AlarmRepository,
	private val appAlarmManager: AppAlarmManager,
	private val bootReceiverManager: BootReceiverManager
) : AlarmController {

	override suspend fun insert(alarm: Alarm) {
		val prevAlarm = alarmRepository.get(alarm.id)
		if (prevAlarm != null) {
			appAlarmManager.cancelAlarm(prevAlarm)
		}

		appAlarmManager.registerAlarm(alarm)

		alarmRepository.insert(alarm)

		if (bootReceiverManager.bootReceiverEnabled.not()) {
			bootReceiverManager.enableReceiver()
		}
	}

	override suspend fun delete(alarm: Alarm) {
		val prevAlarm = alarmRepository.get(alarm.id)
		if (prevAlarm != null) {
			appAlarmManager.cancelAlarm(prevAlarm)
		}

		alarmRepository.delete(alarm)

		if (needToDisableBootReceiver()) {
			bootReceiverManager.disableReceiver()
		}
	}

	private suspend fun needToDisableBootReceiver() =
		alarmRepository.isAlarmEmpty() && bootReceiverManager.bootReceiverEnabled

	override suspend fun get(id: Long): Alarm? {
		return alarmRepository.get(id)
	}

	override suspend fun recoverAllAlarm() {
		alarmRepository.getAll().forEach {
			appAlarmManager.cancelAlarm(it)
			appAlarmManager.registerAlarm(it)
		}
	}

	override fun getAllAlarmFlow(): Flow<List<Alarm>> {
		return alarmRepository.getAllAlarmFlow()
	}
}