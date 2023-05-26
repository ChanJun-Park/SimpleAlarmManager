package com.jingom.simplealarmmanager.data.alarm.repository

import com.jingom.simplealarmmanager.data.alarm.dao.AlarmEntityDao
import com.jingom.simplealarmmanager.data.alarm.entity.toDBModel
import com.jingom.simplealarmmanager.data.alarm.entity.toDomainModel
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import com.jingom.simplealarmmanager.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultAlarmRepository(
	private val alarmEntityDao: AlarmEntityDao
): AlarmRepository {

	override suspend fun insert(alarm: Alarm) {
		alarmEntityDao.insert(alarm.toDBModel())
	}

	override suspend fun delete(alarm: Alarm) {
		alarmEntityDao.delete(alarm.toDBModel())
	}

	override fun getAllAlarmFlow(): Flow<Alarm> {
		return alarmEntityDao.getAllFlow().map { it.toDomainModel() }
	}
}