package com.jingom.simplealarmmanager.alarm

import com.jingom.simplealarmmanager.common.BootReceiverManager
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import com.jingom.simplealarmmanager.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

interface AlarmController {
	suspend fun upsert(alarm: Alarm)
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

	override suspend fun upsert(alarm: Alarm) {
		val prevAlarm = alarmRepository.get(alarm.id)
		if (prevAlarm != null) {
			appAlarmManager.cancelAlarm(prevAlarm)
		}

		val id = alarmRepository.upsert(alarm)
		if (id > 0 && alarm.alarmOn) {
			val insertedAlarm = alarm.copy(id = id)

			appAlarmManager.registerAlarm(insertedAlarm)

			if (bootReceiverManager.bootReceiverEnabled.not()) {
				bootReceiverManager.enableReceiver()
			}
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