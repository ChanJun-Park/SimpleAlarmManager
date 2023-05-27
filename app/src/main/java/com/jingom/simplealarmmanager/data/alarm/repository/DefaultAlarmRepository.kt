package com.jingom.simplealarmmanager.data.alarm.repository

import com.jingom.simplealarmmanager.data.alarm.dao.AlarmEntityDao
import com.jingom.simplealarmmanager.data.alarm.entity.toDBModel
import com.jingom.simplealarmmanager.data.alarm.entity.toDomainModel
import com.jingom.simplealarmmanager.domain.model.alarm.Alarm
import com.jingom.simplealarmmanager.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class DefaultAlarmRepository(
	private val alarmEntityDao: AlarmEntityDao
): AlarmRepository {

	override suspend fun insert(alarm: Alarm) {
		alarmEntityDao.insert(alarm.toDBModel())
	}

	override suspend fun delete(alarm: Alarm) {
		alarmEntityDao.delete(alarm.toDBModel())
	}

	override suspend fun get(id: Long): Alarm? {
		return alarmEntityDao.select(id)?.toDomainModel()
	}

	override fun getAllAlarmFlow(): Flow<List<Alarm>> {
		return alarmEntityDao.getAllFlow().map {
			it.map { alarmEntity -> alarmEntity.toDomainModel() }
		}
	}
}